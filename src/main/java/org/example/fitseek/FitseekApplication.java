package org.example.fitseek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FitseekApplication {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "test";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Хеш пароля: " + encodedPassword);
        SpringApplication.run(FitseekApplication.class, args);
    }

}
