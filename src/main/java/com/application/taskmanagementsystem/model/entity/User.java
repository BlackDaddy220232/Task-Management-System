package com.application.taskmanagementsystem.model.entity;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
  @Column private String password;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "country_id")
  private Country country;

  private String role;

  @PreRemove
  public void removeUser() {
    country.getUsers().removeAll(Collections.singleton(this));
  }

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(
      name = "saved_city_mapping",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "city_id"))
  private Set<City> savedCities = new HashSet<>();
}
