package org.example.fitseek.repository;

import org.example.fitseek.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {
    Gender findByName(String name);
}
