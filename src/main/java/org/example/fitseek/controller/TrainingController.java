package org.example.fitseek.controller;

import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.dto.response.ExerciseResponseFullbody;
import org.example.fitseek.dto.response.ExerciseResponseSplit;
import org.example.fitseek.dto.response.TrainingTypeResponse;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TrainingController {
    @Autowired
    private ExerciseService exerciseService;
    private final static String MALE_GENDER = "Male";
    private final static String SPLIT_TRAINING = "Split";
    private final static String FULLBODY_TRAINING = "FullBody";

    @PostMapping("/training-type")
    public ResponseEntity<?> trainingType(@RequestBody TrainingTypeRequest trainingTypeRequest) {
        if(trainingTypeRequest == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(!trainingTypeRequest.getName().equalsIgnoreCase(SPLIT_TRAINING) &&
                !trainingTypeRequest.getName().equalsIgnoreCase(FULLBODY_TRAINING)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new TrainingTypeResponse(trainingTypeRequest.getName(), "Enter your age, weight and height"));
    }

    @PostMapping("/training-type/exercises")
    public ResponseEntity<?> trainingTypeExercises(@RequestBody UserInformationRequest userRequest) {
        if (userRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body cannot be null");
        }
        if (userRequest.getGender() == null || userRequest.getGender().getName() == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Gender cannot be null");
        }
        if (userRequest.getTrainingType() == null || userRequest.getTrainingType().getName() == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Training type cannot be null");
        }

        Long genderId = userRequest.getGender().getName().equalsIgnoreCase(MALE_GENDER) ? 1L : 2L;
        List<Exercise> exercises;
        List<?> exerciseResponses;

        if (userRequest.getTrainingType().getName().equalsIgnoreCase(FULLBODY_TRAINING)) {
            exercises = exerciseService.exerciseListForFullBody(
                    userRequest.getAge(), userRequest.getWeight(), genderId);
            exerciseResponses = exercises.stream().map(ExerciseResponseFullbody::new).toList();
        } else if (userRequest.getTrainingType().getName().equalsIgnoreCase(SPLIT_TRAINING)) {
            exercises = exerciseService.exerciseListForSplit(
                    userRequest.getAge(), userRequest.getWeight(), genderId);
            exerciseResponses = exercises.stream().map(ExerciseResponseSplit::new).toList();
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid training type");
        }
        return ResponseEntity.ok(exerciseResponses);
    }
}
