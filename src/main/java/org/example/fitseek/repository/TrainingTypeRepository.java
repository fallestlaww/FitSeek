package org.example.fitseek.repository;

import org.example.fitseek.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
    TrainingType findByName(String name);
}
