package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;
/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating responses about operations with {@link org.example.fitseek.model.Recommendation} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #recommendedSets} represents number of recommended sets for exercise according to user age and weight.
 * {@link #recommendedRepeats} represents number of recommended repeats for exercise according to user age and weight.
 * {@link #recommendedWeightMin} represents number of recommended minimal weight for exercise according to user age and weight.
 * {@link #recommendedWeightMax} represents number of recommended maximal weight for exercise according to user age and weight.
 *
 * @see org.example.fitseek.model.Recommendation
 */
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
