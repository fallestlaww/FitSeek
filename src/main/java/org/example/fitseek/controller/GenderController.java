package org.example.fitseek.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.response.GenderResponse;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.model.Gender;
import org.example.fitseek.service.ExerciseService;
import org.example.fitseek.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController()
public class GenderController {
    @Autowired
    private GenderService genderService;
    @Autowired
    private ExerciseService exerciseService;
    public final static String MALE_GENDER = "Male";
    public final static String FEMALE_GENDER = "Female";

    @PostMapping("/gender")
    public ResponseEntity<?> selectGender(@RequestBody GenderRequest genderRequest) {
        log.debug("Requested gender: {}", Objects.toString(genderRequest, "null"));
        if(genderRequest == null) {
            log.warn("Gender request is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Gender choosenGender = genderService.chooseGender(genderRequest);
            log.info("Chosen gender: {}", choosenGender.getName());
            String genderName = choosenGender.getName().equalsIgnoreCase(MALE_GENDER) ? MALE_GENDER : FEMALE_GENDER;
            List<Exercise> exercises = exerciseService.exerciseListForGender(choosenGender.getId());
            List<GenderResponse> genderResponses =
                    exercises.stream().map(exercise -> new GenderResponse(exercise, genderName)).toList();
            return ResponseEntity.status(HttpStatus.OK).body(genderResponses);
        } catch (NullPointerException e) {
            log.error("Gender doesn't exist: {}", genderRequest.getName());
            return ResponseEntity.notFound().build();
        }
    }
}
