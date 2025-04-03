package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecommendationResponse {
    Integer recommendedSets;
    Integer recommendedRepeats;
    Integer recommendedWeightMin;
    Integer recommendedWeightMax;

    public RecommendationResponse(Exercise exercise) {
        this.recommendedSets = exercise.getRecommendationList().getFirst().getRecommendedSets();
        this.recommendedRepeats = exercise.getRecommendationList().getFirst().getRecommendedRepeats();
        this.recommendedWeightMin = exercise.getRecommendationList().getFirst().getRecommendedWeightMin();
        this.recommendedWeightMax = exercise.getRecommendationList().getFirst().getRecommendedWeightMax();
    }
}
