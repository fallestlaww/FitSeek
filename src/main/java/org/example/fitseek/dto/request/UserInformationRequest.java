package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInformationRequest {
    @Min(value = 0, message = "Age must be higher than 0")
    private int age;
    @Min(value = 0, message = "Weight must be higher than 0")
    private double weight;
    @NotBlank(message = "Gender can't be blank")
    @NotNull(message = "Gender can't be null")
    private GenderRequest gender;
    @NotBlank(message = "Training type can't be blank")
    @NotNull(message = "Training type can't be null")
    private TrainingTypeRequest trainingType;
}
