package com.application.taskmanagementsystem.dao;

import com.application.taskmanagementsystem.model.entity.Task;
import com.application.taskmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByIdAndEmployer(Long id, User employer);

    boolean existsByTitleAndEmployer(String title, User employer);
}
