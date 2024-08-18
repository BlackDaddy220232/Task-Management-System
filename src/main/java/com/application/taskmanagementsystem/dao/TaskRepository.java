package com.application.taskmanagementsystem.dao;

import com.application.taskmanagementsystem.model.entity.Task;
import com.application.taskmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByIdAndEmployer(Long id, User employer);

    boolean existsByTitleAndEmployer(String title, User employer);
    List<Task> findAll();
    @Query("SELECT t FROM Task t WHERE t.employer = :user OR t.employee = :user")
    List<Task> findTasksByUser(User user);

    @Query("SELECT t FROM Task t WHERE (t.employer = :user OR t.employee = :user) AND t.id = :id")
    Optional<Task> findTaskByUserAndId(User user, Long id);

    Optional<Task> getTaskById(Long id);
}
