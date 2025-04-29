package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating requests for working with user requests in {@link org.example.fitseek.controller.TrainingController#trainingTypeExercises(UserInformationRequest)}.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #age} represents user age.
 * {@link #weight} represent user weight.
 * {@link #gender} represents a request object for class {@link org.example.fitseek.model.Gender},
 *  which contains all information about gender of the user given by user.
 *  {@link #trainingType} represents a request object for class {@link org.example.fitseek.model.TrainingType}
 * which contains all information about training type, which user choose.
 *
 * @see org.example.fitseek.model.Gender
 * @see org.example.fitseek.model.TrainingType
 */
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
