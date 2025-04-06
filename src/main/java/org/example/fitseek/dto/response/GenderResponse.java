package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenderResponse {
    String gender;
    String name;
    MuscleResponse muscle;

    public GenderResponse(Exercise exercise, String gender) {
        this.gender = gender;
        this.name = exercise.getName();
        this.muscle = new MuscleResponse(exercise);
    }
}
