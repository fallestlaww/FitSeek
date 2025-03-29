package org.example.fitseek.service.impl;

import org.example.fitseek.model.Gender;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderServiceImpl implements GenderService {
    @Autowired
    private GenderRepository genderRepository;

    @Override
    public Gender chooseGender(String gender) {
        Gender foundGender = genderRepository.findByName(gender);
        if (foundGender == null) {
            throw new NullPointerException("Gender not found: " + gender);
        }
        return genderRepository.findByName(gender);
    }
}
