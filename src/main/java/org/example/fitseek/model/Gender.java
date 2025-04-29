package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * <h4>Info:</h4>
 * Entity class that represents gender type of user for the training system.
 * Mapped to the {@code gender} table in database
 * <h4>Fields:</h4>
 *  <li>{@link #id} – unique identifier of the gender (primary key).</li>
 *  <li>{@link #name} – name of the gender (e.g. "Male", "Female").</li>
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "gender")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gender gender = (Gender) o;
        return Objects.equals(id, gender.id) && Objects.equals(name, gender.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}