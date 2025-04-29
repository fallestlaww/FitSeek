package org.example.fitseek.controller;

import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.model.Recommendation;
import org.example.fitseek.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**<h4>Info</h4>
 * REST-controller, responsible for processing CRUD-operations on {@link Recommendation} entity
 * <h4>Fields</h4>
 * {@link #recommendationService} - object of {@link RecommendationService}, responsible for business logic to work with {@link Recommendation} entity.
 *
 * @see RecommendationService
 */
@RestController
@RequestMapping("/exercise/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Claims id from URL and gets all data by id from database, using {@link RecommendationService#readRecommendation(Long)}
     * @param exerciseId represents an id of exercise for which we find recommendations in database
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and recommendation data in response
     */
    @GetMapping("/{exerciseId}")
    public ResponseEntity<?> getRecommendation(@PathVariable Long exerciseId) {
        List<Recommendation> recommendationList = recommendationService.readRecommendation(exerciseId);
        return ResponseEntity.ok().body(recommendationList);
    }

    /**
     * Claims recommendation data in object of {@link RecommendationRequest} class and update existing recommendation using {@link RecommendationService#updateRecommendation(RecommendationRequest)}
     * @param recommendation object with requested recommendation data for updating from user
     * @return {@link ResponseEntity} with a status code according to the result of the process execution and updated recommendation data in response
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateRecommendation(@RequestBody RecommendationRequest recommendation) {
        Recommendation updatedRecommendation = recommendationService.updateRecommendation(recommendation);
        return ResponseEntity.ok().body(updatedRecommendation);
    }

    /**
     * Claims recommendation data in object of {@link RecommendationRequest} class and deletes all that recommendation data from database using {@link RecommendationService#deleteRecommendation(RecommendationRequest)}
     * @param recommendation represents a requested recommendation data for deleting from user
     * @return {@link ResponseEntity} with a status code according to the result of the process execution
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRecommendation(@RequestBody RecommendationRequest recommendation) {
        recommendationService.deleteRecommendation(recommendation);
        return ResponseEntity.ok().body(recommendationService.readRecommendation(recommendation.getExerciseId()));
    }
}
