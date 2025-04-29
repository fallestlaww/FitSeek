package org.example.fitseek.service;

import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.model.Recommendation;

import java.util.List;

/**
 * <h4>Info</h4>
 * Realization of service layer for {@link Recommendation} entity.
 * Provides CRUD operations for entity object based on user parameters
 */
public interface RecommendationService {
    /**
     * Returns list of recommendations for exercises, found by given exercise id
     * @param id given exercise id
     * @return list of recommendations
     */
    List<Recommendation> readRecommendation(Long id);
    /**
     * Updates the information provided in the request in the recommendations for exercise found using the request also.
     * @param recommendation object of {@link RecommendationRequest} which include all needed information
     * @return updated {@link Recommendation} object
     */
    Recommendation updateRecommendation(RecommendationRequest recommendation);
    /**
     * Deletes {@link Recommendation} entity found by recommendation request.
     * @param recommendation given object of {@link RecommendationRequest}
     */
    void deleteRecommendation(RecommendationRequest recommendation);
}
