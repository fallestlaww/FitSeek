package org.example.fitseek.repository;

import org.example.fitseek.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    Day findByName(String dayName);
}
