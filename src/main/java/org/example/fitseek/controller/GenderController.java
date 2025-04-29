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

/**<h4>Info</h4>
 * REST-controller, responsible for processing operations on {@link Gender} entity
 * <h4>Fields</h4>
 * {@link #genderService} - object of {@link GenderService}, responsible for business logic to work with {@link Gender } entity.
 * {@link #exerciseService} - object of {@link ExerciseService}, responsible for business logic to work with {@link Exercise} entity
 *
 * @see GenderService
 * @see ExerciseService
 */
@Slf4j
@RestController
public class GenderController {
    private final GenderService genderService;
    private final ExerciseService exerciseService;

    public GenderController(ExerciseService exerciseService, GenderService genderService) {
        this.exerciseService = exerciseService;
        this.genderService = genderService;
    }

    /**
     * Gives out in response all exercise for specific gender
     * @param genderRequest object with requested gender name from user
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and list of all exercise for specific gender in response
     */
    @PostMapping("/gender")
    public ResponseEntity<?> selectGender(@RequestBody GenderRequest genderRequest) {
        log.debug("Requested gender: {}", genderRequest);
        // find object of class gender by name, if gender not found, then appropriate status code will be thrown
        Gender choosenGender = genderService.getGenderByName(genderRequest);
        log.info("Chosen gender: {}", choosenGender.getName());

        // find exercise by gender name, if exercises not found, then appropriate status code will be thrown
        List<Exercise> exercises = exerciseService.exerciseListForGender(choosenGender.getId());
        // create gender response with exercise list and gender name
        List<GenderResponse> genderResponses =
                exercises.stream().map(exercise -> new GenderResponse(exercise, choosenGender.getName())).toList();

        return ResponseEntity.status(HttpStatus.OK).body(genderResponses);
    }
}
