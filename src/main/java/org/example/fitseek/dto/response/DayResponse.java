package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Exercise;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating responses about creating and updating {@link org.example.fitseek.model.Day} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents a name of a day
 *
 * @see org.example.fitseek.dto.request.DayRequest
 * @see org.example.fitseek.model.Day
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayResponse {
    String name;

    public DayResponse(Exercise exercise) {
        this.name = exercise.getDay().getName();
    }
}
