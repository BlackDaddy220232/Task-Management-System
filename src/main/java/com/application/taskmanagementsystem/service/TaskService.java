package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.TaskRepository;
import com.application.taskmanagementsystem.model.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }
}
