package org.example.fitseek.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.fitseek.model.User;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating requests for working with {@link org.example.fitseek.model.User} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents a name of the user
 * {@link #email} represents an email of the user
 * must be minimum 6 symbols long, using digits and latin letters
 * must contain at least one uppercase letter
 * {@link #age} represents user age.
 * {@link #weight} represents user weight.
 * {@link #gender} represents a request object for class {@link org.example.fitseek.model.Gender},
 * which contains all information about gender of the user given by user.
 *
 * @see org.example.fitseek.model.User
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    Long id;
    String name;
    String email;
    int age;
    double weight;
    String gender;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
        this.weight = user.getWeight();
        this.gender = user.getGender().getName();
    }
}
