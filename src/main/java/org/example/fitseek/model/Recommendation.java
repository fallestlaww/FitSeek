package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Exercise exercise;
    private int age;
    private double weight;
    private double height;
    private int recommendedApproaches;
    private int recommendedRepeats;
    private String recommendedWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return age == that.age && Double.compare(weight, that.weight) == 0 && Double.compare(height, that.height) == 0 && recommendedApproaches == that.recommendedApproaches && recommendedRepeats == that.recommendedRepeats && Objects.equals(id, that.id) && Objects.equals(exercise, that.exercise) && Objects.equals(recommendedWeight, that.recommendedWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, age, weight, height, recommendedApproaches, recommendedRepeats, recommendedWeight);
    }
}
