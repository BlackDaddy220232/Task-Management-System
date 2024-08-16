package com.application.taskmanagementsystem.model.entity;

import jakarta.persistence.*;

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
  @Column private String username;
  @Column private String email;
  @Column private String password;


  private String role;
}
