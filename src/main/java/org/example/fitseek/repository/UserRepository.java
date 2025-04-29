package org.example.fitseek.repository;

import org.example.fitseek.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <h4>Info</h4>
 * Repository for {@link User} entity, provides CRUD operations and custom query methods for working with {@code users} table in database
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Returns {@link User} objects founded in database by user email
     * @param email given user email for search
     * @return {@link User} objects
     */
    User findByEmail(String email);
}
