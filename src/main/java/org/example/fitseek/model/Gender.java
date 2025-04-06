package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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