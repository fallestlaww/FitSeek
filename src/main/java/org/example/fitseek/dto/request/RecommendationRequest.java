package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.Min;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating requests for CRUD operations for {@link org.example.fitseek.model.Recommendation} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #exerciseId} represents the ID of the exercise whose recommendations the user wants to work with.
 * {@link #userAge} represents user age.
 * {@link #userWeight} represents user weight.
 * {@link #recommendedSets} represents recommended number of sets for exercise.
 * {@link #recommendedRepeats} represents recommended number of repeats for exercise.
 * {@link #recommendedWeightMin} represents recommended min weight for exercise.
 * {@link #recommendedWeightMax} represents recommended max weight for exercise.
 *
 * @see org.example.fitseek.model.Recommendation
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecommendationRequest {
    @Min(value = 1, message = "Exercise id must be higher than 0")
    private Long exerciseId;
    @Min(value = 1, message = "Age must be higher than 0")
    private int userAge;
    @Min(value = 1, message = "Weight must be higher than 0")
    private double userWeight;
    @Min(value = 1, message = "Recommended sets number must be higher than 0")
    private int recommendedSets;
    @Min(value = 1, message = "Recommended repeats number must be higher than 0")
    private int recommendedRepeats;
    @Min(value = 1, message = "Min recommended weight number must be higher than 0")
    private int recommendedWeightMin;
    @Min(value = 1, message = "Max recommended weight number must be higher than 0")
    private int recommendedWeightMax;
}
