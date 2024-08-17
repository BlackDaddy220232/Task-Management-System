package com.application.taskmanagementsystem.model.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
  @Column private String role;

  @OneToMany(
          mappedBy = "user",
          fetch=FetchType.EAGER,
          cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
  )
  private Set<Task> taskSet= new HashSet<>();
}
