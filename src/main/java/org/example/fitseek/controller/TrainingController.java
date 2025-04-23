package org.example.fitseek.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.dto.response.ExerciseResponseFullbody;
import org.example.fitseek.dto.response.ExerciseResponseSplit;
import org.example.fitseek.dto.response.TrainingTypeResponse;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.exception.exceptions.InvalidRequestException;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.service.ExerciseService;
import org.example.fitseek.service.impl.TrainingTypeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class TrainingController {
    private final ExerciseService exerciseService;
    private final static String SPLIT_TRAINING = "Split";
    private final static String FULLBODY_TRAINING = "FullBody";
    private final TrainingTypeServiceImpl trainingTypeServiceImpl;

    public TrainingController(ExerciseService exerciseService, TrainingTypeServiceImpl trainingTypeServiceImpl) {
        this.exerciseService = exerciseService;
        this.trainingTypeServiceImpl = trainingTypeServiceImpl;
    }

    @PostMapping("/training-type/information")
    public ResponseEntity<?> trainingType(@RequestBody TrainingTypeRequest trainingTypeRequest) {
        log.debug("Requested training type {}", Objects.toString(trainingTypeRequest, "null"));
        if(trainingTypeRequest == null || trainingTypeRequest.getName() == null || trainingTypeRequest.getName().isEmpty()) {
            log.warn("Requested training type is null");
            throw new EntityNullException("Training type is null");
        }
        if(!trainingTypeRequest.getName().equalsIgnoreCase(SPLIT_TRAINING) &&
                !trainingTypeRequest.getName().equalsIgnoreCase(FULLBODY_TRAINING)) {
            log.warn("Requested training type name {} is not valid", trainingTypeRequest.getName());
            throw new InvalidRequestException("Requested training type name is not valid");
        }

        if(trainingTypeRequest.getName().equalsIgnoreCase(SPLIT_TRAINING)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new TrainingTypeResponse(trainingTypeRequest.getName(), "Enter your age, weight and height below"
                            ,"Split -  is a type of training where different muscle groups are worked on different days. " +
                            "For example, you might train your legs one day, your chest and biceps on another, and your back and triceps on a third. " +
                            "This approach allows you to recover your muscles better and train with greater intensity."));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new TrainingTypeResponse(trainingTypeRequest.getName(), "Enter your age, weight and height below",
                            "FullBody -  is a type of training that works all major muscle groups in one session" +
                                    "This approach is suitable for beginners or those who train 2–3 times a week, and promotes overall strength and endurance development."));
        }
    }

    @PostMapping("/training-type/exercises")
    public ResponseEntity<?> trainingTypeExercises(@RequestBody UserInformationRequest userRequest) {
        List<Exercise> exercises = trainingTypeServiceImpl.trainingTypeExercises(userRequest);
        List<?> exerciseResponses;

        if (userRequest.getTrainingType().getName().equalsIgnoreCase(FULLBODY_TRAINING)) {
            log.info("Exercise added to full body training: {}", exercises.toString());
            exerciseResponses = exercises.stream().map(ExerciseResponseFullbody::new).toList();
        } else if (userRequest.getTrainingType().getName().equalsIgnoreCase(SPLIT_TRAINING)) {
            log.info("Exercise added to split training: {}", exercises.toString());
            exerciseResponses = exercises.stream().map(ExerciseResponseSplit::new).toList();
        } else {
            log.warn("Invalid training type {}", userRequest.getTrainingType().getName());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Invalid training type");
        }
        return ResponseEntity.ok(exerciseResponses);
    }
}
