package org.example.fitseek.service;

import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.model.Gender;

/**
 * <h4>Info</h4>
 * Realization of service layer for {@link Gender} entity.
 * Provides operations for retrieving entity object based on user parameters
 */
public interface GenderService {
    /**
     * Returns information about exercise based on {@link GenderRequest} object
     * @param genderRequest given {@link GenderRequest} object
     * @return information about user gender
     */
    Gender getGenderByName(GenderRequest genderRequest);
}
