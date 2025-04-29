package org.example.fitseek.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.model.Recommendation;
import org.example.fitseek.repository.ExerciseRepository;
import org.example.fitseek.repository.RecommendationRepository;
import org.example.fitseek.service.ExerciseService;
import org.example.fitseek.service.RecommendationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of service layer {@link RecommendationService} for {@link Recommendation} entity.
 * Provides logic for managing {@link RecommendationService},
 * including role-based access control for sensitive methods, namely {@link #updateRecommendation(RecommendationRequest)},
 * {@link #deleteRecommendation(RecommendationRequest)}
 */
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

        /*
        find in database recommendation for exercise by exercise id, user age and user weight got from request, 
        without existing recommendation for exercise code can't update anything 
         */
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
        
        //if user age from request equals not-updated recommendation user age -> do not update recommendation
        if (recommendation.getUserAge() != existingRecommendation.getUserAge()) {
            existingRecommendation.setUserAge(recommendation.getUserAge());
        }
        //if user weight from request equals not-updated recommendation user weight -> do not update recommendation
        if (recommendation.getUserWeight() != existingRecommendation.getUserWeight()) {
            existingRecommendation.setUserWeight(recommendation.getUserWeight());
        }
        //if recommended repeats from request equals not-updated recommendation recommended repeats -> do not update recommendation
        if (recommendation.getRecommendedRepeats() != existingRecommendation.getRecommendedRepeats()) {
            existingRecommendation.setRecommendedRepeats(recommendation.getRecommendedRepeats());
        }
        //if recommended sets from request equals not-updated recommendation recommended sets -> do not update recommendation
        if(recommendation.getRecommendedSets() != existingRecommendation.getRecommendedSets()) {
            existingRecommendation.setRecommendedSets(recommendation.getRecommendedSets());
        }
        //if recommended min weight from request equals not-updated recommendation recommended min weight -> do not update recommendation
        if(recommendation.getRecommendedWeightMin() != existingRecommendation.getRecommendedWeightMin()) {
            existingRecommendation.setRecommendedWeightMin(recommendation.getRecommendedWeightMin());
        }
        //if recommended max weight from request equals not-updated recommendation recommended max weight -> do not update recommendation
        if(recommendation.getRecommendedWeightMax() != existingRecommendation.getRecommendedWeightMax()) {
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
        /*
        find in database recommendation for exercise by exercise id, user age and user weight got from request, 
        without existing recommendation for exercise code can't delete anything 
         */
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
