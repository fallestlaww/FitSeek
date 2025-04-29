package org.example.fitseek.repository;

import org.example.fitseek.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <h4>Info</h4>
 * Repository for {@link TrainingType} entity, provides CRUD operations and custom query methods for working with {@code training_type} table in database
 */
@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    /**
     * Returns {@link TrainingType} objects founded in database by training type name
     * @param name given training type name for search
     * @return {@link TrainingType} objects
     */
    TrainingType findByName(String name);
}
