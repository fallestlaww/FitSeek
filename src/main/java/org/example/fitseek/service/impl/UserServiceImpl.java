package org.example.fitseek.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.exception.exceptions.EntityAlreadyExistsException;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.exception.exceptions.InvalidRequestException;
import org.example.fitseek.model.Gender;
import org.example.fitseek.model.User;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.repository.RoleRepository;
import org.example.fitseek.repository.UserRepository;
import org.example.fitseek.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of service layer {@link UserService} for {@link User} entity.
 * Provides logic for managing {@link UserService},
 * including role-based access control for sensitive methods, namely {@link #readUserForAdmin(Long)}
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenderRepository genderRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, GenderRepository genderRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.genderRepository = genderRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createUser(UserRequest userRequest) {
            log.debug("Creating user: {}", userRequest.getEmail());
            User existingUser = userRepository.findByEmail(userRequest.getEmail());
            if (existingUser != null) {
                log.error("User with email {} already exists", userRequest.getEmail());
                throw new EntityAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists");
            }

            if (userRequest.getName() == null || userRequest.getEmail() == null
                    || userRequest.getPassword() == null || userRequest.getAge() == 0 || userRequest.getWeight() == 0) {
                log.error("Invalid user request");
                throw new InvalidRequestException("Invalid user request");
            }
            //find in database gender by given gender name in request
            Gender gender = Optional.ofNullable(genderRepository.findByName(userRequest.getGender().getName()))
                    .orElseThrow(() -> new InvalidEntityException("Invalid gender: " + userRequest.getGender().getName()));
            User newUser = new User();
            newUser.setName(userRequest.getName());
            newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            newUser.setEmail(userRequest.getEmail());
            newUser.setAge(userRequest.getAge());
            newUser.setGender(gender);
            newUser.setWeight(userRequest.getWeight());
            newUser.setRole(roleRepository.findByName("USER"));
            log.debug("Saving user: {}", newUser);

            return userRepository.save(newUser);
    }
    @Override
    public User readUser(String email) {
        log.info("Reading user: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User with email {} not found", email);
            throw new InvalidEntityException("User with email " + email + " does not exist");
        }
        return user;
    }

    @Override
    public User updateUser(UserRequest user) {
        log.debug("Updating user: {}", Objects.toString(user, "null"));
        if(user == null) {
            log.error("Requested user is null");
            throw new EntityNullException("Requested user is null");
        }
        // find in database user by user email got from request, without existing user code can't update anything
        User existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser == null) {
            log.error("User with email {} not found", user.getEmail());
            throw new EntityNotFoundException("User with email " + user.getEmail() + " does not exist");
        }
        log.debug("Updating existing user: {}", existingUser.getEmail());
        Optional.ofNullable(user.getName()).ifPresent(existingUser::setName);
        Optional.ofNullable(user.getPassword())
                .ifPresent(password -> existingUser.setPassword(passwordEncoder.encode(user.getPassword())));
        //find in database gender by given gender name in request
        Gender gender = Optional.ofNullable(user.getGender())
                .map(g -> genderRepository.findByName(g.getName()))
                .orElseThrow(() -> new InvalidEntityException("Invalid gender"));
        if(gender != null) {
            existingUser.setGender(gender);
            log.debug("Existing gender: {}", gender.getName());
        } else throw new EntityNullException("Gender is not found");
        if(user.getAge() > 0) existingUser.setAge(user.getAge());
        if(user.getWeight() > 0) existingUser.setWeight(user.getWeight());
        log.info("Existing user: {}", existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            log.error("User with id {} not found", id);
            throw new EntityNotFoundException("User not found");
        }
        User user = userOpt.get();
        log.debug("Deleting user: {}", user.getEmail());
        userRepository.deleteById(id);
        log.info("Deleted user: {}", user.getEmail());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public User readUserForAdmin(Long id) {
        log.debug("Reading user: {}", id);
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // find user in database
        User user = userRepository.findByEmail(username);
        if(user == null) {
            log.error("User with user email {} not found", username);
            throw new EntityNotFoundException("User with email " + username + " not found");
        }
        // group user roles into a list
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        log.info("User authorities: {}", authorities);
        // return object of user created specially for security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
