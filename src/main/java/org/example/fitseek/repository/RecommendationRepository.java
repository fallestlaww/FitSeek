package org.example.fitseek.repository;

import org.example.fitseek.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> getRecommendationByExerciseName(String name);
    @Query("SELECT r FROM Recommendation r WHERE r.exercise.id = :exerciseId AND r.userAge = :userAge AND r.userWeight = :userWeight")
    Recommendation getRecommendationByExerciseIdAndUserAgeAndUserWeight(@Param("exerciseId") Long exerciseId,
                                                                        @Param("userAge") int userAge,
                                                                        @Param("userWeight") double userWeight);
}
