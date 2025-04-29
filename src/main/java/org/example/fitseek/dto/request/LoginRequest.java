package org.example.fitseek.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <h4>Info</h4>
 * Request DTO that sends a pair username+password, given by user, for authenticating by {@link org.example.fitseek.controller.AuthenticationController#authenticateUser(LoginRequest)}
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
