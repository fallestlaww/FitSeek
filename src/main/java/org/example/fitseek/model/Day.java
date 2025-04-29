package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * <h4>Info:</h4>
 *Entity class that represent day of the week in the training schedule system.
 *Mapped to the {@code days} table in database
 *  <h4>Fields:</h4>
 *  <li>{@link #id} – unique identifier of the day (primary key).</li>
 *  <li>{@link #name} – name of the day (e.g. "Monday", "Tuesday").</li>
 */
@Entity
@Table(name = "days")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return Objects.equals(id, day.id) && Objects.equals(name, day.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
