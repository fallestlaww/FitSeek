package org.example.fitseek.repository;

import org.example.fitseek.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <h4>Info</h4>
 * Repository for {@link Day} entity, provides CRUD operations and custom query methods for working with {@code days} table in database
 */
@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    /**
     * Returns {@link Day} object  founded in database by day name
     * @param dayName given day name for search
     * @return {@link Day} object
     */
    Day findByName(String dayName);
}
