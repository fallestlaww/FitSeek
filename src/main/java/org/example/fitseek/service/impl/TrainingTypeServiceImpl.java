package org.example.fitseek.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.exception.exceptions.InvalidRequestException;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.service.ExerciseService;
import org.example.fitseek.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
/**
 * Implementation of service layer {@link org.example.fitseek.service.TrainingTypeService} for {@link org.example.fitseek.model.TrainingType} entity.
 * Provides logic for managing {@link TrainingTypeService},
 */
@Service
@Slf4j
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private static final String MALE_GENDER = "male";
    private static final String FULLBODY_TRAINING = "fullbody";
    private static final String SPLIT_TRAINING = "split";
    @Autowired
    private ExerciseService exerciseService;

    @Override
    public List<Exercise> trainingTypeExercises(UserInformationRequest userRequest) {
        log.debug("Requested user information {}", Objects.toString(userRequest, "null"));
        if (userRequest == null) {
            log.warn("Requested user information is null");
            throw new InvalidRequestException("Requested user information is null");
        }
        if (userRequest.getGender() == null || userRequest.getGender().getName() == null) {
            log.warn("Requested user information gender is null: {}", Objects.toString(userRequest.getGender(), "null"));
            throw new InvalidRequestException("Requested user information gender is null");
        }
        if (userRequest.getTrainingType() == null || userRequest.getTrainingType().getName() == null) {
            log.warn("Requested user training type is null: {}", Objects.toString(userRequest.getTrainingType(), "null"));
            throw new InvalidRequestException("Requested user training type is null");
        }

        // defines user gender, if male -> return 1L, if not male -> return 2L
        Long genderId = userRequest.getGender().getName().equalsIgnoreCase(MALE_GENDER) ? 1L : 2L;
        log.info("Gender id: {}", genderId);
        List<Exercise> exercises;

        // generate training program for user based on his age, weight and gender
        if (userRequest.getTrainingType().getName().equalsIgnoreCase(FULLBODY_TRAINING)) {
            exercises = exerciseService.exerciseListForFullBody(
                    userRequest.getAge(), userRequest.getWeight(), genderId);
            log.info("Exercise added to full body training: {}", exercises.toString());
        } else if (userRequest.getTrainingType().getName().equalsIgnoreCase(SPLIT_TRAINING)) {
            exercises = exerciseService.exerciseListForSplit(
                    userRequest.getAge(), userRequest.getWeight(), genderId);
            log.info("Exercise added to split training: {}", exercises.toString());
        } else {
            log.warn("Invalid training type {}", userRequest.getTrainingType().getName());
            throw new InvalidEntityException("Invalid training type");
        }
        return exercises;
    }
}
