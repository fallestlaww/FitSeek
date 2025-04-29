package org.example.fitseek.config.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <h4>Info</h4>
 * Utility class for JWT operations, including generation, validation, and data extraction.
 * <h4>Fields</h4>
 * {@link JwtUtils#jwtSecret} - secret key, given in application.properties, which used to signing and checking tokens.
 * {@link JwtUtils#jwtExpirationMs} - time of life of token, given in application.properties.
 * {@link JwtUtils#key} - key for signing and validating token, which initializes by HMAC SHA-256 algorithms
 *
 * @see Key
 */
@Component
public class JwtUtils {
    @Value("${jwttoken.app.jwtSecret}")
    private String jwtSecret;
    @Value("${jwttoken.app.jwtExpirationMs}")
    private long jwtExpirationMs;
    private Key key;

    /**
     * Initializes value for {@link #key} from {@link #jwtSecret} by {@link Keys#hmacShaKeyFor(byte[])}
     */
    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalArgumentException("JWT Secret is not provided!");
        }
        // generate HMAC SHA-256 key by bytes in jwtSecret
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Main method in all JWT filter logic, corresponding for generating token, in this logic JWT is generating for username
     * @param username subject for which JWT is generating
     * @param roles user roles
     * @return JWT
     */
    public String generateTokenFromUserName(String username, Collection<? extends GrantedAuthority> roles) {
        List<String> roleNames = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(username)
                .setIssuer("jwttoken")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .claim("roles", roleNames)
                .compact();
    }

    /**
     * Validates if generated token is generated correctly, if not - throwing an error in accordance to the problem
     * @param token given JWT for validating
     * @return boolean result
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {} " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}" + e.getMessage());
        }
        return false;
    }
    /**
     * Extracts "subject" claim from token
     * @param token - given JWT
     * @return token's subject
     */
    public String getSubject(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Extracts all claims from token
     * @param token given JWT
     * @return token's claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts email from token
     * @param token given JWT
     * @return user email
     */
    public String getEmailFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }
}
