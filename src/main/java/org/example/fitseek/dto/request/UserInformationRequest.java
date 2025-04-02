package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInformationRequest {
    @Min(value = 0, message = "Age must be higher than 0")
    private int age;
    @Min(value = 0, message = "Height must be higher than 0")
    private int height;
    @Min(value = 0, message = "Weight must be higher than 0")
    private int weight;
    @NotBlank(message = "Gender can't be blank")
    @NotNull(message = "Gender can't be null")
    private GenderRequest gender;
    @NotBlank(message = "Training type can't be blank")
    @NotNull(message = "Training type can't be null")
    private TrainingTypeRequest trainingType;
}
