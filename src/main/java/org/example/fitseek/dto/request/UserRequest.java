package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.*;

/**
 * <h4>Info</h4>
 * Realization of class, which is using for creating requests for working with {@link org.example.fitseek.model.User} entity.
 * Using for transferring data between user and backend in JSON format.
 * The field naming format corresponds to "snake_case".
 * <h4>Fields</h4>
 * {@link #name} represents a name of the user, can't be null or blank.
 * {@link #email} represents an email of the user, can't be null or blank.
 * {@link #password} represents user password, can't be null or blank,
 * must be minimum 6 symbols long, using digits and latin letters, must contain at least one digit,
 * must contain at least one uppercase letter, must contain at least one lowercase letter.
 * {@link #age} represents user age.
 * {@link #weight} represents user weight.
 * {@link #gender} represents a request object for class {@link org.example.fitseek.model.Gender},
 * which contains all information about gender of the user given by user.
 *
 * @see org.example.fitseek.model.User
 * @see org.example.fitseek.model.Gender
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequest {
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name can't be null")
    private String name;
    @Email(message = "Email must be specified correctly")
    @NotBlank(message = "Email can't be blank")
    @NotNull(message = "Email can't be null")
    private String email;
    @Pattern(regexp = "[A-Za-z\\d]{6,}",
            message = "Must be minimum 6 symbols long, using digits and latin letters")
    @Pattern(regexp = ".*\\d.*",
            message = "Must contain at least one digit")
    @Pattern(regexp = ".*[A-Z].*",
            message = "Must contain at least one uppercase letter")
    @Pattern(regexp = ".*[a-z].*",
            message = "Must contain at least one lowercase letter")
    private String password;
    @Min(value = 0, message = "Age must be higher than 0")
    private int age;
    @Min(value = 0, message = "Weight must be higher than 0")
    private double weight;
    @NotBlank(message = "Gender can't be blank")
    @NotNull(message = "Gender can't be null")
    private GenderRequest gender;
}
