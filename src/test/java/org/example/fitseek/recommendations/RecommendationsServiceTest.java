package org.example.fitseek.recommendations;

import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.model.*;
import org.example.fitseek.repository.ExerciseRepository;
import org.example.fitseek.repository.RecommendationRepository;
import org.example.fitseek.service.impl.RecommendationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationsServiceTest {
    @InjectMocks
    private RecommendationServiceImpl recommendationService;
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private ExerciseRepository exerciseRepository;

    private Exercise exercise;
    private Gender gender;
    private Muscle muscle;
    private Day day;
    private Recommendation recommendation;
    private List<Recommendation> recommendations;

    @BeforeEach
    public void setup() {
        exercise = new Exercise();
        gender = new Gender();
        muscle = new Muscle();
        day = new Day();
        recommendation = new Recommendation();

        gender.setName("Male");
        day.setName("Thursday");
        muscle.setName("Legs");

        exercise.setId(1L);
        exercise.setName("Test");
        exercise.setGender(gender);
        exercise.setDay(day);
        exercise.setMuscle(muscle);

        recommendation = new Recommendation();
        recommendation.setId(1L);
        recommendation.setExercise(exercise);
        recommendation.setUserAge(25);
        recommendation.setUserWeight(80.0);
        recommendation.setRecommendedRepeats(12);
        recommendation.setRecommendedSets(4);
        recommendation.setRecommendedWeightMin(4);
        recommendation.setRecommendedWeightMax(12);

        recommendations = new ArrayList<>();
        recommendations.add(recommendation);
        exercise.setRecommendationList(recommendations);
    }

    @Test
    public void testReadRecommendationSuccess() {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseName(exercise.getName())).thenReturn(recommendations);

        List<Recommendation> actualRecommendations = recommendationService.readRecommendation(exercise.getId());
        Assertions.assertEquals(actualRecommendations.getFirst(), recommendation);
    }

    @Test
    public void testReadRecommendationFailure() {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> recommendationService.readRecommendation(exercise.getId()));
    }

    @Test
    public void testUpdateRecommendationSuccess() {
        int previousRecommendedSets = exercise.getRecommendationList().getFirst().getRecommendedSets();

        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setExerciseId(1L);
        recommendationRequest.setUserAge(25);
        recommendationRequest.setUserWeight(80.0);
        recommendationRequest.setRecommendedSets(6);
        recommendationRequest.setRecommendedWeightMin(4);
        recommendationRequest.setRecommendedWeightMax(12);
        recommendationRequest.setRecommendedRepeats(12);

        Recommendation expectedRecommendation = new Recommendation();
        expectedRecommendation.setExercise(exercise);
        expectedRecommendation.setUserAge(recommendationRequest.getUserAge());
        expectedRecommendation.setUserWeight(recommendationRequest.getUserWeight());
        expectedRecommendation.setRecommendedSets(recommendationRequest.getRecommendedSets());
        expectedRecommendation.setRecommendedWeightMin(recommendationRequest.getRecommendedWeightMin());
        expectedRecommendation.setRecommendedWeightMax(recommendationRequest.getRecommendedWeightMax());
        expectedRecommendation.setRecommendedRepeats(recommendationRequest.getRecommendedRepeats());

        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                exercise.getRecommendationList().getFirst().getId(),
                exercise.getRecommendationList().getFirst().getUserAge(),
                exercise.getRecommendationList().getFirst().getUserWeight()
        )).thenReturn(recommendation);
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(expectedRecommendation);

        Recommendation actualRecommendation = recommendationService.updateRecommendation(recommendationRequest);

        Assertions.assertNotNull(actualRecommendation);
        Assertions.assertEquals(actualRecommendation.getId(), expectedRecommendation.getId());
        Assertions.assertEquals(actualRecommendation.getRecommendedRepeats(), recommendationRequest.getRecommendedRepeats());
        Assertions.assertNotEquals(actualRecommendation.getRecommendedSets(), previousRecommendedSets);
    }

    @Test
    public void testUpdateRecommendationFailureWrongId() {
        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setExerciseId(1L);
        recommendationRequest.setUserAge(25);
        recommendationRequest.setUserWeight(80.0);
        recommendationRequest.setRecommendedSets(6);
        recommendationRequest.setRecommendedWeightMin(4);
        recommendationRequest.setRecommendedWeightMax(12);
        recommendationRequest.setRecommendedRepeats(12);

        when(exerciseRepository.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> recommendationService.updateRecommendation(recommendationRequest));
    }

    @Test
    public void testUpdateRecommendationFailureWrongUserData() {
        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setExerciseId(1L);
        recommendationRequest.setUserAge(25);
        recommendationRequest.setUserWeight(80.0);
        recommendationRequest.setRecommendedSets(6);
        recommendationRequest.setRecommendedWeightMin(4);
        recommendationRequest.setRecommendedWeightMax(12);
        recommendationRequest.setRecommendedRepeats(12);

        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                anyLong(),
                anyInt(),
                anyDouble()
        )).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> recommendationService.updateRecommendation(recommendationRequest));
    }

    @Test
    public void testDeleteRecommendationSuccess() {
        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setExerciseId(1L);
        recommendationRequest.setUserAge(exercise.getRecommendationList().getFirst().getUserAge());
        recommendationRequest.setUserWeight(exercise.getRecommendationList().getFirst().getUserWeight());
        recommendationRequest.setRecommendedSets(exercise.getRecommendationList().getFirst().getRecommendedSets());
        recommendationRequest.setRecommendedWeightMin(exercise.getRecommendationList().getFirst().getRecommendedWeightMin());
        recommendationRequest.setRecommendedWeightMax(exercise.getRecommendationList().getFirst().getRecommendedWeightMax());
        recommendationRequest.setRecommendedRepeats(exercise.getRecommendationList().getFirst().getRecommendedRepeats());

        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                recommendationRequest.getExerciseId(),
                recommendationRequest.getUserAge(),
                recommendationRequest.getUserWeight()
        )).thenReturn(recommendation);

        recommendationService.deleteRecommendation(recommendationRequest);
        verify(recommendationRepository).delete(recommendation);
    }

    @Test
    public void testDeleteRecommendationFailureWrongId() {
        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setExerciseId(1L);
        recommendationRequest.setUserAge(exercise.getRecommendationList().getFirst().getUserAge());
        recommendationRequest.setUserWeight(exercise.getRecommendationList().getFirst().getUserWeight());
        recommendationRequest.setRecommendedSets(exercise.getRecommendationList().getFirst().getRecommendedSets());
        recommendationRequest.setRecommendedWeightMin(exercise.getRecommendationList().getFirst().getRecommendedWeightMin());
        recommendationRequest.setRecommendedWeightMax(exercise.getRecommendationList().getFirst().getRecommendedWeightMax());
        recommendationRequest.setRecommendedRepeats(exercise.getRecommendationList().getFirst().getRecommendedRepeats());

        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> recommendationService.deleteRecommendation(recommendationRequest));
    }
}
