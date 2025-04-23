package org.example.fitseek.exercise;

import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.dto.request.DayRequest;
import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.request.MuscleRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.model.*;
import org.example.fitseek.repository.*;
import org.example.fitseek.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest  {
    @InjectMocks
    private ExerciseServiceImpl exerciseService;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private MuscleRepository muscleRepository;
    @Mock
    private GenderRepository genderRepository;
    @Mock
    private DayRepository dayRepository;

    private Exercise exercise;
    private Muscle muscle;
    private Gender gender;
    private Day day;
    private List<Recommendation> recommendations;

    @BeforeEach
    public void setUp() {
        exercise = new Exercise();
        muscle = new Muscle();
        gender = new Gender();
        day = new Day();

        gender.setId(1L);
        gender.setName("Male");
        day.setName("Monday");
        muscle.setName("Chest");
        muscle.setId(1L);

        exercise.setId(1L);
        exercise.setName("Test");
        exercise.setMuscle(muscle);
        exercise.setGender(gender);
        exercise.setDay(day);

        recommendations = new ArrayList<>();
        Recommendation recommendation = new Recommendation();
        recommendation.setExercise(exercise);
        recommendation.setRecommendedRepeats(12);
        recommendation.setRecommendedSets(4);
        recommendation.setRecommendedWeightMin(4);
        recommendation.setRecommendedWeightMax(12);
        recommendation.setUserAge(25);
        recommendation.setUserWeight(75.0);
        recommendations.add(recommendation);
        exercise.setRecommendationList(recommendations);
    }

    @Test
    public void testExerciseListSuccess() {
        when(exerciseRepository.findFirstByAgeAndWeight(
                anyInt(),
                anyDouble(),
                anyLong(),
                anyLong()
        )).thenReturn(List.of(exercise));

        List<Exercise> exercises = exerciseService.exerciseListForSplit(
                exercise.getRecommendationList().getFirst().getUserAge(),
                exercise.getRecommendationList().getFirst().getUserWeight(),
                exercise.getGender().getId()
        );

        assertNotNull(exercises);
        assertEquals(exercises.size(), 1);
        assertEquals(exercises.getFirst(), exercise);
    }

    @Test
    public void testExerciseListFailure() {
        when(exerciseRepository.findFirstByAgeAndWeight(
                anyInt(),
                anyDouble(),
                anyLong(),
                anyLong()
        )).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> {
           exerciseService.exerciseListForSplit(
                   exercise.getRecommendationList().getFirst().getUserAge(),
                   exercise.getRecommendationList().getFirst().getUserWeight(),
                   exercise.getGender().getId()
           );
        });
    }

    @Test
    public void testExerciseListForGenderSuccess() {
        when(exerciseRepository.findByGenderId(anyLong())).thenReturn(List.of(exercise));
        List<Exercise> exercises = exerciseService.exerciseListForGender(gender.getId());
        assertNotNull(exercises);
        assertNotNull(exercises.getFirst());
        assertEquals(exercises.size(), 1);
    }

    @Test
    public void testExerciseListForGenderFailure() {
        when(exerciseRepository.findByGenderId(anyLong())).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> {
            exerciseService.exerciseListForGender(gender.getId());
        });
    }

    @Test
    public void testReadExerciseSuccess() {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));

        Exercise exerciseRead = exerciseService.readExercise(exercise.getId());
        assertNotNull(exerciseRead);
        assertEquals(exerciseRead.getId(), exercise.getId());
        assertEquals(exerciseRead.getName(), exercise.getName());
    }

    @Test
    public void testReadExerciseFailure() {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            Exercise exerciseRead = exerciseService.readExercise(exercise.getId());
        });
    }

    @Test
    public void testReadExerciseFailureIdNull() {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            Exercise exerciseRead = exerciseService.readExercise(exercise.getId());
        });
    }

    @Test
    public void testUpdateExerciseSuccess() {
        ExerciseRequest exerciseRequest = new ExerciseRequest();
        GenderRequest genderRequest = new GenderRequest();
        MuscleRequest muscleRequest = new MuscleRequest();
        DayRequest dayRequest = new DayRequest();

        genderRequest.setName("Female");
        muscleRequest.setName("Legs");
        dayRequest.setName("Saturday");

        exerciseRequest.setGender(genderRequest);
        exerciseRequest.setMuscle(muscleRequest);
        exerciseRequest.setDay(dayRequest);

        gender.setName("Female");
        muscle.setName("Legs");
        day.setName("Saturday");

        when(exerciseRepository.findByName(exerciseRequest.getName())).thenReturn(exercise);
        when(muscleRepository.findByName(muscleRequest.getName())).thenReturn(muscle);
        when(genderRepository.findByName(genderRequest.getName())).thenReturn(gender);
        when(dayRepository.findByName(dayRequest.getName())).thenReturn(day);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        Exercise updated = exerciseService.updateExercise(exerciseRequest);
        assertNotNull(updated);
        assertEquals(updated.getId(), exercise.getId());
        assertEquals(updated.getName(), exercise.getName());
        assertEquals(updated.getGender().getName(), genderRequest.getName());
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    public void testUpdateExerciseFailureInvalidName() {
        ExerciseRequest exerciseRequest = new ExerciseRequest();
        MuscleRequest muscleRequest = new MuscleRequest();

        exerciseRequest.setName("Wrong");
        muscleRequest.setName("Wrong");

        exerciseRequest.setMuscle(muscleRequest);

        when(exerciseRepository.findByName(exerciseRequest.getName())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            Exercise updated = exerciseService.updateExercise(exerciseRequest);
        });
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    public void testUpdateExerciseFailureInvalidMuscle() {
        ExerciseRequest exerciseRequest = new ExerciseRequest();
        MuscleRequest muscleRequest = new MuscleRequest();

        muscleRequest.setName("Wrong");

        exerciseRequest.setMuscle(muscleRequest);

        gender.setName("Wrong");

        when(exerciseRepository.findByName(exerciseRequest.getName())).thenReturn(exercise);
        when(muscleRepository.findByName(muscleRequest.getName())).thenThrow(InvalidEntityException.class);

        assertThrows(InvalidEntityException.class, () -> {
            Exercise updated = exerciseService.updateExercise(exerciseRequest);
        });
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    public void deleteExerciseSuccess() {
        exerciseService.deleteExercise(exercise.getName());
        verify(exerciseRepository).deleteExerciseByName(exercise.getName());
    }

    @Test
    public void deleteExerciseFailure() {
        exercise.setName(null);
        assertThrows(EntityNullException.class, () -> {
            exerciseService.deleteExercise(exercise.getName());
        });
        verify(exerciseRepository, never()).deleteExerciseByName(exercise.getName());
    }
}
