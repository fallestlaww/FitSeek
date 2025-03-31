package org.example.fitseek.service;

import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.model.Gender;

public interface GenderService {
    Gender chooseGender(GenderRequest genderRequest);
}
