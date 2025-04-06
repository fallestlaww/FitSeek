package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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
