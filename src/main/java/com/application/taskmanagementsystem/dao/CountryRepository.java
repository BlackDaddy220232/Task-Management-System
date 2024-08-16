package com.application.taskmanagementsystem.dao;

import java.util.Optional;

import com.application.taskmanagementsystem.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
  Optional<Country> findCountryByCountryName(String country);
}
