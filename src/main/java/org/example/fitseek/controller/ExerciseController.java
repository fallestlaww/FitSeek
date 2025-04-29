package org.example.fitseek.controller;

import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>Info</h4>
 * REST-controller, responsible for processing CRUD-operations on {@link Exercise} entity
 * <h4>Fields</h4>
 * <li>{@link #exerciseService} object of {@link ExerciseService}, responsible for business logic to work with entity {@link Exercise}</li>
 *
 * @see ExerciseService
 */
@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    /**
     * Claims id from URL and gets all data by id from database, using {@link ExerciseService#readExercise(Long)}
     * @param id represents a requested exercise id in database from user
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and exercise data in response
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getExercise(@PathVariable Long id) {
        Exercise exercise = exerciseService.readExercise(id);
        return ResponseEntity.ok().body(exercise);
    }

    /**
     * Claims exercise data in object of {@link ExerciseRequest} class and update existing exercise using {@link ExerciseService#updateExercise(ExerciseRequest)}
     * @param exerciseRequest object with requested exercise data for updating from user
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and updated exercise data in response
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateExercise(@RequestBody ExerciseRequest exerciseRequest) {
        Exercise updatedExercise = exerciseService.updateExercise(exerciseRequest);
        return ResponseEntity.ok().body(updatedExercise);
    }

    /**
     * Claims exercise name from URL and deletes all that exercise data from database using {@link ExerciseService#deleteExercise(String)}
     * @param name represents a requested exercise name in database from user
     * @return {@link ResponseEntity} with a status code according to the result of the process execution
     */
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteExercise(@PathVariable String name) {
        exerciseService.deleteExercise(name);
        return ResponseEntity.ok().build();
    }
}
