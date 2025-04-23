package org.example.fitseek.recommendations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.controller.RecommendationController;
import org.example.fitseek.dto.request.RecommendationRequest;
import org.example.fitseek.model.*;
import org.example.fitseek.repository.*;
import org.example.fitseek.service.impl.ExerciseServiceImpl;
import org.example.fitseek.service.impl.RecommendationServiceImpl;
import org.example.fitseek.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = RecommendationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(RecommendationsControllerTest.MockConfig.class)
public class RecommendationsControllerTest {
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
    private MuscleRepository muscleRepository;
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ExerciseServiceImpl exerciseService;

    private Exercise exercise;
    private Gender gender;
    private Muscle muscle;
    private Day day;
    private Recommendation recommendation;
    private List<Recommendation> recommendations;
    private RecommendationRequest recommendationRequest;
    private Recommendation expectedRecommendation;

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
    }

    @AfterEach
    public void resetMocks() {
        Mockito.reset(
                recommendationService,
                recommendationRepository,
                exerciseRepository,
                userService,
                jwtUtils,
                genderRepository,
                dayRepository,
                muscleRepository
        );
    }

    @Test
    public void testGetRecommendationSuccessful() throws Exception {
        when(recommendationService.readRecommendation(exercise.getId())).thenReturn(List.of(recommendation));

        mockMvc.perform(get("/exercise/recommendation/{id}", exercise.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRecommendationFailed() throws Exception {
        Long wrongId = 9L;
        when(recommendationService.readRecommendation(wrongId)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/exercise/recommendation/{id}", wrongId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRecommendationSuccessful() throws Exception {
        int originalRecommendedSets = recommendation.getRecommendedSets();

        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                exercise.getId(), recommendationRequest.getUserAge(), recommendationRequest.getUserWeight()
        )).thenReturn(recommendation);
        when(recommendationService.updateRecommendation(any(RecommendationRequest.class))).thenReturn(expectedRecommendation);


        mockMvc.perform(put("/exercise/recommendation/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exercise_id").value(recommendationRequest.getExerciseId()))
                .andExpect(jsonPath("$.user_age").value(recommendationRequest.getUserAge()))
                .andExpect(jsonPath("$.user_weight").value(recommendationRequest.getUserWeight()))
                .andExpect(jsonPath("$.recommended_sets").value(recommendationRequest.getRecommendedSets()))
                .andExpect(jsonPath("$.recommended_weight_min").value(recommendationRequest.getRecommendedWeightMin()))
                .andExpect(jsonPath("$.recommended_weight_max").value(recommendationRequest.getRecommendedWeightMax()))
                .andExpect(jsonPath("$.recommended_repeats").value(recommendationRequest.getRecommendedRepeats()))
                .andDo(result -> {
                    Assertions.assertEquals(((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.exercise_id")).longValue(), recommendation.getExercise().getId());
                    Assertions.assertEquals(((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.recommended_sets")).intValue(), recommendationRequest.getRecommendedSets());
                    Assertions.assertEquals(((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.recommended_repeats")).intValue(), recommendationRequest.getRecommendedRepeats());

                    Assertions.assertNotEquals(((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.recommended_sets")).intValue(), originalRecommendedSets);
                });
    }

    @Test
    public void testUpdateRecommendationFailedWrongId() throws Exception {;
        when(exerciseRepository.findById(9L)).thenReturn(Optional.empty());
        when(recommendationService.updateRecommendation(any(RecommendationRequest.class))).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/exercise/recommendation/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRecommendationFailedWrongUserData() throws Exception {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                exercise.getId(), 111, recommendationRequest.getUserWeight()
        )).thenReturn(null);
        when(recommendationService.updateRecommendation(any(RecommendationRequest.class))).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/exercise/recommendation/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRecommendationSuccessful() throws Exception {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                exercise.getId(), recommendationRequest.getUserAge(), recommendationRequest.getUserWeight()
        )).thenReturn(recommendation);

        doNothing().when(recommendationService).deleteRecommendation(recommendationRequest);

        mockMvc.perform(delete("/exercise/recommendation/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteRecommendationFailedWrongId() throws Exception {
        Long wrongId = 9L;
        when(exerciseRepository.findById(wrongId)).thenReturn(Optional.empty());
        doThrow(EntityNotFoundException.class).when(recommendationService).deleteRecommendation(any(RecommendationRequest.class));

        mockMvc.perform(delete("/exercise/recommendation/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recommendationRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRecommendationFailedWrongUserData() throws Exception {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(recommendationRepository.getRecommendationByExerciseIdAndUserAgeAndUserWeight(
                exercise.getId(), 111, recommendationRequest.getUserWeight()
        )).thenReturn(null);
        doThrow(EntityNotFoundException.class).when(recommendationService).deleteRecommendation(any(RecommendationRequest.class));

        mockMvc.perform(delete("/exercise/recommendation/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recommendationRequest)))
                .andExpect(status().isNotFound());
    }
}
