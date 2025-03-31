package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int musclesId;
    private int dayId;
    private int genderId;
    private int recommendedApproaches;
    private int recommendedRepeats;
    private String recommendedWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id == exercise.id && musclesId == exercise.musclesId && dayId == exercise.dayId && genderId == exercise.genderId && recommendedApproaches == exercise.recommendedApproaches && recommendedRepeats == exercise.recommendedRepeats && Objects.equals(name, exercise.name) && Objects.equals(recommendedWeight, exercise.recommendedWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, musclesId, dayId, genderId, recommendedApproaches, recommendedRepeats, recommendedWeight);
    }
}
