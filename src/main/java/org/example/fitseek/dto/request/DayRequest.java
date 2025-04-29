package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating requests for creating and updating {@link org.example.fitseek.model.Day} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents a name of a day, can't be null or blank
 *
 * @see org.example.fitseek.model.Day
 * @see org.example.fitseek.dto.response.DayResponse
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayRequest {
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name can't be null")
    private String name;
}
