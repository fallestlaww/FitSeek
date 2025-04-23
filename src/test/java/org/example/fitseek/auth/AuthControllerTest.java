package org.example.fitseek.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.controller.AuthenticationController;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.exception.exceptions.EntityAlreadyExistsException;
import org.example.fitseek.exception.exceptions.InvalidRequestException;
import org.example.fitseek.model.Gender;
import org.example.fitseek.model.LoginRequest;
import org.example.fitseek.model.User;
import org.example.fitseek.service.impl.ExerciseServiceImpl;
import org.example.fitseek.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@Import(AuthControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ExerciseServiceImpl exerciseService;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Gender gender;
    private UserRequest userRequest;
    private GenderRequest genderRequest;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public JwtUtils jwtUtils() {
            return Mockito.mock(JwtUtils.class);
        }
        @Bean
        public AuthenticationManager authenticationManager() {
            return Mockito.mock(AuthenticationManager.class);
        }
        @Bean
        public UserServiceImpl userService() {
            return Mockito.mock(UserServiceImpl.class);
        }
        @Bean
        public ExerciseServiceImpl exerciseService() {
            return Mockito.mock(ExerciseServiceImpl.class);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        gender = new Gender();
        user = new User();

        gender.setName("Male");

        user.setId(1L);
        user.setGender(gender);
        user.setAge(25);
        user.setWeight(80.0);
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setName("Test");

        userRequest = new UserRequest();
        userRequest.setName("Test");
        userRequest.setAge(25);
        userRequest.setWeight(80.0);
        userRequest.setEmail("test@mail.com");
        userRequest.setPassword("password");
        userRequest.setGender(genderRequest);

        genderRequest = new GenderRequest();
        genderRequest.setName("Male");
    }

    @AfterEach
    public void reset() {
        Mockito.reset(userService, exerciseService, authenticationManager, jwtUtils);
    }
    @Test
    public void testRegisterSuccess() throws Exception {
        when(userService.createUser(userRequest)).thenReturn(user);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterFailureEntityExist() throws Exception {
        when(userService.createUser(any(UserRequest.class))).thenThrow(EntityAlreadyExistsException.class);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testLoginSuccessful() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("test");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(loginRequest.getUsername(), loginRequest.getPassword(), Collections.emptyList());

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(jwtUtils.generateTokenFromUserName(any(String.class), any())).thenReturn("token");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    public void testLoginFailureWrongLoginRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrong");
        loginRequest.setPassword("wrong");

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginFailureLoginRequestNull() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(null);
        loginRequest.setPassword(null);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(loginRequest);
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(InvalidRequestException.class);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
