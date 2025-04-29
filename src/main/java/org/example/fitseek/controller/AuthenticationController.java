package org.example.fitseek.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.dto.response.AuthResponse;
import org.example.fitseek.dto.request.LoginRequest;
import org.example.fitseek.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *<h4>Info</h4>
 * REST-controller, responsible for user authentication, namely user login and registration
 * <p></p>
 *
 * <h4>Fields</h4>
 * {@link #userService} object of {@link UserService}, responsible for work with user data.
 * {@link #authenticationManager}, responsible for execution of the process of authentication.
 * {@link #jwtUtils}, responsible for work with JWT
 *
 * @see UserService
 * @see AuthenticationManager
 * @see JwtUtils
 */
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

    /**
     * Claims validated user data in object of {@link UserRequest} class and registers new user using {@link UserService#createUser(UserRequest)}.
     *
     * @param userRequest object with user data for registering
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and user data in response
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) {
        log.debug("Registering user {}", userRequest.toString());
        userService.createUser(userRequest);
        log.info("User created: {}", userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(userRequest);
    }

    /**
     * Claims validated user data in object of {@link LoginRequest} class and authenticates user with JWT issuance
     * @param loginRequest object with user data for authenticating
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and JWT in response
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("Authenticating user {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("User authenticated: {}", userDetails.getUsername());
        // generate JWT based on username and user roles
        String jwtToken = jwtUtils.generateTokenFromUserName(userDetails.getUsername(), userDetails.getAuthorities());
        // generate object of response class to send JWT after successful authenticating
        AuthResponse authResponse = new AuthResponse(jwtToken);
        return ResponseEntity.ok(authResponse);
    }
}
