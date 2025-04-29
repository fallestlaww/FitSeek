package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating responses about operations with {@link org.example.fitseek.model.TrainingType} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents name of requested by user training type.
 * {@link #message} represents message for user.
 * {@link #information} represents information about training type for user.
 *
 * @see org.example.fitseek.model.TrainingType
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TrainingTypeResponse {
    String name;
    String message;
    String information;

    public TrainingTypeResponse(String name, String message, String information) {
        this.name = name;
        this.message = message;
        this.information = information;
    }
}
