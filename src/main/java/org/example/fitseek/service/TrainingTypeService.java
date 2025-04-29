package org.example.fitseek.service;

import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.model.Exercise;

import java.util.List;

/**
 * <h4>Info</h4>
 * Realization of service layer for {@link org.example.fitseek.model.TrainingType} entity.
 * Provides operations for retrieving entity object based on user parameters
 */
public interface TrainingTypeService {
    /**
     * Returns list of exercises based on {@link UserInformationRequest} object
     * @param userRequest given {@link UserInformationRequest} object
     * @return appropriate list of exercises
     */
    List<Exercise> trainingTypeExercises(UserInformationRequest userRequest);
}
