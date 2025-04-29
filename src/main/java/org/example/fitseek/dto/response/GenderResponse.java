package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating responses about creating and updating {@link org.example.fitseek.model.Gender} entity.
 * Using for transferring data between user and backend in JSON format.
 * This class created specially for working with case, if user want to get all exercise by special gender.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents a name of an exercise, can't be null or blank
 * {@link #gender} represents a name of the gender user entered
 * {@link #muscle} represents a response object for class {@link org.example.fitseek.model.Muscle},
 * which contains all needed information about muscle type for the exercise.
 *
 * @see org.example.fitseek.model.Gender
 * @see org.example.fitseek.model.Muscle
 * @see org.example.fitseek.model.Exercise
 */
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
