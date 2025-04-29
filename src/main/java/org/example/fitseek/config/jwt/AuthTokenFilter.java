package org.example.fitseek.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.dto.request.LoginRequest;
import org.example.fitseek.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <h4>Info</h4>
 * Realization of JWT filter, which used only once for every HTTP-request (by using {@link OncePerRequestFilter}).
 * Main task of this filter is to validate token and to load user in {@link org.springframework.security.core.context.SecurityContext} of security
 *
 * @see UserServiceImpl
 * @see JwtUtils
 * @see OncePerRequestFilter
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Main method, describing a work of this JWT filter:
     * <li>First of all, filter checks if given JWT has a header "Bearer "</li>
     * <li>After that, filter validate token by {@link JwtUtils#validateToken(String)}</li>
     * <li>Set user in {@link org.springframework.security.core.context.SecurityContext}</li>
     * <li>Finally, send request to the controller layer</li>
     * ATTENTION!
     * <li>Even if token wasn't validated, filter send request to the controller too.
     * It work in this way because we have an endpoint for which we don't need JWT, like {@link org.example.fitseek.controller.AuthenticationController#register(UserRequest)}
     * or {@link org.example.fitseek.controller.AuthenticationController#authenticateUser(LoginRequest)}</li>
     * @param request HTTP-request with which this filter work
     * @param response HTTP-response with which this filter work
     * @param filterChain object which allowed HTTP-request to go through sequence of filters
     * @throws ServletException throws only in case if something in filter goes wrong
     * @throws IOException throws in case of JSON serialization/deserialization or network errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getAccessToken(request);
        if(!jwtUtils.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    /**
     * Checks if token, given in header of request, has a "Bearer " prefix
     * @param request from which we get JWT
     * @return boolean result
     */
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer ")) return false;
        return true;
    }

    /**
     * Removes prefix "Bearer " from JWT
     * @param request from which we get JWT
     * @return JWT without prefix
     */
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.substring(7);
    }

    /**
     * Extracts {@link UserDetails} object from token
     * @param token given token with user data
     * @return {@link UserDetails} object with data got from token
     */
    private UserDetails getUserDetails(String token) {
        String[] jwtSubject = jwtUtils.getSubject(token).split(",");
        return userService.loadUserByUsername(jwtSubject[0]);
    }

    /**
     * Sets user in {@link org.springframework.security.core.context.SecurityContext}
     * @param token given token with user data
     * @param request given request with user data
     */
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
        );
        // creating an object with detailed information about request and setting that to authentication object
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
