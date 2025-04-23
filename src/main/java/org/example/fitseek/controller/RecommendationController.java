package org.example.fitseek.controller;

import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.model.Recommendation;
import org.example.fitseek.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/exercise/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecommendation(@PathVariable Long id) {
        List<Recommendation> recommendationList = recommendationService.readRecommendation(id);
        return ResponseEntity.ok().body(recommendationList);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRecommendation(@RequestBody RecommendationRequest recommendation) {
        recommendationService.updateRecommendation(recommendation);
        return ResponseEntity.ok().body(recommendation);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRecommendation(@RequestBody RecommendationRequest recommendation) {
        recommendationService.deleteRecommendation(recommendation);
        return ResponseEntity.ok().body(recommendationService.readRecommendation(recommendation.getExerciseId()));
    }
}
