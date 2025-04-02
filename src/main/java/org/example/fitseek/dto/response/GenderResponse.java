package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

import java.util.List;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenderResponse {
    String name;
    List<String> typesOfTraining;

    public GenderResponse(String name, List<String> typesOfTraining) {
        this.name = name;
        this.typesOfTraining = typesOfTraining;
    }
}
