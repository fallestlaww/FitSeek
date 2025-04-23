package org.example.fitseek.service;

import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.model.Exercise;

import java.util.List;

public interface TrainingTypeService {
    List<Exercise> trainingTypeExercises(UserInformationRequest userRequest);
}
