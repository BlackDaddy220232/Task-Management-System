package com.application.taskmanagementsystem.controller;

import com.application.taskmanagementsystem.model.dto.TaskRequest;
import com.application.taskmanagementsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> getUserTasks(HttpServletRequest request){
    return ResponseEntity.ok(userService.getUserTasks(request));
  }
}
