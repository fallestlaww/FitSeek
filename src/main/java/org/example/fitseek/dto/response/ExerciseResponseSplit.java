package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.model.Exercise;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating responses about operations with {@link org.example.fitseek.model.Exercise} entity.
 * This class created specially for working with case,, if {@link TrainingTypeRequest#getName()} equals "Split".
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents a name of an exercise
 * {@link #muscle} represents a response object for class {@link org.example.fitseek.model.Muscle},
 * which contains all needed information about muscle type for the exercise.
 * {@link #recommendations} represents a response object for class {@link org.example.fitseek.model.Recommendation},
 * which contains all needed information about recommendation for the exercise
 * {@link #day} represents a response object for class {@link org.example.fitseek.model.Day},
 * which contains all needed information about day for the exercise.
 *
 * @see org.example.fitseek.model.Exercise
 * @see org.example.fitseek.dto.response.ExerciseResponseFullbody
 * @see ExerciseRequest
 * @see MuscleResponse
 * @see RecommendationResponse
 */
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
