package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.CommentRepository;
import com.application.taskmanagementsystem.dao.TaskRepository;
import com.application.taskmanagementsystem.dao.UserRepository;
import com.application.taskmanagementsystem.exception.TaskNotFoundException;
import com.application.taskmanagementsystem.exception.TaskTakenException;
import com.application.taskmanagementsystem.exception.UserNotFoundException;
import com.application.taskmanagementsystem.model.dto.CommentDTO;
import com.application.taskmanagementsystem.model.dto.TaskRequest;
import com.application.taskmanagementsystem.model.entity.Comment;
import com.application.taskmanagementsystem.model.entity.Task;
import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.model.enumeration.Role;
import com.application.taskmanagementsystem.security.JwtCore;
import com.application.taskmanagementsystem.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "dataUser")
public class UserService implements UserDetailsService {
  private JwtCore jwtCore;
  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final CommentRepository commentRepository;
  private static final String USER_NOT_FOUND_MESSAGE = "User with name \"%s\" already exists";
  private static final String TASK_CREATED = "Task \"%s\"  has been created successfully";
  private static final String TASK_NOT_FOUND = "Task with this id has not found!";
  private static final String USER_NOT_FOUND = "User with this id has not found!";
  private static final String TASK_ALREADY_EXISTS = "Task \"%s\" already exists for this user";
  private static final String COMMENT_PUBLISHED = "Comment has been published!";
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
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    return UserDetailsImpl.build(user);
  }

  public String createTask(TaskRequest taskRequest, HttpServletRequest request) {
    User user =
        userRepository
            .findUserByUsername(jwtCore.getNameFromJwt(request))
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    if (taskRepository.existsByTitleAndEmployer(taskRequest.getTitle(), user)) {
      throw new TaskTakenException(String.format(TASK_ALREADY_EXISTS, taskRequest.getTitle()));
    }

    Task task =
        Task.builder()
            .title(taskRequest.getTitle())
            .description(taskRequest.getDefinition())
            .priority(taskRequest.getPriority())
            .employer(user)
            .build();

    taskRepository.save(task);
    return String.format(TASK_CREATED, task.getTitle());
  }

  public String appointEmployee(Long taskId, Long employeeId, HttpServletRequest request) {
    Task task = getTaskByIdAndEmployer(taskId, request);
    User employee = getUserByIdAndRole(employeeId, Role.EMPLOYEE);
    task.setEmployee(employee);
    taskRepository.save(task);
    return String.format(ACCOMPLISHED_APPOINTMENT);
  }

  public List<User> getAllEmployees() {
    return userRepository.getAllByRole(Role.EMPLOYEE);
  }

  public List<Task> getUserTasks(HttpServletRequest request) {
    return taskRepository.findTasksByUser(getUserByUsername(jwtCore.getNameFromJwt(request)));
  }

  public List<Task> getTasksByUserId(Long id) {
    return taskRepository.findTasksByUser(
        userRepository
            .findUserById(id)
            .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND))));
  }

  public String createComment(Long taskId, CommentDTO commentDTO, HttpServletRequest request) {
    Comment comment =
        Comment.builder()
            .time(LocalDateTime.now())
            .content(commentDTO.getContent())
            .author(jwtCore.getNameFromJwt(request))
            .task(getTaskById(taskId))
            .build();
    commentRepository.save(comment);
    return String.format(COMMENT_PUBLISHED);
  }

  private Task getTaskByIdAndEmployer(Long taskId, HttpServletRequest request) {
    User employer = getUserByUsername(jwtCore.getNameFromJwt(request));
    return taskRepository
        .findTaskByIdAndEmployer(taskId, employer)
        .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
  }

  private User getUserByIdAndRole(Long userId, Role role) {
    return userRepository
        .findUserByIdAndRole(userId, role)
        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
  }

  private User getUserByUsername(String username) {
    return userRepository
        .findUserByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

  private Task getTaskById(Long id) {
    return taskRepository
        .findById(id)
        .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND)));
  }
}
