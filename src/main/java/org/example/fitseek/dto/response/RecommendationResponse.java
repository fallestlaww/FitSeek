package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecommendationResponse {
    Integer recommendedApproaches;
    Integer recommendedRepeats;
    String recommendedWeight;

    public RecommendationResponse(Exercise exercise) {
        this.recommendedApproaches = exercise.getRecommendationList().getFirst().getRecommendedApproaches();
        this.recommendedRepeats = exercise.getRecommendationList().getFirst().getRecommendedRepeats();
        this.recommendedWeight = exercise.getRecommendationList().getFirst().getRecommendedWeight();
    }
}
