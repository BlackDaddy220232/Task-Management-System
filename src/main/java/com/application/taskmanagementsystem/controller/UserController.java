package com.application.taskmanagementsystem.controller;

import com.application.taskmanagementsystem.model.dto.CommentDTO;
import com.application.taskmanagementsystem.model.dto.StatusDTO;
import com.application.taskmanagementsystem.model.dto.TaskRequest;
import com.application.taskmanagementsystem.model.dto.UpdateTaskDTO;
import com.application.taskmanagementsystem.model.entity.Task;
import com.application.taskmanagementsystem.model.enumeration.Priority;
import com.application.taskmanagementsystem.model.enumeration.Status;
import com.application.taskmanagementsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/task")
  public ResponseEntity<Object> createTask(
      @Valid @RequestBody TaskRequest taskRequest, HttpServletRequest request) {
    return ResponseEntity.ok(userService.createTask(taskRequest, request));
  }

  @PostMapping("/task/employee")
  public ResponseEntity<Object> appointEmployee(
      @RequestParam Long taskId, Long employeeId, HttpServletRequest request) {
    return ResponseEntity.ok(userService.appointEmployee(taskId, employeeId, request));
  }

  @GetMapping("/allEmployees")
  public ResponseEntity<Object> getAllEmployees() {
    return ResponseEntity.ok(userService.getAllEmployees());
  }

  @GetMapping("/tasks")
  public ResponseEntity<Object> getUserTasks(HttpServletRequest request) {
    return ResponseEntity.ok(userService.getUserTasks(request));
  }

  @GetMapping("/{id}/tasks")
  public ResponseEntity<Page<Task>> getTasksByUserID(
      @RequestParam("offset") @Min(0) Integer offset,
      @RequestParam("limit") @Min(1) Integer limit,
      @RequestParam (required = false)Status status,
      @RequestParam (required = false)Priority priority,
      @PathVariable Long id) {
    return ResponseEntity.ok(userService.getTaskByUserId(id,offset,limit,status,priority));
  }

  @PostMapping("/task/{id}/comment")
  public ResponseEntity<Object> createComment(
      @PathVariable Long id,
      @Valid @RequestBody CommentDTO commentDTO,
      HttpServletRequest request) {
    return ResponseEntity.ok(userService.createComment(id, commentDTO, request));
  }

  @PatchMapping("/task/{id}/status")
  public ResponseEntity<Object> changeStatus(
      @PathVariable Long id, @Valid @RequestBody StatusDTO status, HttpServletRequest request) {
    return ResponseEntity.ok(userService.changeStatus(id, status, request));
  }

  @PatchMapping("/task/{id}")
  public ResponseEntity<Object> editTask(
      @PathVariable Long id, @RequestBody UpdateTaskDTO updateTaskDTO, HttpServletRequest request) {
    return ResponseEntity.ok(userService.editTask(id, updateTaskDTO, request));
  }

  @DeleteMapping("/task/{id}")
  public ResponseEntity<Object> deleteTask(@PathVariable Long id, HttpServletRequest request) {
    return ResponseEntity.ok(userService.deleteTask(id, request));
  }
}
