package org.example.fitseek.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.model.Gender;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.service.GenderService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementation of service layer {@link GenderService} for {@link Gender} entity.
 * Provides logic for managing {@link GenderService},
 */
@Slf4j
@Service
public class GenderServiceImpl implements GenderService {
    private final GenderRepository genderRepository;
    public GenderServiceImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    @Override
    public Gender getGenderByName(GenderRequest genderRequest) {
        log.debug("Requested gender: {}", Objects.toString(genderRequest, "null"));
        if(genderRequest == null || genderRequest.getName() == null || genderRequest.getName().isEmpty()) {
            log.warn("Gender request cannot be null");
            throw new EntityNullException("Gender request cannot be null");
        }
        Gender foundGender = genderRepository.findByName(genderRequest.getName());
        log.debug("Found gender: {}", Objects.toString(foundGender, "null"));
        if (foundGender == null || foundGender.getName() == null) {
            log.error("Gender not found: {}", genderRequest.getName());
            throw new EntityNotFoundException("Gender not found: " + genderRequest.getName());
        }
        return foundGender;
    }
}
