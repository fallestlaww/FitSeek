package org.example.fitseek.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.fitseek.dto.request.ExerciseRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.exception.exceptions.InvalidEntityException;
import org.example.fitseek.model.*;
import org.example.fitseek.repository.*;
import org.example.fitseek.service.ExerciseService;
import org.example.fitseek.service.RecommendationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ExerciseServiceImpl implements ExerciseService {
    private ExerciseRepository exerciseRepository;
    private static final Map<Long, Integer> GROUPS_FOR_SPLIT = Map.of(1L,4,
            2L,4, 3L,4, 4L,3, 5L,3, 6L,3);
    private static final Map<Long, Integer> GROUPS_FOR_FULLBODY = Map.of(1L,1,
            2L,1, 3L,1, 4L,1, 5L,1, 6L,1);
    private static final int GROUPED_AGE_25 = 25;
    private static final int GROUPED_AGE_40 = 40;
    private static final int GROUPED_AGE_41 = 41;
    private static final int GROUPED_WEIGHT_50 = 50;
    private static final int GROUPED_WEIGHT_60 = 60;
    private static final int GROUPED_WEIGHT_70 = 70;
    private static final int GROUPED_WEIGHT_75 = 75;
    private static final int GROUPED_WEIGHT_90 = 90;
    private static final int GROUPED_WEIGHT_91 = 91;

    private final MuscleRepository muscleRepository;
    private final GenderRepository genderRepository;
    private final DayRepository dayRepository;
    private final RecommendationService recommendationService;

    public ExerciseServiceImpl(MuscleRepository muscleRepository, GenderRepository genderRepository, DayRepository dayRepository, RecommendationService recommendationService, ExerciseRepository exerciseRepository) {
        this.muscleRepository = muscleRepository;
        this.genderRepository = genderRepository;
        this.dayRepository = dayRepository;
        this.recommendationService = recommendationService;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> exerciseListForSplit(int age, double weight, Long genderId) {
        int ageGrouped = groupAge(age, genderId);
        log.info("Grouped age for split: {}", ageGrouped);
        double weightGrouped = groupWeight(weight, genderId);
        log.info("Weight grouped for split: {}", weightGrouped);
        List<Exercise> exercises = getFilteredExercises(ageGrouped, weightGrouped, genderId, GROUPS_FOR_FULLBODY);
        if(exercises.isEmpty()) {
            log.info("Exercise list is empty");
            throw new EntityNotFoundException("No exercise found");
        }
        return exercises;
    }

    @Override
    public List<Exercise> exerciseListForFullBody(int age, double weight, Long genderId) {
        int ageGrouped = groupAge(age, genderId);
        log.info("Grouped age for fullbody: {}", ageGrouped);
        double weightGrouped = groupWeight(weight, genderId);
        log.info("Weight grouped for fullbody: {}", weightGrouped);
        List<Exercise> exercises = getFilteredExercises(ageGrouped, weightGrouped, genderId, GROUPS_FOR_FULLBODY);
        if(exercises.isEmpty()) {
            log.info("Exercise list is empty");
            throw new EntityNotFoundException("No exercise found");
        }
        return exercises;
    }

    @Override
    public List<Exercise> exerciseListForGender(Long genderId) {
        List<Exercise> exercises = exerciseRepository.findByGenderId(genderId);
        if(exercises == null || exercises.isEmpty()) {
            log.info("Exercise list is empty");
            throw new EntityNotFoundException("Exercise not found");
        }
        return exercises;
    }

    @Override
    public Exercise readExercise(Long id) {
        log.debug("Reading exercise {}", id);
        if(id == null) {
            log.warn("Requested exercise id is null");
            throw new EntityNullException("Requested exercise id is null");
        }
        Optional<Exercise> exerciseOptional = exerciseRepository.findById(id);
        return exerciseOptional.orElseThrow(EntityNotFoundException::new);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Exercise updateExercise(ExerciseRequest exercise) {
        log.debug("Updating exercise {}", Objects.toString(exercise, "null"));
        if(exercise == null) {
            log.warn("Requested exercise is null");
            throw new EntityNullException("Requested exercise is null");
        }
        Exercise existingExercise = exerciseRepository.findByName(exercise.getName());
        if(existingExercise == null) {
            log.warn("Exercise with name {} not found", exercise.getName());
            throw new EntityNotFoundException("Exercise with name " + exercise.getName() + " not found");
        }

        if(exercise.getMuscle() != null) {
            try {
                Muscle muscle = muscleRepository.findByName(exercise.getMuscle().getName());
                existingExercise.setMuscle(muscle);
            } catch (InvalidEntityException e) {
                log.warn("Invalid muscle {}", exercise.getMuscle().getName());
                throw new InvalidEntityException("Invalid muscle: " + exercise.getMuscle().getName());
            }
        }
        if(exercise.getGender() != null) {
            try {
                Gender gender = genderRepository.findByName(exercise.getGender().getName());
                existingExercise.setGender(gender);
            } catch (InvalidEntityException e) {
                log.warn("Invalid gender {}", exercise.getGender().getName());
                throw new InvalidEntityException("Invalid gender: " + exercise.getGender().getName());
            }
        }
        if(exercise.getDay() != null) {
            try {
                Day day = dayRepository.findByName(exercise.getDay().getName());
                existingExercise.setDay(day);
            } catch (InvalidEntityException e) {
                log.warn("Invalid day {}", exercise.getDay().getName());
                throw new InvalidEntityException("Invalid day: " + exercise.getDay().getName());
            }
        }
        List<Recommendation> recommendations = new ArrayList<>();
        if(exercise.getRecommendation() != null) {
            try {
                for(int i = 0; i < exercise.getRecommendation().size(); i++) {
                    Recommendation recommendation = recommendationService.updateRecommendation(exercise.getRecommendation().get(i));
                    recommendations.add(recommendation);
                    existingExercise.setRecommendationList(recommendations);
                }
            } catch (InvalidEntityException e) {
                log.warn("Invalid recommendation {}", recommendations);
                throw new InvalidEntityException("Invalid recommendation " + recommendations);
            }
        }
        return exerciseRepository.save(existingExercise);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    @Transactional
    public void deleteExercise(String name) {
        log.debug("Deleting exercise {}", name);
        if(name == null || name.isEmpty()) {
            log.warn("Requested exercise name is null");
            throw new EntityNullException("Requested exercise name is null");
        }
        exerciseRepository.deleteExerciseByName(name);
    }

    private List<Exercise> getFilteredExercises(int ageGrouped, double weightGrouped, Long genderId, Map<Long, Integer> muscleGroups) {
        List<Exercise> exercises = new ArrayList<>();
        muscleGroups.forEach((muscleId, sets) -> {
            log.info("Accepted muscleId: {}", muscleId);
            log.info("Accepted sets: {}", sets);
            for (int i = 0; i < sets; i++) {
                exerciseRepository.findFirstByAgeAndWeight(ageGrouped, weightGrouped, genderId, muscleId)
                        .stream()
                        .filter(exercise -> !exercises.contains(exercise))
                        .findAny()
                        .ifPresent(exercise -> {
                            List<Recommendation> filteredRecommendations = exercise.getRecommendationList().stream()
                                    .filter(recommendation -> groupAge(recommendation.getUserAge(), genderId) == ageGrouped
                                            && groupWeight(recommendation.getUserWeight(), genderId) == weightGrouped)
                                    .toList();
                            exercise.setRecommendationList(filteredRecommendations);
                            if (!exercise.getRecommendationList().isEmpty()) {
                                exercises.add(exercise);
                            }
                        });
            }
            log.info("Exercises for one group of muscles: {}", exercises);
        });
        log.info("Exercises for training: {}", exercises);
        return exercises;
    }

    private int groupAge(int age, Long genderId) {
        return switch (genderId.intValue()) {
            case 1 -> groupAgeForMale(age);
            case 2 -> groupAgeForFemale(age);
            default -> GROUPED_AGE_40;
        };
    }

    private int groupAgeForMale(int age) {
        if (1 < age && age <= GROUPED_AGE_25) {
            return GROUPED_AGE_25;
        } else if (GROUPED_AGE_25 < age && age <= GROUPED_AGE_40) {
            return GROUPED_AGE_40;
        } else {
            return GROUPED_AGE_41;
        }
    }

    private int groupAgeForFemale(int age) {
        if (1 < age && age <= GROUPED_AGE_25) {
            return GROUPED_AGE_25;
        } else if (GROUPED_AGE_25 < age && age <= GROUPED_AGE_40) {
            return GROUPED_AGE_40;
        } else {
            return GROUPED_AGE_40;
        }
    }

    private int groupWeight(double weight, Long genderId) {
        return switch (genderId.intValue()) {
            case 1 -> groupWeightForMale(weight);
            case 2 -> groupWeightForFemale(weight);
            default -> GROUPED_WEIGHT_91;
        };
    }

    private int groupWeightForMale(double weight) {
        if (weight <= GROUPED_WEIGHT_60) return GROUPED_WEIGHT_60;
        if (weight <= GROUPED_WEIGHT_75) return GROUPED_WEIGHT_75;
        if (weight <= GROUPED_WEIGHT_90) return GROUPED_WEIGHT_90;
        return GROUPED_WEIGHT_91;
    }

    private int groupWeightForFemale(double weight) {
        if (weight <= GROUPED_WEIGHT_50) return GROUPED_WEIGHT_50;
        if (weight <= GROUPED_WEIGHT_60) return GROUPED_WEIGHT_60;
        return GROUPED_WEIGHT_70;
    }
}
