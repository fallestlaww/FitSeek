package org.example.fitseek.repository;

import org.example.fitseek.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <h4>Info</h4>
 * Repository for {@link Exercise} entity, provides CRUD operations and custom query methods for working with {@code exercises} table in database
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    /**
     * Returns list of {@link Exercise} objects founded in database by user age, user weight, gender id and muscle id
     * @param age given user age for search
     * @param weight given user weight for search
     * @param genderId given gender id for search
     * @param muscleId given muscle id for search
     * @return list of {@link Exercise} objects
     */
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
    /**
     * Returns list of {@link Exercise} objects founded in database by gender id
     * @param genderId given gender id for search
     * @return list of {@link Exercise} objects
     */
    List<Exercise> findByGenderId(Long genderId);
    /**
     * Returns {@link Exercise} object founded in database by name
     * @param name given name for search
     * @return {@link Exercise} object
     */
    Exercise findByName(String name);
    /**
     * Returns optional of {@link Exercise} object founded in database by id
     * @param id given id for search
     * @return optional of {@link Exercise} object
     */
    Optional<Exercise> findById(Long id);

    /**
     * Removing {@link Exercise} object founded by name from database
     * @param name given name for search
     */
    void deleteExerciseByName(String name);
}