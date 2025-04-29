package org.example.fitseek.service;

import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.model.Exercise;

import java.util.List;

/**
 * <h4>Info</h4>
 * Realization of service layer for {@link Exercise} entity.
 * Provides CRUD operations and operations for retrieving entity object based on user parameters
 */
public interface ExerciseService {
    /**
     * Returns list of {@link Exercise} entity for {@link org.example.fitseek.model.TrainingType} with name "Split" based on inputted user age, user weight and gender id
     * @param age given user age for sorting and returning appropriate recommendations for exercise
     * @param weight given user weight for sorting and returning appropriate recommendations for exercise
     * @param genderId given gender id for sorting and returning appropriate exercise
     * @return list of exercise according to inputted age, weight and gender
     */
    List<Exercise> exerciseListForSplit(int age, double weight, Long genderId);
    /**
     * Returns list of {@link Exercise} entity for {@link org.example.fitseek.model.TrainingType} with name "FullBody" based on inputted user age, user weight and gender id
     * @param age given user age for sorting and returning appropriate recommendations for exercise
     * @param weight given user weight for sorting and returning appropriate recommendations for exercise
     * @param genderId given gender id for sorting and returning appropriate exercise
     * @return list of exercise according to inputted age, weight and gender
     */
    List<Exercise> exerciseListForFullBody(int age, double weight,  Long genderId);
    /**
     * Returns list of {@link Exercise} entity based on inputted gender id
     * @param genderId given gender id for sorting and returning appropriate exercise
     * @return list of exercise according to inputted gender
     */
    List<Exercise> exerciseListForGender(Long genderId);

    /**
     * Returns information about exercise based on exercise id
     * @param id give exercise id
     * @return information about exercise
     */
    Exercise readExercise(Long id);
    /**
     * Updates the information provided in the request in the exercise found using the request also.
     * @param exerciseRequest object of {@link ExerciseRequest} which include all needed information
     * @return updated {@link Exercise} object
     */
    Exercise updateExercise(ExerciseRequest exerciseRequest);

    /**
     * Deletes {@link Exercise} entity found by its name.
     * @param name given {@link Exercise} entity name
     */
    void deleteExercise(String name);
}
