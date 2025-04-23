package org.example.fitseek.exercise;

import org.example.fitseek.model.*;
import org.example.fitseek.repository.DayRepository;
import org.example.fitseek.repository.ExerciseRepository;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.repository.MuscleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class ExerciseRepositoryTest {
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private MuscleRepository muscleRepository;
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private DayRepository dayRepository;
    private Exercise expectedExercise;
    private Gender gender;
    private Day day;
    private Muscle muscle;
    private Recommendation recommendation;

    @BeforeEach
    public void setup() {
        exerciseRepository.deleteAll();
        genderRepository.deleteAll();
        dayRepository.deleteAll();
        muscleRepository.deleteAll();

        gender = new Gender();
        gender.setName("Male");
        gender = genderRepository.save(gender);

        day = new Day();
        day.setName("Tuesday");
        day = dayRepository.save(day);

        muscle = new Muscle();
        muscle.setName("Back");
        muscle = muscleRepository.save(muscle);

        expectedExercise = new Exercise();
        expectedExercise.setName("Test Exercise");
        expectedExercise.setGender(gender);
        expectedExercise.setDay(day);
        expectedExercise.setMuscle(muscle);

        recommendation = new Recommendation();
        recommendation.setRecommendedSets(4);
        recommendation.setUserWeight(75.0);
        recommendation.setRecommendedRepeats(12);
        recommendation.setRecommendedWeightMin(4);
        recommendation.setRecommendedWeightMax(12);
        recommendation.setUserAge(25);
        recommendation.setExercise(expectedExercise);

        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(recommendation);
        expectedExercise.setRecommendationList(recommendations);

        expectedExercise = exerciseRepository.save(expectedExercise);
    }

    @Test
    public void findFirstByAgeAndWeightSuccess() {
        List<Exercise> actualExercise = exerciseRepository.findFirstByAgeAndWeight(
                expectedExercise.getRecommendationList().getFirst().getUserAge(),
                expectedExercise.getRecommendationList().getFirst().getUserWeight(),
                expectedExercise.getGender().getId(),
                expectedExercise.getMuscle().getId()
        );

        Assertions.assertEquals(actualExercise.size(), 1);
        Assertions.assertEquals(actualExercise.getFirst(), expectedExercise);
    }

    @Test
    public void findFirstByAgeAndWeightBadMuscleId() {
        List<Exercise> actualExercise = exerciseRepository.findFirstByAgeAndWeight(
                expectedExercise.getRecommendationList().getFirst().getUserAge(),
                expectedExercise.getRecommendationList().getFirst().getUserWeight(),
                expectedExercise.getGender().getId(),
                expectedExercise.getMuscle().getId() + 1
        );
        Assertions.assertTrue(actualExercise.isEmpty());
    }

    @Test
    public void findByGenderIdSuccess() {
        List<Exercise> actualExercise = exerciseRepository.findByGenderId(expectedExercise.getGender().getId());
        Assertions.assertEquals(actualExercise.size(), 1);
        Assertions.assertEquals(actualExercise.getFirst(), expectedExercise);
    }

    @Test
    public void findByGenderIdFailure() {
        List<Exercise> actualExercise = exerciseRepository.findByGenderId(expectedExercise.getGender().getId() + 1);
        Assertions.assertTrue(actualExercise.isEmpty());
    }

    @Test
    public void findByIdSuccess() {
        Optional<Exercise> actualExercise = exerciseRepository.findById(expectedExercise.getId());
        Assertions.assertTrue(actualExercise.isPresent());
        Assertions.assertEquals(actualExercise.get().getName(), expectedExercise.getName());
    }

    @Test
    public void findByIdFailure() {
        Optional<Exercise> actualExercise = exerciseRepository.findById(expectedExercise.getId() + 1);
        Assertions.assertFalse(actualExercise.isPresent());
    }

    @Test
    public void findByNameSuccess() {
        Exercise actualExercise = exerciseRepository.findByName(expectedExercise.getName());
        Assertions.assertEquals(actualExercise.getName(), expectedExercise.getName());
    }

    @Test
    public void findByNameFailure() {
        String wrongName = "Wrong name";
        Exercise actualExercise = exerciseRepository.findByName(wrongName);
        Assertions.assertNull(actualExercise);
    }
}
