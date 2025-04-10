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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<?> readUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtils.getEmailFromToken(token);
            User user = userService.readUser(email);
            log.info("User for reading: {}", user.getEmail());
            return ResponseEntity.ok().body(new UserResponse(user));
        } catch (JwtException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody UserRequest userRequest) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtils.getEmailFromToken(token);
            userRequest.setEmail(email);
            User updatedUser = userService.updateUser(userRequest);
            log.info("User updated: {}", updatedUser.getEmail());
            return ResponseEntity.ok().body(updatedUser);
        } catch (JwtException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
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

    @GetMapping("/read/{id}")
    public ResponseEntity<?> readUserById(@PathVariable("id") long id) {
        User user = userService.readUserForAdmin(id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") long id) {
        userService.deleteUserForAdmin(id);
        log.info("User deleted: {}", id);
        return ResponseEntity.ok().body("Deleted successfully");
    }
}
