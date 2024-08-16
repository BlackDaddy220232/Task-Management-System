package com.application.taskmanagementsystem.dao;

import java.util.Optional;

import com.application.taskmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByEmail(String username);

  Boolean existsUserByEmail(String username);
}
