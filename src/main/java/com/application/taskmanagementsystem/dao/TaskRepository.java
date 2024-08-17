package com.application.taskmanagementsystem.dao;

import com.application.taskmanagementsystem.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
