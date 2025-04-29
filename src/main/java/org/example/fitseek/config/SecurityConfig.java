package org.example.fitseek.config;

import org.example.fitseek.config.jwt.AuthEntryPoint;
import org.example.fitseek.config.jwt.AuthTokenFilter;
import org.example.fitseek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * <h4>Info</h4>
 * Configuration class for definition rules of authorization and authentication.
 * Enable web-security and work with HTTP-requests with JWT.
 *
 * @see UserService
 * @see AuthEntryPoint
 * @see PasswordEncoder
 * @see CorsConfigurationSource
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    AuthEntryPoint authEntryPoint;

    @Bean
    public AuthTokenFilter authTokenFilter() {return new AuthTokenFilter();}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    /**
     * Defines rules for correct CORS work and correct combination with front-end part
     * @return CORS configuration object for all endpoints
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Connects and configures the security for the application.
     * <p>
     * This method:
     * <ul>
     *     <li>Enables CORS using the configuration defined in {@link SecurityConfig#corsConfigurationSource()}.</li>
     *     <li>Disables CSRF protection (not required for stateless REST APIs).</li>
     *     <li>Allows unauthenticated access to the <code>/register</code> and <code>/login</code> endpoints.</li>
     *     <li>Requires authentication for all other endpoints.</li>
     *     <li>Disables session creation by setting session policy to {@link SessionCreationPolicy#STATELESS} (no cookies).</li>
     *     <li>Registers a custom {@link org.springframework.security.web.AuthenticationEntryPoint} for handling unauthorized access attempts.</li>
     *     <li>Registers a custom JWT authentication filter before {@link UsernamePasswordAuthenticationFilter}.</li>
     * </ul>
     * After configuration, the resulting {@link SecurityFilterChain} is used by Spring Security to handle HTTP request security.
     *
     * @param http the {@link HttpSecurity} object used to configure the security settings
     * @return the configured {@link SecurityFilterChain} bean
     * @throws Exception in case of any configuration error
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register","/login").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint)
                )
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
