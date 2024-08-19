package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.CommentRepository;
import com.application.taskmanagementsystem.dao.TaskRepository;
import com.application.taskmanagementsystem.dao.UserRepository;
import com.application.taskmanagementsystem.exception.TaskNotFoundException;
import com.application.taskmanagementsystem.exception.TaskTakenException;
import com.application.taskmanagementsystem.exception.UserNotFoundException;
import com.application.taskmanagementsystem.model.dto.CommentDTO;
import com.application.taskmanagementsystem.model.dto.StatusDTO;
import com.application.taskmanagementsystem.model.dto.TaskRequest;
import com.application.taskmanagementsystem.model.dto.UpdateTaskDTO;
import com.application.taskmanagementsystem.model.entity.Comment;
import com.application.taskmanagementsystem.model.entity.Task;
import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.model.enumeration.Priority;
import com.application.taskmanagementsystem.model.enumeration.Role;
import com.application.taskmanagementsystem.model.enumeration.Status;
import com.application.taskmanagementsystem.security.JwtCore;
import com.application.taskmanagementsystem.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional
@CacheConfig(cacheNames = "dataUser")
public class UserService implements UserDetailsService {

  private final JwtCore jwtCore;
  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final CommentRepository commentRepository;

  private static final String USER_NOT_FOUND_MESSAGE = "User with name \"%s\" already exists";
  private static final String TASK_CREATED = "Task \"%s\" has been created successfully";
  private static final String TASK_NOT_FOUND = "Task with this ID has not been found!";
  private static final String USER_NOT_FOUND = "User with this ID has not been found!";
  private static final String TASK_ALREADY_EXISTS = "Task \"%s\" already exists for this user";
  private static final String COMMENT_PUBLISHED = "Comment has been published!";
  private static final String STATUS_CHANGED = "Status successfully changed to \"%s\"";
  private static final String TASK_CHANGED = "Task successfully changed";
  private static final String TASK_DELETED = "Task successfully deleted";
  private static final String ACCOMPLISHED_APPOINTMENT =
      "Employee successfully has been appointed!";

  @Autowired
  public UserService(
      UserRepository userRepository,
      TaskRepository taskRepository,
      JwtCore jwtCore,
      CommentRepository commentRepository) {
    this.userRepository = userRepository;
    this.taskRepository = taskRepository;
    this.jwtCore = jwtCore;
    this.commentRepository = commentRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findUserByUsername(username)
        .map(UserDetailsImpl::build)
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
  }

  @Cacheable(key = "#taskRequest.title")
  public String createTask(TaskRequest taskRequest, HttpServletRequest request) {
    User user = getUserByUsername(request);
    if (taskRepository.existsByTitleAndEmployer(taskRequest.getTitle(), user)) {
      throw new TaskTakenException(String.format(TASK_ALREADY_EXISTS, taskRequest.getTitle()));
    }

    Task task =
        Task.builder()
            .title(taskRequest.getTitle())
            .description(taskRequest.getDefinition())
            .priority(taskRequest.getPriority())
            .employer(user)
            .status(taskRequest.getStatus())
            .build();

    taskRepository.save(task);
    return String.format(TASK_CREATED, task.getTitle());
  }

  @CachePut(key = "#taskId")
  public String appointEmployee(Long taskId, Long employeeId, HttpServletRequest request) {
    Task task = getTaskByIdAndEmployer(taskId, request);
    User employee = getUserByIdAndRole(employeeId, Role.EMPLOYEE);
    task.setEmployee(employee);
    taskRepository.save(task);
    return ACCOMPLISHED_APPOINTMENT;
  }

  @Cacheable(key = "'employees'")
  public List<User> getAllEmployees() {
    return userRepository.getAllByRole(Role.EMPLOYEE);
  }

  @Cacheable(key = "#request.remoteUser")
  public List<Task> getUserTasks(HttpServletRequest request) {
    return taskRepository.findTasksByUser(getUserByUsername(request));
  }

  @Cacheable(key = "#id")
  public Page<Task> getTaskByUserId(
      Long id, Integer offset, Integer limit, Status status, Priority priority) {
    User user = getUserById(id);
    Pageable pageable = PageRequest.of(offset, limit);

    if (priority != null && status != null) {
      return taskRepository.findTasksByUserAndPriorityAndStatus(user, priority, status, pageable);
    } else if (priority != null) {
      return taskRepository.findTasksByUserAndPriority(user, priority, pageable);
    } else if (status != null) {
      return taskRepository.findTasksByUserAndStatus(user, status, pageable);
    } else {
      return taskRepository.findTasksByUser(user, pageable);
    }
  }

  @Cacheable(key = "#commentDTO.content")
  public String createComment(Long taskId, CommentDTO commentDTO, HttpServletRequest request) {
    Task task = getTaskById(taskId);
    Comment comment =
        Comment.builder()
            .time(LocalDateTime.now())
            .content(commentDTO.getContent())
            .author(jwtCore.getNameFromJwt(request))
            .task(task)
            .build();
    commentRepository.save(comment);
    return COMMENT_PUBLISHED;
  }

  public String changeStatus(Long taskId, StatusDTO status, HttpServletRequest request) {
    Task task = getTaskByIdAndUser(taskId, request);
    task.setStatus(status.getStatus());
    taskRepository.save(task);
    return String.format(STATUS_CHANGED, status.getStatus().toString());
  }

  @CachePut(key = "#id")
  public String editTask(Long id, UpdateTaskDTO updateTaskDTO, HttpServletRequest request) {
    Task task = getTaskByIdAndUser(id, request);
    updateIfPresent(updateTaskDTO.getTitle(), task::setTitle);
    updateIfPresent(updateTaskDTO.getDescription(), task::setDescription);
    updateIfPresent(updateTaskDTO.getStatus(), task::setStatus);
    updateIfPresent(updateTaskDTO.getPriority(), task::setPriority);
    taskRepository.save(task);
    return TASK_CHANGED;
  }

  @CacheEvict(key = "#id")
  public String deleteTask(Long id, HttpServletRequest request) {
    Task task = getTaskByIdAndUser(id, request);
    User user = getUserByUsername(request);
    user.getTaskSet().remove(task);
    userRepository.save(user);
    taskRepository.delete(task);
    return TASK_DELETED;
  }

  private <T> void updateIfPresent(T value, Consumer<T> setter) {
    Optional.ofNullable(value).ifPresent(setter);
  }

  private Task getTaskByIdAndEmployer(Long taskId, HttpServletRequest request) {
    User employer = getUserByUsername(request);
    return taskRepository
        .findTaskByIdAndEmployer(taskId, employer)
        .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
  }

  private Task getTaskByIdAndUser(Long id, HttpServletRequest request) {
    User user = getUserByUsername(request);
    return taskRepository
        .findTaskByUserAndId(user, id)
        .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
  }

  private User getUserByIdAndRole(Long userId, Role role) {
    return userRepository
        .findUserByIdAndRole(userId, role)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  private User getUserByUsername(HttpServletRequest request) {
    String username = jwtCore.getNameFromJwt(request);
    return userRepository
        .findUserByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

  private User getUserById(Long id) {
    return userRepository
        .findUserById(id)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  private Task getTaskById(Long id) {
    return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
  }
}
