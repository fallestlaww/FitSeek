package org.example.fitseek.repository;

import org.example.fitseek.dto.response.ExerciseResponse;
import org.example.fitseek.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    @Query("SELECT e.name, r.recommended_approaches, r.recommended_repeats, r.recommended_weight, e.musles_id, e.day_id, e.gender_id) " +
            "FROM exercises e " +
            "JOIN recommendations r ON r.exercise_id = e.id " +
            "WHERE r.age = :age AND r.weight = :weight AND e.gender_id = :gender_id AND e.muscle_id = :muscle_id")
    Optional<Exercise> findFirstByAgeAndWeightAndHeight(
            @Param("age") int age,
            @Param("weight") double weight,
            @Param("height") double height,
            @Param("gender_id") int genderId,
            @Param("muscle_id") int muscleId);
}