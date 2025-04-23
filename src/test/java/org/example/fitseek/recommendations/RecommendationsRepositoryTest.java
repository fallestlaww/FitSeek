package org.example.fitseek.recommendations;

import org.example.fitseek.model.*;
import org.example.fitseek.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.test.database.replace=NONE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.sql.init.mode=never"
})
public class RecommendationsRepositoryTest {
    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private MuscleRepository muscleRepository;
    @Autowired
    private DayRepository dayRepository;

    private Exercise exercise;
    private Gender gender;
    private Muscle muscle;
    private Day day;
    private Recommendation recommendation;
    private List<Recommendation> recommendations;

    @BeforeEach
    public void setup() {
        dayRepository.deleteAll();
        genderRepository.deleteAll();
        muscleRepository.deleteAll();
        exerciseRepository.deleteAll();
        recommendationRepository.deleteAll();

        exercise = new Exercise();
        gender = new Gender();
        muscle = new Muscle();
        day = new Day();
        recommendation = new Recommendation();

        gender.setName("Male");
        day.setName("Thursday");
        muscle.setName("Legs");

        exercise.setName("Test");
        exercise.setGender(gender);
        exercise.setDay(day);
        exercise.setMuscle(muscle);

        recommendation = new Recommendation();
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
        recommendationRepository.save(recommendation);
        genderRepository.save(gender);
        dayRepository.save(day);
        muscleRepository.save(muscle);
        exerciseRepository.save(exercise);
    }

    @Test
    public void getRecommendationByExerciseNameSuccess() {
        List<Recommendation> recommendationList = recommendationRepository.getRecommendationByExerciseName(exercise.getName());
        Assertions.assertEquals(recommendationList.getFirst(), recommendation);
    }

    @Test
    public void getRecommendationByExerciseNameFailure() {
        List<Recommendation> recommendationList = recommendationRepository.getRecommendationByExerciseName("wrongName");
        Assertions.assertTrue(recommendationList.isEmpty());
    }

    @Test
    public void getRecommendationByExerciseIdAndUserAgeAndUserWeightSuccess() {
        Recommendation actualRecommendation = recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                recommendation.getExercise().getId(),
                recommendation.getUserAge(),
                recommendation.getUserWeight()
        );
        Assertions.assertEquals(actualRecommendation, recommendation);
    }

    @Test
    public void getRecommendationByExerciseIdAndUserAgeAndUserWeighFailure() {
        Long wrongId = 9L;

        Recommendation actualRecommendation = recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                wrongId,
                recommendation.getUserAge(),
                recommendation.getUserWeight()
        );

        Assertions.assertNull(actualRecommendation);
    }
}
