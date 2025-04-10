package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
