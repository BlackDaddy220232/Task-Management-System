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
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private JwtCore jwtCore;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    private User user;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .role(Role.EMPLOYER)
                .build();

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .employer(user)
                .build();

        comment = Comment.builder()
                .id(1L)
                .content("Test Comment")
                .author("testuser")
                .task(task)
                .build();
    }
    @Test
    void getTaskByUserId_PriorityAndStatusNotNull_ReturnsTasksByPriorityAndStatus() {
        User user = new User();
        user.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findTasksByUserAndPriorityAndStatus(user, Priority.HIGH, Status.TO_DO, pageable)).thenReturn(tasks);

        Page<Task> result = userService.getTaskByUserId(1L, 0, 10, Status.TO_DO, Priority.HIGH);

        assertEquals(tasks, result);
        verify(taskRepository).findTasksByUserAndPriorityAndStatus(user, Priority.HIGH, Status.TO_DO, pageable);
    }

    @Test
    void getTaskByUserId_PriorityNotNull_StatusNull_ReturnsTasksByPriority() {
        User user = new User();
        user.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findTasksByUserAndPriority(user, Priority.MEDIUM, pageable)).thenReturn(tasks);

        Page<Task> result = userService.getTaskByUserId(1L, 0, 10, null, Priority.MEDIUM);

        assertEquals(tasks, result);
        verify(taskRepository).findTasksByUserAndPriority(user, Priority.MEDIUM, pageable);
    }

    @Test
    void getTaskByUserId_PriorityNull_StatusNotNull_ReturnsTasksByStatus() {
        User user = new User();
        user.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findTasksByUserAndStatus(user, Status.IN_PROGRESS, pageable)).thenReturn(tasks);

        Page<Task> result = userService.getTaskByUserId(1L, 0, 10, Status.IN_PROGRESS, null);

        assertEquals(tasks, result);
        verify(taskRepository).findTasksByUserAndStatus(user, Status.IN_PROGRESS, pageable);
    }

    @Test
    void getTaskByUserId_PriorityNull_StatusNull_ReturnsAllTasks() {
        User user = new User();
        user.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findTasksByUser(user, pageable)).thenReturn(tasks);

        Page<Task> result = userService.getTaskByUserId(1L, 0, 10, null, null);

        assertEquals(tasks, result);
        verify(taskRepository).findTasksByUser(user, pageable);
    }
    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        var userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistentuser"));
    }

    @Test
    void createTask_TaskCreated_ReturnsSuccessMessage() {
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.existsByTitleAndEmployer("Test Task", user)).thenReturn(false);

        TaskRequest taskRequest = TaskRequest.builder()
                .title("Test Task")
                .definition("Definition")
                .priority(Priority.MEDIUM)
                .status(Status.TO_DO)
                .build();

        String result = userService.createTask(taskRequest, request);

        assertEquals("Task \"Test Task\" has been created successfully", result);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_TaskAlreadyExists_ThrowsException() {
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.existsByTitleAndEmployer("Test Task", user)).thenReturn(true);

        TaskRequest taskRequest = TaskRequest.builder()
                .title("Test Task")
                .definition("Definition")
                .priority(Priority.MEDIUM)
                .status(Status.TO_DO)
                .build();

        assertThrows(TaskTakenException.class, () -> userService.createTask(taskRequest, request));
    }

    @Test
    void appointEmployee_ValidTaskAndEmployee_ReturnsSuccessMessage() {
        User employee = User.builder().id(2L).username("employee").role(Role.EMPLOYEE).build();
        when(taskRepository.findTaskByIdAndEmployer(1L, user)).thenReturn(Optional.of(task));
        when(userRepository.findUserByIdAndRole(2L, Role.EMPLOYEE)).thenReturn(Optional.of(employee));
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        String result = userService.appointEmployee(1L, 2L, request);

        assertEquals("Employee successfully has been appointed!", result);
        verify(taskRepository).save(task);
    }

    @Test
    void getAllEmployees_ReturnsEmployeeList() {
        when(userRepository.getAllByRole(Role.EMPLOYEE)).thenReturn(List.of(user));

        List<User> employees = userService.getAllEmployees();

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("testuser", employees.get(0).getUsername());
    }

    @Test
    void getUserTasks_ReturnsTaskList() {
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(taskRepository.findTasksByUser(user)).thenReturn(List.of(task));

        List<Task> tasks = userService.getUserTasks(request);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    void getTaskByUserId_ReturnsTaskPage() {
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(task));
        when(taskRepository.findTasksByUser(user, pageable)).thenReturn(taskPage);

        Page<Task> result = userService.getTaskByUserId(1L, 0, 10, null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void createComment_ValidTaskAndComment_ReturnsSuccessMessage() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Test Comment");

        String result = userService.createComment(1L, commentDTO, request);

        assertEquals("Comment has been published!", result);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void changeStatus_ValidTaskAndStatus_ReturnsSuccessMessage() {
        when(taskRepository.findTaskByUserAndId(user, 1L)).thenReturn(Optional.of(task));
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus(Status.IN_PROGRESS);

        String result = userService.changeStatus(1L, statusDTO, request);

        assertEquals("Status successfully changed to \"IN_PROGRESS\"", result);
        verify(taskRepository).save(task);
    }

    @Test
    void editTask_ValidTaskAndUpdates_ReturnsSuccessMessage() {
        when(taskRepository.findTaskByUserAndId(user, 1L)).thenReturn(Optional.of(task));
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Updated Task");

        String result = userService.editTask(1L, updateTaskDTO, request);

        assertEquals("Task successfully changed", result);
        verify(taskRepository).save(task);
    }

    @Test
    void deleteTask_ValidTask_ReturnsSuccessMessage() {
        // Инициализация тестовых данных
        user.setTaskSet(new HashSet<>());
        user.getTaskSet().add(task);

        when(taskRepository.findTaskByUserAndId(user, 1L)).thenReturn(Optional.of(task));
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        // Вызов тестируемого метода
        String result = userService.deleteTask(1L, request);

        // Проверка результата
        assertEquals("Task successfully deleted", result);
        verify(taskRepository).delete(task);
    }
}
