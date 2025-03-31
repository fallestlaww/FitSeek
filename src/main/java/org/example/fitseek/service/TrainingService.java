package org.example.fitseek.service;

import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.model.TrainingType;

public interface TrainingService {
    TrainingType chooseTraining(TrainingTypeRequest request);
}
