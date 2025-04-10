package org.example.fitseek.repository;

import org.example.fitseek.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> getRecommendationByExerciseName(String name);
    Recommendation getRecommendationByExerciseNameAndUserAgeAndUserWeight(String name, int userAge, double userWeight);
}
