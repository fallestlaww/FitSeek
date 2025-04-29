package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating requests for CRUD operations for {@link org.example.fitseek.model.Exercise} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents a name of an exercise, can't be null or blank.
 * {@link #muscle} represents a request object for class {@link org.example.fitseek.model.Muscle},
 * which contains all needed information about muscle type for the exercise.
 * {@link #day} represents a request object for class {@link org.example.fitseek.model.Day},
 * which contains all information about day for the exercise given by user.
 * {@link #gender} represents a request object for class {@link org.example.fitseek.model.Gender},
 * which contains all information about gender for the exercise given by user.
 * {@link #recommendation} represents a list of request objects for {@link org.example.fitseek.model.Recommendation},
 * which contain all information about recommendations for the exercise given by user.
 *
 * @see org.example.fitseek.model.Exercise
 * @see org.example.fitseek.dto.response.ExerciseResponseSplit
 * @see org.example.fitseek.dto.response.ExerciseResponseFullbody
 * @see MuscleRequest
 * @see DayRequest
 * @see GenderRequest
 * @see RecommendationRequest
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExerciseRequest {
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name can't be null")
    private String name;
    @NotBlank(message = "Muscle can't be blank")
    @NotNull(message = "Muscle can't be null")
    private MuscleRequest muscle;
    @NotBlank(message = "Day can't be blank")
    @NotNull(message = "Day can't be null")
    private DayRequest day;
    @NotBlank(message = "Gender can't be blank")
    @NotNull(message = "Gender can't be null")
    private GenderRequest gender;
    @NotBlank(message = "Recommendation can't be blank")
    @NotNull(message = "Recommendation can't be null")
    private List<RecommendationRequest> recommendation;
}
