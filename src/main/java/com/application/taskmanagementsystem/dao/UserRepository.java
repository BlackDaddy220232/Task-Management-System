package com.application.taskmanagementsystem.dao;

import java.util.List;
import java.util.Optional;

import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.model.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByUsername(String username);

  Boolean existsUserByEmail(String email);

  Boolean existsUserByUsername(String username);

  Optional<User> findUserByIdAndRole(Long id, Role role);

  List<User> getAllByRole(Role role);

  Optional<User> findUserById(Long id);
}
