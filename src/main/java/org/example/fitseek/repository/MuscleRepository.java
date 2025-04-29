package org.example.fitseek.repository;

import org.example.fitseek.model.Muscle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <h4>Info</h4>
 * Repository for {@link Muscle} entity, provides CRUD operations and custom query methods for working with {@code muscles} table in database
 */
@Repository
public interface MuscleRepository extends JpaRepository<Muscle, Long> {
    /**
     * Returns {@link Muscle} object  founded in database by muscle name
     * @param muscleName given muscle name for search
     * @return {@link Muscle} object
     */
    Muscle findByName(String muscleName);
}
