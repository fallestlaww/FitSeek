package org.example.fitseek.service.impl;

import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.model.TrainingType;
import org.example.fitseek.repository.TrainingTypeRepository;
import org.example.fitseek.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Override
    public TrainingType chooseTraining(TrainingTypeRequest request) {
        if(request == null) throw new NullPointerException("Training type cannot be null");
        TrainingType trainingType = trainingTypeRepository.findByName(request.getName());
        if(trainingType == null) throw new NullPointerException("Training type not found");
        return trainingType;
    }
}
