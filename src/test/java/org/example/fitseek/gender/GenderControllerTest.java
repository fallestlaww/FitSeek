package org.example.fitseek.gender;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.controller.GenderController;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.model.*;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.service.impl.ExerciseServiceImpl;
import org.example.fitseek.service.impl.GenderServiceImpl;
import org.example.fitseek.service.impl.RecommendationServiceImpl;
import org.example.fitseek.service.impl.UserServiceImpl;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GenderController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(GenderControllerTest.MockConfig.class)
public class GenderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GenderServiceImpl genderService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private GenderRepository genderRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ExerciseServiceImpl exerciseService;
    @Autowired
    private RecommendationServiceImpl recommendationService;
    @Autowired
    private ObjectMapper objectMapper;

    private GenderRequest genderRequest;
    private Gender gender;
    private Exercise exercise;
    private Recommendation recommendation;
    private Muscle muscle;
    private Day day;
    private List<Recommendation> recommendations;


    @TestConfiguration
    static class MockConfig {
        @Bean
        public GenderServiceImpl genderService() {
            return Mockito.mock(GenderServiceImpl.class);
        }

        @Bean
        public JwtUtils jwtUtils() {
            return Mockito.mock(JwtUtils.class);
        }

        @Bean
        public UserServiceImpl userService() {
            return Mockito.mock(UserServiceImpl.class);
        }

        @Bean
        public GenderRepository genderRepository() {
            return Mockito.mock(GenderRepository.class);
        }

        @Bean
        public ExerciseServiceImpl exerciseService() {
            return Mockito.mock(ExerciseServiceImpl.class);
        }

        @Bean
        public RecommendationServiceImpl recommendationService() {
            return Mockito.mock(RecommendationServiceImpl.class);
        }
    }

    @BeforeEach
    public void setUp() {
        genderRequest = new GenderRequest();
        genderRequest.setName("Female");

        gender = new Gender();
        gender.setName("Female");
        gender.setId(1L);

        exercise = new Exercise();
        exercise.setName("Exercise 1");
        exercise.setId(1L);
        exercise.setGender(gender);
        recommendation = new Recommendation();
        recommendation.setExercise(exercise);
        recommendation.setRecommendedSets(1);
        recommendation.setRecommendedRepeats(12);
        recommendation.setUserAge(20);
        recommendation.setUserWeight(70.0);
        recommendation.setRecommendedWeightMin(4);
        recommendation.setRecommendedWeightMax(8);

        recommendations = new ArrayList<>();
        recommendations.add(recommendation);
        exercise.setRecommendationList(recommendations);

        muscle = new Muscle();
        muscle.setName("Chest");
        muscle.setId(1L);

        day = new Day();
        day.setName("Friday");
        day.setId(1L);

        exercise.setDay(day);
        exercise.setMuscle(muscle);
    }

    @Test
    public void selectGenderSuccess() throws Exception {
        when(genderService.getExerciseByGender(any(GenderRequest.class))).thenReturn(gender);
        when(exerciseService.exerciseListForGender(gender.getId())).thenReturn(List.of(exercise));

        mockMvc.perform(post("/gender")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(exercise.getName()))
                .andExpect(jsonPath("$[0].gender").value(genderRequest.getName()));
    }

    @Test
    public void selectGenderFailureBadGenderName() throws Exception {
        when(genderService.getExerciseByGender(any(GenderRequest.class))).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/gender")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genderRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void selectGenderFailureRequestNull() throws Exception {
        when(genderService.getExerciseByGender(null)).thenThrow(EntityNullException.class);

        mockMvc.perform(post("/gender")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genderRequest)))
                .andExpect(status().isBadRequest());
    }
}
