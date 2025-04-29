package org.example.fitseek.repository;

import org.example.fitseek.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <h4>Info</h4>
 * Repository for {@link Role} entity, provides CRUD operations and custom query methods for working with {@code roles} table in database
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Returns {@link Role} objects founded in database by role name
     * @param name given role name for search
     * @return {@link Role} objects
     */
    Role findByName(String name);
}
