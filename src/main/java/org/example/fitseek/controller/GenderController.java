package org.example.fitseek.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.response.GenderResponse;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.model.Gender;
import org.example.fitseek.service.ExerciseService;
import org.example.fitseek.service.GenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController()
public class GenderController {
    private final GenderService genderService;
    private final ExerciseService exerciseService;

    public GenderController(ExerciseService exerciseService, GenderService genderService) {
        this.exerciseService = exerciseService;
        this.genderService = genderService;
    }

    @PostMapping("/gender")
    public ResponseEntity<?> selectGender(@RequestBody GenderRequest genderRequest) {
        log.debug("Requested gender: {}", genderRequest);
        Gender choosenGender = genderService.getExerciseByGender(genderRequest);
        log.info("Chosen gender: {}", choosenGender.getName());

        String genderName = choosenGender.getName();
        List<Exercise> exercises = exerciseService.exerciseListForGender(choosenGender.getId());
        List<GenderResponse> genderResponses =
                exercises.stream().map(exercise -> new GenderResponse(exercise, genderName)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(genderResponses);
    }
}
