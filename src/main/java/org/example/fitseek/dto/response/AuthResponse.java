package org.example.fitseek.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <h4>Info</h4>
 * Response DTO that holds the JWT token after successful authentication.
 * Used as a response body for login operations, allowing the client to receive and store the JWT token for future authenticated requests.
 */
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class AuthResponse {
    private String token;
}
