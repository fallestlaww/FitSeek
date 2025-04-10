package org.example.fitseek.controller;

import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExercise(@PathVariable Long id) {
        Exercise exercise = exerciseService.readExercise(id);
        return ResponseEntity.ok().body(exercise);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateExercise(@RequestBody ExerciseRequest exerciseRequest) {
        exerciseService.updateExercise(exerciseRequest);
        return ResponseEntity.ok().body(exerciseRequest);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteExercise(@PathVariable String name) {
        exerciseService.deleteExercise(name);
        return ResponseEntity.ok().body(name);
    }
}
