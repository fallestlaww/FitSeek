package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.Min;

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
