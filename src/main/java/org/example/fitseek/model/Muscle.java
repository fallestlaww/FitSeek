package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * <h4>Info:</h4>
 * Entity class that represents type of muscle for the exercise for the training system.
 * Mapped to the {@code muscles} table in database
 * <h4>Fields:</h4>
 * <li>{@link #id} – unique identifier of the muscle (primary key).</li>
 *  <li>{@link #name} – name of the muscle (e.g. "Chest", "Biceps").</li>
 */
@Entity
@Table(name = "muscles")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Muscle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Muscle muscle = (Muscle) o;
        return Objects.equals(id, muscle.id) && Objects.equals(name, muscle.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
