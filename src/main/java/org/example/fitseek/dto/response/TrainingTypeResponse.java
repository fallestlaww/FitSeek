package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

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
