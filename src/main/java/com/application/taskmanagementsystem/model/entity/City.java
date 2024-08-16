package com.application.taskmanagementsystem.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий город.
 */
@Entity
@Table(name = "cities")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String cityName;

  @JsonIgnore
  @ManyToMany(mappedBy = "savedCities")
  private Set<User> savedUsers = new HashSet<>();
}