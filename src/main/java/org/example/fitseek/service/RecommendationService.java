package org.example.fitseek.service;

import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.model.Recommendation;

import java.util.List;

public interface RecommendationService {
    List<Recommendation> readRecommendation(Long id);
    Recommendation updateRecommendation(RecommendationRequest recommendation);
    void deleteRecommendation(RecommendationRequest recommendation);
}
