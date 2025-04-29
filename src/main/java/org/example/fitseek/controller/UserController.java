package org.example.fitseek.controller;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.dto.response.UserResponse;
import org.example.fitseek.model.User;
import org.example.fitseek.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**<h4>Info</h4>
 * REST-controller, responsible for processing CRUD-operations on {@link User} entity.
 * <h4>Fields</h4>
 * {@link #userService} object of {@link UserService} class, responsible for business logic to work with {@link User} entity.
 * {@link #jwtUtils} object of {@link JwtUtils} class, responsible for business logic to work with JWT.
 *
 * @see UserService
 * @see JwtUtils
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Gives out information about user, extracting that from the JWT, creating a so-called personal account
     * @param authHeader header of HTTP-request from where method takes header "Authorization"
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and user information in response
     */
    @GetMapping
    public ResponseEntity<?> readUser(@RequestHeader("Authorization") String authHeader) {
        try {
            //removing prefix "Bearer " from JWT
            String token = authHeader.substring(7);
            // extracting email from JWT
            String email = jwtUtils.getEmailFromToken(token);
            User user = userService.readUser(email);
            if (email == null) {
                throw new JwtException("Invalid email in JWT token");
            }
            log.info("User for reading: {}", user.getEmail());
            return ResponseEntity.ok().body(new UserResponse(user));
        } catch (JwtException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Takes information about user extracted from the JWT and claims information to update actual user entity.
     * @param authHeader header of HTTP-request from where method takes header "Authorization"
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and updated user information in response
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody UserRequest userRequest) {
        try {
            //removing prefix "Bearer " from JWT
            String token = authHeader.substring(7);
            // extracting email from JWT
            String email = jwtUtils.getEmailFromToken(token);
            if (email == null) {
                throw new JwtException("Invalid email in JWT token");
            }
            userRequest.setEmail(email);
            User updatedUser = userService.updateUser(userRequest);
            log.info("User updated: {}", updatedUser.getEmail());
            return ResponseEntity.ok().body(new UserResponse(updatedUser));
        } catch (JwtException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Takes information about user extracted from the JWT and deletes that user
     * @param authHeader header of HTTP-request from where method takes header "Authorization"
     * @return {@link ResponseEntity} with a status code according to the result of the process execution
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader) {
        try {
            //removing prefix "Bearer " from JWT
            String token = authHeader.substring(7);
            // extracting email from JWT
            String email = jwtUtils.getEmailFromToken(token);
            User user = userService.readUser(email);
            log.info("User for deleting: {}", user.getEmail());
            userService.deleteUser(user.getId());
            log.info("User deleted: {}", user.getEmail());
            return ResponseEntity.ok().body("Deleted successfully");
        } catch (JwtException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Takes id form URL, takes all information about user from database and give that information out, will work only if user has role "ADMIN"
     * @param id represents user id in database
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and full user information
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<?> readUserById(@PathVariable("id") long id) {
        User user = userService.readUserForAdmin(id);
        return ResponseEntity.ok().body(user);
    }

    /**
     * Takes id form URL and delete user by id from database, will work only if user has role "ADMIN"
     * @param id represents user id in database
     * @return {@link ResponseEntity} with a status code according to the result of the process execution
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") long id) {
        userService.deleteUser(id);
        log.info("User deleted: {}", id);
        return ResponseEntity.ok().body("Deleted successfully");
    }
}
