package org.example.fitseek.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.model.Recommendation;
import org.example.fitseek.repository.ExerciseRepository;
import org.example.fitseek.repository.RecommendationRepository;
import org.example.fitseek.service.RecommendationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final ExerciseRepository exerciseRepository;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository, ExerciseRepository exerciseRepository) {
        this.recommendationRepository = recommendationRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Recommendation> readRecommendation(Long id) {
        log.debug("Reading recommendation for exercise {}", id);
        if(id == null) {
            log.warn("Recommendation is null or empty");
            throw new EntityNullException("Recommendation is null or empty");
        }
        Optional<Exercise> exerciseOptional = exerciseRepository.findById(id);
        Exercise exercise = exerciseOptional.orElseThrow(EntityNotFoundException::new);
        return recommendationRepository
                .getRecommendationByExerciseName(exercise.getName());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Recommendation updateRecommendation(RecommendationRequest recommendation) {
        log.debug("Updating recommendation: {}", Objects.toString(recommendation, "null"));
        if (recommendation == null) {
            log.warn("Requested recommendation is null or empty");
            throw new EntityNullException("Requested recommendation is null");
        }

        Exercise exercise = exerciseRepository.findById(recommendation.getExerciseId())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        log.info("Exercise: {}", exercise);
        Recommendation existingRecommendation = recommendationRepository
                .getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                        recommendation.getExerciseId(),
                        recommendation.getUserAge(),
                        recommendation.getUserWeight());

        if (existingRecommendation == null) {
            log.warn("Recommendation not found");
            throw new EntityNotFoundException("Recommendation not found");
        }

        if(existingRecommendation.getId() == null) {
            log.warn("Recommendation is null or empty");
            throw new EntityNullException("Recommendation is null or empty");
        }

        if (recommendation.getUserAge() != existingRecommendation.getUserAge()) {
            existingRecommendation.setUserAge(recommendation.getUserAge());
        }

        if (recommendation.getUserWeight() != existingRecommendation.getUserWeight()) {
            existingRecommendation.setUserWeight(recommendation.getUserWeight());
        }
        if (recommendation.getRecommendedRepeats() > 0) {
            existingRecommendation.setRecommendedRepeats(recommendation.getRecommendedRepeats());
        }
        if(recommendation.getRecommendedSets() > 0) {
            existingRecommendation.setRecommendedSets(recommendation.getRecommendedSets());
        }
        if(recommendation.getRecommendedWeightMin() > 0) {
            existingRecommendation.setRecommendedWeightMin(recommendation.getRecommendedWeightMin());
        }
        if(recommendation.getRecommendedWeightMax() > 0) {
            existingRecommendation.setRecommendedWeightMax(recommendation.getRecommendedWeightMax());
        }
        log.debug("Saving recommendation: {}", existingRecommendation);
        return recommendationRepository.save(existingRecommendation);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteRecommendation(RecommendationRequest recommendationRequest) {
        if(recommendationRequest == null) {
            log.warn("Requested recommendation is null");
            throw new EntityNullException("Requested recommendation is null");
        }
        Exercise exercise = exerciseRepository.findById(recommendationRequest.getExerciseId())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));
        log.info("Exercise: {}", exercise.toString());
        Recommendation existingRecommendation = recommendationRepository
                .getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                        recommendationRequest.getExerciseId(),
                        recommendationRequest.getUserAge(),
                        recommendationRequest.getUserWeight());
        if(existingRecommendation == null) {
            log.warn("Recommendation not found");
            throw new EntityNotFoundException("Recommendation not found");
        }
        log.info("Recommendation: {}", existingRecommendation);
        recommendationRepository.delete(existingRecommendation);
    }
}
