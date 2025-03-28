package org.example.fitseek.controller;

import io.jsonwebtoken.JwtException;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.dto.response.UserResponse;
import org.example.fitseek.model.User;
import org.example.fitseek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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
            return ResponseEntity.ok().body(new UserResponse(user));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/update/")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest user) {
        if(user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested user is null");
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok().body(updatedUser);
    }
}
