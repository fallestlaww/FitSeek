package org.example.fitseek.controller;

import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.response.GenderResponse;
import org.example.fitseek.model.Gender;
import org.example.fitseek.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class GenderController {
    @Autowired
    private GenderService genderService;
    public final static String MALE_GENDER = "Male";
    public final static String FEMALE_GENDER = "Female";
    public final static String SPLIT_TRAINING = "Split";
    private final static String FULLBODY_TRAINING = "FullBody";

    @PostMapping("/gender")
    public ResponseEntity<?> selectGender(@RequestBody GenderRequest genderRequest) {
        if(genderRequest == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            Gender choosenGender = genderService.chooseGender(genderRequest);
            String genderName = choosenGender.getName().equalsIgnoreCase(MALE_GENDER) ? MALE_GENDER : FEMALE_GENDER;
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenderResponse(genderName, List.of(SPLIT_TRAINING, FULLBODY_TRAINING)));
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
