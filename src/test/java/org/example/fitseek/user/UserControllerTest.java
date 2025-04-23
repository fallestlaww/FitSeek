package org.example.fitseek.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.controller.UserController;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.model.Gender;
import org.example.fitseek.model.User;
import org.example.fitseek.repository.UserRepository;
import org.example.fitseek.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(UserControllerTest.MockConfig.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token = "valid.jwt.token";
    private String email = "test@example.com";
    private Gender gender;
    private User user;
    private UserRequest userRequest;
    private User updatedUser;
    @Autowired
    private UserRepository userRepository;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserServiceImpl userService() {
            return Mockito.mock(UserServiceImpl.class);
        }

        @Bean
        public JwtUtils jwtUtils() {
            return Mockito.mock(JwtUtils.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @BeforeEach
    public void setup() {
        gender = new Gender();
        gender.setId(1L);
        gender.setName("Male");

        user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password"));
        user.setAge(25);
        user.setWeight(75.0);
        user.setGender(gender);

        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setName("Male");

        userRequest = new UserRequest();
        userRequest.setName("test_update");
        userRequest.setEmail(email);
        userRequest.setPassword("new_password");
        userRequest.setAge(30);
        userRequest.setWeight(80.0);
        userRequest.setGender(genderRequest);

        updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName(userRequest.getName());
        updatedUser.setEmail(email);
        updatedUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        updatedUser.setAge(userRequest.getAge());
        updatedUser.setWeight(userRequest.getWeight());
        updatedUser.setGender(gender);
    }

    @Test
    public void testReadUserSuccessful() throws Exception {
        when(jwtUtils.getEmailFromToken(token)).thenReturn(email);
        when(userService.readUser(email)).thenReturn(user);

        mvc.perform(get("/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testReadUserInvalidToken() throws Exception {
        String invalidToken = "invalid.token";
        when(jwtUtils.getEmailFromToken(invalidToken)).thenReturn(null);

        mvc.perform(get("/user")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateUserSuccessful() throws Exception {
        when(jwtUtils.getEmailFromToken(token)).thenReturn(email);
        when(userService.readUser(email)).thenReturn(user);
        when(userService.updateUser(any(UserRequest.class))).thenReturn(updatedUser);

        mvc.perform(put("/user/update")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(userRequest.getName()))
                .andExpect(jsonPath("$.age").value(userRequest.getAge()))
                .andExpect(jsonPath("$.weight").value(userRequest.getWeight()))
                .andExpect(jsonPath("$.gender").value(userRequest.getGender().getName()))
                .andDo(result -> {
                    Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.email"), user.getEmail());
                    Assertions.assertNotEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.name"), user.getName());
                    Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.name"), userRequest.getName());
                    Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.name"), userRequest.getName());
                });
    }

    @Test
    public void testUpdateUserInvalidToken() throws Exception {
        String invalidToken = "invalid.token";
        when(jwtUtils.getEmailFromToken(invalidToken)).thenReturn(null);
        mvc.perform(put("/user/update")
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUserSuccessful() throws Exception {
        when(jwtUtils.getEmailFromToken(token)).thenReturn(email);
        when(userService.readUser(email)).thenReturn(user);
        mvc.perform(delete("/user/delete")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserInvalidToken() throws Exception {
        String invalidToken = "invalid.token";
        when(jwtUtils.getEmailFromToken(invalidToken)).thenReturn(null);
        when(userService.readUser(null)).thenThrow(JwtException.class);
        mvc.perform(delete("/user/delete")
                .header("Authorization", "Bearer " + invalidToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testReadUserById() throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        mvc.perform(get("/user/read/{id}", user.getId())
                .header("Authorization", "Bearer" + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testReadUserByWrongId() throws Exception {
        Long wrongId = 15L;
        when(userRepository.findById(wrongId)).thenReturn(Optional.empty());
        when(userService.readUserForAdmin(wrongId)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/user/read/{id}", wrongId)
                .header("Authorization", "Bearer" + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}