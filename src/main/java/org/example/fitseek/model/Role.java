package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * <h4>Info</h4>
 * Entity class that represents a user's role in the application.
 *
 * Mapped to the {@code roles} table in the database.
 *
 * <h4>Fields:</h4>
 *     <li>{@link #id} – unique identifier of the role (primary key).</li>
 *     <li>{@link #name} – name of the role (e.g. "ADMIN", "USER").</li>
 *
 * <h4>Implements:</h4>
 *     <li>{@link GrantedAuthority} – provides integration with Spring Security for role-based access control.</li>
 * @see org.springframework.security.core.GrantedAuthority
 */

@Getter
@Setter
@Entity
@Table(name = "roles")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}