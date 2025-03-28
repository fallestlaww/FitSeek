package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.Gender;
import org.example.fitseek.model.User;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    long id;
    String name;
    String email;
    int age;
    int height;
    int weight;
    String gender;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.gender = user.getGender().toString();
    }
}
