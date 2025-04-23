package org.example.fitseek.exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.controller.ExerciseController;
import org.example.fitseek.dto.request.DayRequest;
import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.dto.request.MuscleRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.model.*;
import org.example.fitseek.repository.*;
import org.example.fitseek.service.impl.ExerciseServiceImpl;
import org.example.fitseek.service.impl.UserServiceImpl;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ExerciseController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(ExerciseControllerTest.MockConfig.class)
public class ExerciseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExerciseServiceImpl exerciseService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtUtils jwtUtils;

    private Exercise exercise;
    private Muscle muscle;
    private Gender gender;
    private Day day;

    private ExerciseRequest exerciseRequest;
    private MuscleRequest muscleRequest;
    private GenderRequest genderRequest;
    private DayRequest dayRequest;

    private Exercise updatedExercise;


    @TestConfiguration
    static class MockConfig {
        @Bean
        public ExerciseServiceImpl exerciseService() {
            return Mockito.mock(ExerciseServiceImpl.class);
        }

        @Bean
        public ExerciseRepository exerciseRepository() {
            return Mockito.mock(ExerciseRepository.class);
        }

        @Bean
        public UserServiceImpl userService() {
            return Mockito.mock(UserServiceImpl.class);
        }

        @Bean
        public JwtUtils jwtUtils() {
            return Mockito.mock(JwtUtils.class);
        }
    }

    @BeforeEach
    public void setUp() {
        exercise = new Exercise();
        gender = new Gender();
        day = new Day();
        muscle = new Muscle();

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

        exerciseRequest = new ExerciseRequest();
        exerciseRequest.setName("Test");
        muscleRequest = new MuscleRequest();
        muscleRequest.setName("Back");
        exerciseRequest.setMuscle(muscleRequest);
        genderRequest = new GenderRequest();
        genderRequest.setName("Female");
        exerciseRequest.setGender(genderRequest);
        dayRequest = new DayRequest();
        dayRequest.setName("Wednesday");
        exerciseRequest.setDay(dayRequest);
    }

    @Test
    public void testGetExerciseSuccess() throws Exception {
        when(exerciseService.readExercise(exercise.getId())).thenReturn(exercise);

        mockMvc.perform(get("/exercise/{id}", exercise.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.muscle.name").value("Chest"))
                .andExpect(jsonPath("$.gender.name").value("Male"))
                .andExpect(jsonPath("$.day.name").value("Monday"));
    }

    @Test
    public void testGetExerciseFailureWrongId() throws Exception {
        when(exerciseService.readExercise(exercise.getId() + 1)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/exercise/{id}", exercise.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTaskSuccess() throws Exception {
        String originalGenderName = exercise.getGender().getName();
        muscle.setName("Back");
        gender.setName("Female");
        day.setName("Wednesday");

        updatedExercise = new Exercise();
        updatedExercise.setId(1L);
        updatedExercise.setName("Test");
        updatedExercise.setMuscle(muscle);
        updatedExercise.setGender(gender);
        updatedExercise.setDay(day);

        when(exerciseService.updateExercise(any(ExerciseRequest.class))).thenReturn(updatedExercise);

        MvcResult result = mockMvc.perform(put("/exercise/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.muscle.name").value("Back"))
                .andExpect(jsonPath("$.gender.name").value("Female"))
                .andExpect(jsonPath("$.day.name").value("Wednesday"))
                .andReturn();

        Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.name"), exercise.getName());
        Assertions.assertEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.muscle.name"), exerciseRequest.getMuscle().getName());
        Assertions.assertNotEquals(JsonPath.read(result.getResponse().getContentAsString(), "$.gender.name"), originalGenderName);
    }

    @Test
    public void testUpdateTaskFailureWrongId() throws Exception {
        day.setName("Wednesday");

        updatedExercise = new Exercise();
        updatedExercise.setId(1L);
        updatedExercise.setName("Test");
        updatedExercise.setMuscle(muscle);
        updatedExercise.setGender(gender);
        updatedExercise.setDay(day);

        when(exerciseService.updateExercise(any(ExerciseRequest.class))).thenThrow(EntityNullException.class);

        mockMvc.perform(put("/exercise/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateTaskNameIncorrect() throws Exception {
        exerciseRequest.setName("wrong");
        when(exerciseService.updateExercise(any(ExerciseRequest.class))).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/exercise/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTaskSuccess() throws Exception {
        mockMvc.perform(delete("/exercise/delete/{name}", exercise.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTaskFailureWrongName() throws Exception {
        exerciseRequest.setName(null);
        when(exerciseRequest.getName() == null).thenThrow(EntityNullException.class);

        mockMvc.perform(delete("/exercise/delete/{name}", exerciseRequest.getName())
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
