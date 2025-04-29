package org.example.fitseek.service;

import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <h4>Info</h4>
 * Realization of service layer for {@link User} entity.
 * Provides CRUD operations for entity object based on user parameters
 */
public interface UserService extends UserDetailsService {
    /**
     * Creates object of {@link User} entity using object of request.
     * @param user object of {@link UserRequest} which include all needed information
     * @return created {@link User} object
     */
    User createUser(UserRequest user);
    /**
     * Returns object of {@link User} entity, found by given user email.
     * @param email given user email
     * @return object of {@link User}
     */
    User readUser(String email);
    /**
     * Updates the information provided in the request for the user found using the request also.
     * @param user object of {@link UserRequest} which include all needed information
     * @return updated {@link User} object
     */
    User updateUser(UserRequest user);
    /**
     * Deletes {@link User} entity found by user id.
     * @param id given {@link User} entity id.
     */
    void deleteUser(Long id);
    /**
     * Returns all information about object of {@link User} entity, found by given user id.
     * @param id given user id
     * @return object and all information about {@link User}
     */
    User readUserForAdmin(Long id);
}
