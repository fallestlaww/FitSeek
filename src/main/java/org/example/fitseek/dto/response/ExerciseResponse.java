package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExerciseResponse {
    String name;
    RecommendationResponse recommendationResponse;
    MuscleResponse muscleResponse;
    DayResponse dayResponse;

    public ExerciseResponse(Exercise exercise) {
        this.name = exercise.getName();
        this.recommendationResponse = new RecommendationResponse(exercise);
        this.dayResponse = new DayResponse(exercise);
        this.muscleResponse = new MuscleResponse(exercise);
    }
}
