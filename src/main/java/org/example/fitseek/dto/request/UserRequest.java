package org.example.fitseek.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @Min(value = 0, message = "Height must be higher than 0")
    private int height;
    @Min(value = 0, message = "Weight must be higher than 0")
    private int weight;
    @NotBlank(message = "Gender can't be blank")
    @NotNull(message = "Gender can't be null")
    private String gender;
}
