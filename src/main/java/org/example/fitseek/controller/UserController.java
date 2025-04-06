package org.example.fitseek.controller;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.dto.response.UserResponse;
import org.example.fitseek.model.User;
import org.example.fitseek.service.UserService;
import org.example.fitseek.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

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

    @PutMapping("/update/")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest user) {
        if(user == null) {
            log.warn("Requested user is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested user is null");
        }
        User updatedUser = userService.updateUser(user);
        log.info("User updated: {}", updatedUser.getEmail());
        return ResponseEntity.ok().body(updatedUser);
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
}
