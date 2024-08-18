package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.TaskRepository;
import com.application.taskmanagementsystem.model.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAllTasks_ReturnsAllTasks() {
        // Создаем тестовые данные
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> expectedTasks = Arrays.asList(task1, task2);

        // Настраиваем mock для TaskRepository
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // Вызываем тестируемый метод
        List<Task> actualTasks = taskService.getAllTasks();

        // Проверяем результаты
        assertEquals(expectedTasks, actualTasks);
    }
}

