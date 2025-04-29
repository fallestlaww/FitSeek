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

/**
 * Implementation of service layer {@link ExerciseService} for {@link Exercise} entity.
 * Provides logic for managing {@link ExerciseService},
 * including role-based access control for sensitive methods, namely {@link #updateExercise(ExerciseRequest)}, {@link #deleteExercise(String)}
 */
@Slf4j
@Service
public class ExerciseServiceImpl implements ExerciseService {
    private ExerciseRepository exerciseRepository;
    // map for different types of training, long parameter represents muscle id, integer parameter represents number of sets
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
        // grouping age and weight by the logic described below
        int ageGrouped = groupAge(age, genderId);
        log.info("Grouped age for split: {}", ageGrouped);
        double weightGrouped = groupWeight(weight, genderId);
        log.info("Weight grouped for split: {}", weightGrouped);
        // generate list of exercise for split training type
        List<Exercise> exercises = getFilteredExercises(ageGrouped, weightGrouped, genderId, GROUPS_FOR_SPLIT);
        if(exercises.isEmpty()) {
            log.info("Exercise list is empty");
            throw new EntityNotFoundException("No exercise found");
        }
        return exercises;
    }

    @Override
    public List<Exercise> exerciseListForFullBody(int age, double weight, Long genderId) {
        // grouping age and weight by the logic described below
        int ageGrouped = groupAge(age, genderId);
        log.info("Grouped age for fullbody: {}", ageGrouped);
        double weightGrouped = groupWeight(weight, genderId);
        log.info("Weight grouped for fullbody: {}", weightGrouped);
        List<Exercise> exercises = getFilteredExercises(ageGrouped, weightGrouped, genderId, GROUPS_FOR_FULLBODY);
        // generate list of exercise for full body training type
        if(exercises.isEmpty()) {
            log.info("Exercise list is empty");
            throw new EntityNotFoundException("No exercise found");
        }
        return exercises;
    }

    @Override
    public List<Exercise> exerciseListForGender(Long genderId) {
        // generate list of exercise by gender
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
        // find in database exercise by name got from request, without existing exercise code can't update anything
        Exercise existingExercise = exerciseRepository.findByName(exercise.getName());
        if(existingExercise == null) {
            log.warn("Exercise with name {} not found", exercise.getName());
            throw new EntityNotFoundException("Exercise with name " + exercise.getName() + " not found");
        }
        // if muscle in request equals null = it does not need to be changed
        if(exercise.getMuscle() != null) {
            try {
                // find in database muscle by name got from request
                Muscle muscle = muscleRepository.findByName(exercise.getMuscle().getName());
                existingExercise.setMuscle(muscle);
            } catch (InvalidEntityException e) {
                log.warn("Invalid muscle {}", exercise.getMuscle().getName());
                throw new InvalidEntityException("Invalid muscle: " + exercise.getMuscle().getName());
            }
        }
        // if gender in request equals null = it does not need to be changed
        if(exercise.getGender() != null) {
            try {
                // find in database gender by name got from request
                Gender gender = genderRepository.findByName(exercise.getGender().getName());
                existingExercise.setGender(gender);
            } catch (InvalidEntityException e) {
                log.warn("Invalid gender {}", exercise.getGender().getName());
                throw new InvalidEntityException("Invalid gender: " + exercise.getGender().getName());
            }
        }
        // if day in request equals null = it does not need to be changed
        if(exercise.getDay() != null) {
            try {
                // find in database day by name got from request
                Day day = dayRepository.findByName(exercise.getDay().getName());
                existingExercise.setDay(day);
            } catch (InvalidEntityException e) {
                log.warn("Invalid day {}", exercise.getDay().getName());
                throw new InvalidEntityException("Invalid day: " + exercise.getDay().getName());
            }
        }
        List<Recommendation> recommendations = new ArrayList<>();
        // if recommendation list in request equals null = it does not need to be changed
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

    /**
     * Filling list of exercises by parameters given
     * @param ageGrouped given grouped age
     * @param weightGrouped given grouped weight
     * @param genderId given gender id
     * @param muscleGroups given map of muscle groups for filling list with exercises
     * @return list of exercises
     */
    private List<Exercise> getFilteredExercises(int ageGrouped, double weightGrouped, Long genderId, Map<Long, Integer> muscleGroups) {
        List<Exercise> exercises = new ArrayList<>();
        muscleGroups.forEach((muscleId, sets) -> {
            log.info("Accepted muscleId: {}", muscleId);
            log.info("Accepted sets: {}", sets);
            for (int i = 0; i < sets; i++) {
                exerciseRepository.findFirstByAgeAndWeight(ageGrouped, weightGrouped, genderId, muscleId)
                        .stream()
                        // if list contains this exercise -> skip this exercise
                        .filter(exercise -> !exercises.contains(exercise))
                        .findAny()
                        .ifPresent(exercise -> {
                            // realization similar to database grouping for correct work
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

        /*
    Because of fact, that recommendations in database is grouped by user age and user weight,
    similar grouping need to be implemented in code, made that for memory retention and ease of work.
    Works as follows for age: users under 25 years old will be marked as 25 years old, users over 25 and under 40
    will be marked as 40 years old and users over 40 will be marked as 41 years old.
    For users who entered gender as "Female": users under 25 years old will be marked as 25 years old,
    and users higher than 25 year will be marked as 40 years old.
     */

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
        }  else {
            return GROUPED_AGE_40;
        }
    }

    /*
    Works as follows for weight: for users who entered gender as a "Male":
    users that weight less than 60 kg, will be marked as 60 kg, users that higher than 60 kg
    and lower than 75 kg will be marked as 75 kg, users that higher than 75 kg and lower than 90 kg
    will be marked as 90 kg, and users that higher than 90 kg will be marked as 91 kg.
    For users who entered gender as a "Female": users that weigh less than 50 kg will be marked as 50 kg,
    user that weigh higher than 50 and less than 60 kg will be marked as 60 kg, and users that weight higher than
    60 kg will be marked as 70 kg.
     */

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
