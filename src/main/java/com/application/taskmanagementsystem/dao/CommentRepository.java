package com.application.taskmanagementsystem.dao;

import com.application.taskmanagementsystem.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
