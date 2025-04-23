package org.example.fitseek.trainingtype;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.controller.TrainingController;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.model.*;
import org.example.fitseek.repository.*;
import org.example.fitseek.service.impl.ExerciseServiceImpl;
import org.example.fitseek.service.impl.RecommendationServiceImpl;
import org.example.fitseek.service.impl.TrainingTypeServiceImpl;
import org.example.fitseek.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrainingController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(TrainingTypeControllerTest.MockConfig.class)
public class TrainingTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private RecommendationServiceImpl recommendationService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ExerciseServiceImpl exerciseService;
    @Autowired
    private TrainingTypeServiceImpl trainingTypeService;

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

    @TestConfiguration
    static class MockConfig {
        @Bean
        public RecommendationServiceImpl recommendationService() {
            return Mockito.mock(RecommendationServiceImpl.class);
        }

        @Bean
        public RecommendationRepository recommendationRepository() {
            return Mockito.mock(RecommendationRepository.class);
        }

        @Bean
        public UserServiceImpl userService() {
            return Mockito.mock(UserServiceImpl.class);
        }

        @Bean
        public JwtUtils jwtUtils() {
            return Mockito.mock(JwtUtils.class);
        }

        @Bean
        public GenderRepository genderRepository() {
            return Mockito.mock(GenderRepository.class);
        }

        @Bean
        public DayRepository dayRepository() {
            return Mockito.mock(DayRepository.class);
        }

        @Bean
        public ExerciseRepository exerciseRepository() {
            return Mockito.mock(ExerciseRepository.class);
        }

        @Bean
        public ExerciseServiceImpl exerciseService() {
            return Mockito.mock(ExerciseServiceImpl.class);
        }

        @Bean
        public MuscleRepository muscleRepository() {
            return Mockito.mock(MuscleRepository.class);
        }

        @Bean
        public TrainingTypeServiceImpl trainingTypeService() {
            return Mockito.mock(TrainingTypeServiceImpl.class);
        }
    }

    @BeforeEach
    public void setup() {
        exercise = new Exercise();
        gender = new Gender();
        muscle = new Muscle();
        day = new Day();
        recommendation = new Recommendation();

        gender.setId(1L);
        day.setId(1L);
        muscle.setId(1L);
        gender.setName("Male");
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
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(
                recommendationRepository,
                recommendationService,
                userService,
                jwtUtils,
                exerciseRepository,
                exerciseService,
                trainingTypeService
        );
    }

    @Test
    public void trainingTypeInformationSuccess() throws Exception {
       mockMvc.perform(post("/training-type/information")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(trainingTypeRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value(trainingType.getName()));
    }

    @Test
    public void trainingTypeInformationFailureWrongName() throws Exception {
        trainingTypeRequest.setName("WrongName");
        mockMvc.perform(post("/training-type/information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingTypeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trainingTypeExercisesSuccess() throws Exception {
        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setName("Female");
        UserInformationRequest userInformationRequest = new UserInformationRequest();
        userInformationRequest.setAge(25);
        userInformationRequest.setWeight(80.0);
        userInformationRequest.setGender(genderRequest);
        userInformationRequest.setTrainingType(trainingTypeRequest);

        when(trainingTypeService.trainingTypeExercises(any(UserInformationRequest.class)))
                .thenReturn(List.of(exercise));

        mockMvc.perform(post("/training-type/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInformationRequest)))
                .andExpect(jsonPath("$[0].name").value(exercise.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void trainingTypeExerciseFailureRequestNull() throws Exception {
        when(trainingTypeService.trainingTypeExercises(null)).thenThrow(InvalidEntityException.class);

        mockMvc.perform(post("/training-type/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trainingTypeExerciseFailureWrongName() throws Exception {
        trainingTypeRequest.setName("WrongName");
        when(trainingTypeService.trainingTypeExercises(any(UserInformationRequest.class))).thenThrow(InvalidEntityException.class);

        mockMvc.perform(post("/training-type/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInformationRequest)))
                .andExpect(status().isBadRequest());
    }
}
