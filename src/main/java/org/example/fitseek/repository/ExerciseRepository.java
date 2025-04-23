package org.example.fitseek.repository;

import org.example.fitseek.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query(value = "SELECT e.*, r.recommended_sets, r.recommended_repeats, " +
            "r.recommended_weight_min, r.recommended_weight_max FROM exercises e " +
            "JOIN recommendations r ON r.exercise_id = e.id " +
            "WHERE r.user_age = :age AND r.user_weight = :weight " +
            "AND e.gender_id = :gender_id AND e.muscles_id = :muscle_id ",
            nativeQuery = true)
    List<Exercise> findFirstByAgeAndWeight(
            @Param("age") int age,
            @Param("weight") double weight,
            @Param("gender_id") Long genderId,
            @Param("muscle_id") Long muscleId);
    List<Exercise> findByGenderId(Long genderId);
    Exercise findByName(String name);
    Optional<Exercise> findById(Long id);
    void deleteExerciseByName(String name);
}