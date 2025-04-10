package org.example.fitseek.repository;

import org.example.fitseek.model.Muscle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuscleRepository extends JpaRepository<Muscle, Long> {
    Muscle findByName(String muscleName);
}
