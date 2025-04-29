package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <h4>Info</h4>
 * Entity class that represents a training type in the application.
 *
 * Mapped to the {@code user} table in the database.
 *
 * <h4>Fields:</h4>
 *     <li>{@link #id} – unique identifier of the user (primary key).</li>
 *     <li>{@link #name} – name of the user (e.g. "user_user", "John").</li>
 *     <li>{@link #password} - password of the user</li>
 *     <li>{@link #email} - email of the user</li>
 *     <li>{@link #age} - age of the user</li>
 *     <li>{@link #weight} - weight of the user</li>
 *     <li>{@link #gender} - object with name of the gender (e.g. "Male", "Female"</li>
 *     <li>{@link #role} - object with name of the role (e.g. "USER", "ADMIN)</li>
 *
 * <h4>Implements</h4>
 * <li>UserDetails - provides integration with Spring Security to work with user data</li>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
    @Pattern(regexp = "[A-Za-z\\d]{6,}",
            message = "Must be minimum 6 symbols long, using digits and latin letters")
    @Pattern(regexp = ".*\\d.*",
            message = "Must contain at least one digit")
    @Pattern(regexp = ".*[A-Z].*",
            message = "Must contain at least one uppercase letter")
    @Pattern(regexp = ".*[a-z].*",
            message = "Must contain at least one lowercase letter")
    @Column(name = "password", nullable = false)
    private String password;
    @Email
    private String email;
    private int age;
    private double weight;
    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && weight == user.weight && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(gender, user.gender) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, age, weight, gender, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
