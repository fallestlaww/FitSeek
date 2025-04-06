package org.example.fitseek.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.model.Gender;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class GenderServiceImpl implements GenderService {
    @Autowired
    private GenderRepository genderRepository;

    @Override
    public Gender chooseGender(GenderRequest genderRequest) {
        log.debug("Requested gender: {}", genderRequest.getName());
        Gender foundGender = genderRepository.findByName(genderRequest.getName());
        log.debug("Found gender: {}", Objects.toString(foundGender, "null"));
        if (foundGender == null || foundGender.getName() == null) {
            log.error("Gender not found: {}", genderRequest.getName());
            throw new EntityNotFoundException("Gender not found: " + genderRequest.getName());
        }
        return foundGender;
    }
}
