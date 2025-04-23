package org.example.fitseek.user;

import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.exception.exceptions.EntityAlreadyExistsException;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.exception.exceptions.InvalidRequestException;
import org.example.fitseek.model.Gender;
import org.example.fitseek.model.Role;
import org.example.fitseek.model.User;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.repository.RoleRepository;
import org.example.fitseek.repository.UserRepository;
import org.example.fitseek.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private GenderRepository genderRepository;

    private User expectedUser;
    private UserRequest expectedUserRequest;
    private Gender gender;
    private Role role;

    @BeforeEach
    public void setUp() {
        gender = new Gender();
        role = new Role();
        gender.setName("Male");
        role.setName("USER");

        expectedUserRequest = new UserRequest();
        expectedUserRequest.setAge(19);
        expectedUserRequest.setName("test_user");
        expectedUserRequest.setPassword("password");
        expectedUserRequest.setEmail("test@example.com");
        expectedUserRequest.setWeight(81.0);
        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setName("Male");
        expectedUserRequest.setGender(genderRequest);

        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName(expectedUserRequest.getName());
        expectedUser.setEmail(expectedUserRequest.getEmail());
        expectedUser.setPassword(expectedUserRequest.getPassword());
        expectedUser.setAge(expectedUserRequest.getAge());
        expectedUser.setGender(gender);
        expectedUser.setWeight(expectedUserRequest.getWeight());
        expectedUser.setRole(role);
    }

    @Test
    public void userCreateSuccess() {
        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(null);
        when(genderRepository.findByName("Male")).thenReturn(gender);
        when(passwordEncoder.encode(expectedUserRequest.getPassword())).thenReturn(expectedUserRequest.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User actualUser = userService.createUser(expectedUserRequest);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void userCreateFailExistingEmail() {
        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(new User());

        assertThrows(EntityAlreadyExistsException.class, () -> userService.createUser(expectedUserRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    public void userCreateFailWrongGender() {
        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(null);
        when(genderRepository.findByName("Male")).thenReturn(null);

        assertThrows(InvalidEntityException.class, () -> userService.createUser(expectedUserRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    public void userCreateFailNull() {
        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(null);

        expectedUserRequest.setName(null);
        assertThrows(InvalidRequestException.class, () -> userService.createUser(expectedUserRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    public void userReadSuccess() {
        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(expectedUser);
        User actualUser = userService.readUser(expectedUserRequest.getEmail());
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void userReadFailWrongEmail() {
        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(null);
        assertThrows(InvalidEntityException.class, () -> userService.readUser(expectedUserRequest.getEmail()));
    }

    @Test
    public void userUpdateSuccess() {
        gender.setName("Female");
        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setName("Female");
        expectedUserRequest.setName("update_test");
        expectedUserRequest.setPassword("update_password");
        expectedUserRequest.setAge(25);
        expectedUserRequest.setWeight(91.0);
        expectedUser.setGender(gender);
        expectedUserRequest.setGender(genderRequest);

        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(expectedUser);
        when(genderRepository.findByName(genderRequest.getName())).thenReturn(gender);
        when(passwordEncoder.encode(expectedUserRequest.getPassword())).thenReturn(expectedUserRequest.getPassword());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User actualUser = userService.updateUser(expectedUserRequest);

        assertNotNull(actualUser);
        assertEquals(expectedUserRequest.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUserRequest.getPassword(), actualUser.getPassword());
        assertEquals(expectedUserRequest.getAge(), actualUser.getAge());
        assertEquals(expectedUserRequest.getGender().getName(), actualUser.getGender().getName());
        assertEquals(expectedUserRequest.getWeight(), actualUser.getWeight());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void userUpdateFailWrongEmail() {
        gender.setName("Female");
        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setName("Female");
        expectedUserRequest.setName("update_test");
        expectedUserRequest.setPassword("update_password");
        expectedUserRequest.setAge(25);
        expectedUserRequest.setWeight(91.0);
        expectedUser.setGender(gender);
        expectedUserRequest.setGender(genderRequest);
        expectedUserRequest.setEmail("wrong_email@mail.com");

        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(expectedUserRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    public void userUpdateFailNull() {
        gender = new Gender();
        gender.setName("Female");
        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setName(null);
        expectedUserRequest.setName("update_test");
        expectedUserRequest.setPassword("update_password");
        expectedUserRequest.setAge(25);
        expectedUserRequest.setWeight(91.0);
        expectedUser.setGender(gender);
        expectedUserRequest.setGender(genderRequest);

        when(userRepository.findByEmail(expectedUserRequest.getEmail())).thenReturn(expectedUser);
        when(genderRepository.findByName(genderRequest.getName())).thenReturn(null);
        assertThrows(InvalidEntityException.class, () -> userService.updateUser(expectedUserRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    public void deleteUserSuccess() {
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.ofNullable(expectedUser));

        userService.deleteUser(expectedUser.getId());
        verify(userRepository).deleteById(expectedUser.getId());
    }

    @Test
    public void deleteUserFailWrongId() {
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(expectedUser.getId()));
        verify(userRepository, never()).deleteById(expectedUser.getId());
    }

    @Test
    public void readUserForAdminSuccess() {
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.ofNullable(expectedUser));
        User actualUser = userService.readUserForAdmin(expectedUser.getId());

        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getAge(), actualUser.getAge());
        assertEquals(expectedUser.getGender().getName(), actualUser.getGender().getName());
        assertEquals(expectedUser.getWeight(), actualUser.getWeight());
        assertEquals(expectedUser.getRole().getName(), actualUser.getRole().getName());
        verify(userRepository).findById(expectedUser.getId());
    }

    @Test
    public void readUserForAdminFailWrongId() {
        when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.readUserForAdmin(expectedUser.getId()));
    }
}