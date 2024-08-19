package com.application.taskmanagementsystem.controller;

import com.application.taskmanagementsystem.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/all")
  public ResponseEntity<Object> getAllTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }
}
