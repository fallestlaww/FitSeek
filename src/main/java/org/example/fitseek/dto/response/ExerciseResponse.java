package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExerciseResponse {
    String name;
    Integer recommendedApproaches;
    Integer recommendedRepeats;
    String recommendedWeight;
    Integer musclesId;
    Integer dayId;

    public ExerciseResponse(Exercise exercise) {
        this.name = exercise.getName();
        this.recommendedApproaches = exercise.getRecommendedApproaches();
        this.recommendedRepeats = exercise.getRecommendedRepeats();
        this.recommendedWeight = exercise.getRecommendedWeight();
        this.dayId = exercise.getDayId();
        this.musclesId = exercise.getMusclesId();
    }
}
