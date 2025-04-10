package org.example.fitseek.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.exception.exceptions.EntityAlreadyExistsException;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.exception.exceptions.InvalidRequestException;
import org.example.fitseek.model.AuthResponse;
import org.example.fitseek.model.LoginRequest;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthenticationController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) {
        log.debug("Registering user {}", userRequest.toString());
        userService.createUser(userRequest);
        log.info("User created: {}", userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("Authenticating user {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("User authenticated: {}", userDetails.getUsername());
        String jwtToken = jwtUtils.generateTokenFromUserName(userDetails.getUsername(), userDetails.getAuthorities());
        AuthResponse authResponse = new AuthResponse(jwtToken);
        return ResponseEntity.ok(authResponse);
    }
}
