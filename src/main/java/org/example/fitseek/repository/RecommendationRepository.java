package org.example.fitseek.repository;

import org.example.fitseek.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * <h4>Info</h4>
 * Repository for {@link Recommendation} entity, provides CRUD operations and custom query methods for working with {@code recommendations} table in database
 */
@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    /**
     * Returns list of {@link Recommendation} objects founded in database by exercise name
     * @param name given exercise name for search
     * @return list of {@link Recommendation} objects
     */
    List<Recommendation> getRecommendationByExerciseName(String name);
    /**
     * Returns {@link Recommendation} object founded in database by exercise id, given user age and given user weight
     * @param exerciseId given exercise id for search
     * @param userAge given user age for search
     * @param userWeight given user weight for search
     * @return {@link Recommendation} object
     */
    @Query("SELECT r FROM Recommendation r WHERE r.exercise.id = :exerciseId AND r.userAge = :userAge AND r.userWeight = :userWeight")
    Recommendation getRecommendationByExerciseIdAndUserAgeAndUserWeight(@Param("exerciseId") Long exerciseId,
                                                                        @Param("userAge") int userAge,
                                                                        @Param("userWeight") double userWeight);
}
