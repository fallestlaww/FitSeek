package org.example.fitseek.repository;

import org.example.fitseek.dto.response.ExerciseResponse;
import org.example.fitseek.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("SELECT e.name, r.recommendedApproaches, r.recommendedRepeats, r.recommendedWeight, e.muscle, e.day, e.gender " +
            "FROM Exercise e " +
            "JOIN Recommendation r ON r.exercise.id= e.id " +
            "WHERE r.age = :age AND r.weight = :weight AND e.gender.id = :gender_id AND e.muscle.id = :muscle_id")
    Optional<Exercise> findFirstByAgeAndWeightAndHeight(
            @Param("age") int age,
            @Param("weight") double weight,
            @Param("height") double height,
            @Param("gender_id") Long genderId,
            @Param("muscles_id") Long muscleId);
}