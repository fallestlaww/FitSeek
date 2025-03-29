package org.example.fitseek.controller;

import org.example.fitseek.model.Gender;
import org.example.fitseek.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController()
@RequestMapping({"/", "/home"})
public class GenderController {
    @Autowired
    private GenderService genderService;

    @PostMapping
    public ResponseEntity<?> selectGender(@RequestBody String gender) {
        try {
            Gender choosenGender = genderService.chooseGender(gender);
            if (choosenGender.getName().equals("Male")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("/male-training"))
                        .build();
            }
            if (choosenGender.getName().equals("Female")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("/female-training"))
                        .build();
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
