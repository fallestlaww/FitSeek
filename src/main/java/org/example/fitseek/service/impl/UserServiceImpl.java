package org.example.fitseek.service.impl;

import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.model.Gender;
import org.example.fitseek.model.User;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.repository.RoleRepository;
import org.example.fitseek.repository.UserRepository;
import org.example.fitseek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public User createUser(UserRequest userRequest) {
        User existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser != null) throw new IllegalArgumentException("User with email " + userRequest.getEmail() + " already exists");

        if(userRequest.getName() == null || userRequest.getEmail() == null
                || userRequest.getPassword() == null || userRequest.getAge() == 0 || userRequest.getHeight() == 0
                || userRequest.getWeight() == 0) throw new IllegalArgumentException("Invalid user request");

        Gender gender = genderRepository.findByName(userRequest.getGender().getName());
        if (gender == null) throw new IllegalArgumentException("Invalid gender: " + userRequest.getGender());

        User newUser = new User();
        newUser.setName(userRequest.getName());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setEmail(userRequest.getEmail());
        newUser.setAge(userRequest.getAge());
        newUser.setGender(gender);
        newUser.setHeight(userRequest.getHeight());
        newUser.setWeight(userRequest.getWeight());
        newUser.setRole(roleRepository.findByName("USER"));

        return userRepository.save(newUser);
    }
    @Override
    public User readUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new IllegalArgumentException("User with email " + email + " does not exist");
        return user;
        //todo custom exceptions
    }

    @Override
    public User updateUser(UserRequest user) {
        //todo custom exceptions + logging
        if(user == null) throw new NullPointerException("Requested user is null");
        User existingUser = userRepository.findByEmail(user.getEmail());
        Optional.ofNullable(user.getName()).ifPresent(existingUser::setName);
        Optional.ofNullable(user.getPassword())
                .ifPresent(password -> existingUser.setPassword(passwordEncoder.encode(user.getPassword())));
        if (user.getGender() != null) {
            try {
                Gender existingGender = genderRepository.findByName(user.getGender().getName());
                existingUser.setGender(existingGender);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid gender: " + user.getGender(), e);
            }
        }
        if(user.getAge() > 0) existingUser.setAge(user.getAge());
        if(user.getHeight() > 0) existingUser.setHeight(user.getHeight());
        if(user.getWeight() > 0) existingUser.setWeight(user.getWeight());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        //todo custom exceptions + logging
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null) throw new UsernameNotFoundException("User with email " + username + " not found");

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
