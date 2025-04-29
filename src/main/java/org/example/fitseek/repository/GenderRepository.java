package org.example.fitseek.repository;

import org.example.fitseek.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <h4>Info</h4>
 * Repository for {@link Gender} entity, provides CRUD operations and custom query methods for working with {@code gender} table in database
 */
@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {
    /**
     * Returns {@link Gender} object  founded in database by gender name
     * @param name given gender name for search
     * @return {@link Gender} object
     */
    Gender findByName(String name);
}
