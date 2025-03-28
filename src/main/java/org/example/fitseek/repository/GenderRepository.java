package org.example.fitseek.repository;

import org.example.fitseek.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Gender findByName(String name);
}
