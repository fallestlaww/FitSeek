package org.example.fitseek.trainingtype;

import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.exception.exceptions.InvalidRequestException;
import org.example.fitseek.model.*;
import org.example.fitseek.service.impl.ExerciseServiceImpl;
import org.example.fitseek.service.impl.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;
    @Mock
    private ExerciseServiceImpl exerciseService;

    private Exercise exercise;
    private Gender gender;
    private Muscle muscle;
    private Day day;
    private Recommendation recommendation;
    private List<Recommendation> recommendations;
    private RecommendationRequest recommendationRequest;
    private Recommendation expectedRecommendation;
    private TrainingType trainingType;
    private TrainingTypeRequest trainingTypeRequest;
    private GenderRequest genderRequest;
    private UserInformationRequest userInformationRequest;

    @BeforeEach
    public void setup() {
        exercise = new Exercise();
        gender = new Gender();
        muscle = new Muscle();
        day = new Day();
        recommendation = new Recommendation();

        gender.setId(2L);
        day.setId(1L);
        muscle.setId(1L);
        gender.setName("Female");
        day.setName("Thursday");
        muscle.setName("Legs");

        exercise.setId(1L);
        exercise.setName("Test");
        exercise.setGender(gender);
        exercise.setDay(day);
        exercise.setMuscle(muscle);

        recommendation = new Recommendation();
        recommendation.setExercise(exercise);
        recommendation.setId(1L);
        recommendation.setUserAge(25);
        recommendation.setUserWeight(80.0);
        recommendation.setRecommendedRepeats(12);
        recommendation.setRecommendedSets(4);
        recommendation.setRecommendedWeightMin(4);
        recommendation.setRecommendedWeightMax(12);

        recommendations = new ArrayList<>();
        recommendations.add(recommendation);
        exercise.setRecommendationList(recommendations);

        recommendationRequest = new RecommendationRequest();
        recommendationRequest.setExerciseId(1L);
        recommendationRequest.setUserAge(25);
        recommendationRequest.setUserWeight(80.0);
        recommendationRequest.setRecommendedSets(6);
        recommendationRequest.setRecommendedWeightMin(4);
        recommendationRequest.setRecommendedWeightMax(12);
        recommendationRequest.setRecommendedRepeats(12);

        expectedRecommendation = new Recommendation();
        expectedRecommendation.setExercise(exercise);
        expectedRecommendation.setUserAge(recommendationRequest.getUserAge());
        expectedRecommendation.setUserWeight(recommendationRequest.getUserWeight());
        expectedRecommendation.setRecommendedSets(recommendationRequest.getRecommendedSets());
        expectedRecommendation.setRecommendedWeightMin(recommendationRequest.getRecommendedWeightMin());
        expectedRecommendation.setRecommendedWeightMax(recommendationRequest.getRecommendedWeightMax());
        expectedRecommendation.setRecommendedRepeats(recommendationRequest.getRecommendedRepeats());

        trainingType = new TrainingType();
        trainingType.setName("Split");
        trainingTypeRequest = new TrainingTypeRequest();
        trainingTypeRequest.setName("Split");

        genderRequest = new GenderRequest();
        genderRequest.setName("Female");
        userInformationRequest = new UserInformationRequest();
        userInformationRequest.setAge(25);
        userInformationRequest.setWeight(80.0);
        userInformationRequest.setGender(genderRequest);
        userInformationRequest.setTrainingType(trainingTypeRequest);
    }

    @Test
    public void trainingTypeExercisesSuccess() {
        when(exerciseService.exerciseListForSplit(
                userInformationRequest.getAge(),
                userInformationRequest.getWeight(),
                exercise.getGender().getId()
        )).thenReturn(List.of(exercise));

        List<Exercise> exercises = trainingTypeService.trainingTypeExercises(userInformationRequest);

        Assertions.assertEquals(exercises.getFirst(), exercise);
        Assertions.assertEquals(exercises.size(), 1);
    }

    @Test
    public void trainingTypeExercisesFailUserRequestNull() {
        userInformationRequest = null;

        Assertions.assertThrows(InvalidRequestException.class, () -> trainingTypeService.trainingTypeExercises(userInformationRequest));
    }

    @Test
    public void trainingTypeExercisesFailWrongTrainingType() {
        trainingTypeRequest.setName("Wrong");
        userInformationRequest.setTrainingType(trainingTypeRequest);

        Assertions.assertThrows(InvalidEntityException.class, () -> trainingTypeService.trainingTypeExercises(userInformationRequest));
    }
}
