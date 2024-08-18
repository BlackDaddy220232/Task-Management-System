package com.application.taskmanagementsystem.dao;

import com.application.taskmanagementsystem.model.entity.Task;
import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.model.enumeration.Priority;
import com.application.taskmanagementsystem.model.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByIdAndEmployer(Long id, User employer);

    boolean existsByTitleAndEmployer(String title, User employer);
    List<Task> findAll();
    @Query("SELECT t FROM Task t WHERE t.employer = :user OR t.employee = :user")
    Page<Task> findTasksByUser(User user, Pageable pageable);
    @Query("SELECT t FROM Task t WHERE t.employer = :user OR t.employee = :user")
    List<Task> findTasksByUser(User user);
    @Query("SELECT t FROM Task t WHERE (t.employer = :user OR t.employee = :user) AND t.priority = :priority")
    Page<Task> findTasksByUserAndPriority(User user, Priority priority, Pageable pageable);
    @Query("SELECT t FROM Task t WHERE (t.employer = :user OR t.employee = :user) AND t.status = :status")
    Page<Task> findTasksByUserAndStatus(User user, Status status, Pageable pageable);
    @Query("SELECT t FROM Task t WHERE (t.employer = :user OR t.employee = :user) AND t.priority = :priority AND t.status = :status")
    Page<Task> findTasksByUserAndPriorityAndStatus(User user, Priority priority, Status status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE (t.employer = :user OR t.employee = :user) AND t.id = :id")
    Optional<Task> findTaskByUserAndId(User user, Long id);

    Optional<Task> getTaskById(Long id);

    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
}
