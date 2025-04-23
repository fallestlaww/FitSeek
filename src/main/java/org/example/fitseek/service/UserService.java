package org.example.fitseek.service;

import org.example.fitseek.dto.request.UserRequest;
import org.example.fitseek.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    User createUser(UserRequest user);
    User readUser(String email);
    User updateUser(UserRequest user);
    void deleteUser(Long id);
    User readUserForAdmin(Long id);
}
