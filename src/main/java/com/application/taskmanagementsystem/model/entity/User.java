package com.application.taskmanagementsystem.model.entity;

import com.application.taskmanagementsystem.model.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
  @JsonIgnore
  @Column private String password;
  @Enumerated(EnumType.STRING)
  @Column private Role role;

  @OneToMany(
          mappedBy = "employer",
          fetch = FetchType.EAGER,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
  )
  @JsonIgnore
  private Set<Task> taskSet= new HashSet<>();
}
