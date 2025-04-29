package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * <h4>Info</h4>
 * Entity class that represents a training type in the application.
 *
 * Mapped to the {@code training_type} table in the database.
 *
 * <h4>Fields:</h4>
 *     <li>{@link #id} – unique identifier of the training type (primary key).</li>
 *     <li>{@link #name} – name of the training type (e.g. "Split", "FullBody").</li>
 */
@Entity
@Table(name = "training_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingType that = (TrainingType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
