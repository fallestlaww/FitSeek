package org.example.fitseek.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Exercise exercise;
    private int userAge;
    private double userWeight;
    private int recommendedSets;
    private int recommendedRepeats;
    private int recommendedWeightMin;
    private int recommendedWeightMax;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return userAge == that.userAge && Double.compare(userWeight, that.userWeight) == 0 && recommendedSets == that.recommendedSets && recommendedRepeats == that.recommendedRepeats && recommendedWeightMin == that.recommendedWeightMin && recommendedWeightMax == that.recommendedWeightMax && Objects.equals(id, that.id) && Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, userAge, userWeight, recommendedSets, recommendedRepeats, recommendedWeightMin, recommendedWeightMax);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", exercise=" + exercise +
                ", userAge=" + userAge +
                ", userWeight=" + userWeight +
                ", recommendedSets=" + recommendedSets +
                ", recommendedRepeats=" + recommendedRepeats +
                ", recommendedWeightMin=" + recommendedWeightMin +
                ", recommendedWeightMax=" + recommendedWeightMax +
                '}';
    }
}
