package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExerciseResponseSplit {
    String name;
    MuscleResponse muscle;
    RecommendationResponse recommendations;
    DayResponse day;

    public ExerciseResponseSplit(Exercise exercise) {
        this.name = exercise.getName();
        this.recommendations = new RecommendationResponse(exercise);
        this.day = new DayResponse(exercise);
        this.muscle = new MuscleResponse(exercise);
    }
}
