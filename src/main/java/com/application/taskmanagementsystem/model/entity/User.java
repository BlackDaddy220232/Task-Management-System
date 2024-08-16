package com.application.taskmanagementsystem.model.entity;

import jakarta.persistence.*;
import java.util.Collections;

import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String email;
  @Column private String password;


  private String role;
}
