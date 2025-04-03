package org.example.fitseek.service.impl;

import org.example.fitseek.model.Exercise;
import org.example.fitseek.model.Recommendation;
import org.example.fitseek.repository.ExerciseRepository;
import org.example.fitseek.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    @Autowired
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

    @Override
    public List<Exercise> exerciseListForSplit(int age, double weight, Long genderId) {
        int ageGrouped = groupAge(age);
        double weightGrouped = groupWeight(weight, genderId);
        return getFilteredExercises(ageGrouped, weightGrouped, genderId, GROUPS_FOR_SPLIT);
    }

    @Override
    public List<Exercise> exerciseListForFullBody(int age, double weight, Long genderId) {
        int ageGrouped = groupAge(age);
        double weightGrouped = groupWeight(weight, genderId);
        return getFilteredExercises(ageGrouped, weightGrouped, genderId, GROUPS_FOR_FULLBODY);
    }

    private List<Exercise> getFilteredExercises(int ageGrouped, double weightGrouped, Long genderId, Map<Long, Integer> muscleGroups) {
        List<Exercise> exercises = new ArrayList<>();

        muscleGroups.forEach((muscleId, approaches) -> {
            for (int i = 0; i < approaches; i++) {
                exerciseRepository.findFirstByAgeAndWeightAndHeight(ageGrouped, weightGrouped, genderId, muscleId)
                        .stream()
                        .filter(exercise -> !exercises.contains(exercise))
                        .findFirst()
                        .ifPresent(exercise -> {
                            List<Recommendation> filteredRecommendations = exercise.getRecommendationList().stream()
                                    .filter(recommendation -> groupAge(recommendation.getUserAge()) == ageGrouped
                                            && groupWeight(recommendation.getUserWeight(), genderId) == weightGrouped)
                                    .toList();
                            exercise.setRecommendationList(filteredRecommendations);
                            if (!exercise.getRecommendationList().isEmpty()) {
                                exercises.add(exercise);
                            }
                        });
            }
        });
        return exercises;
    }

    private int groupAge(int age) {
        if (10 < age && age <= GROUPED_AGE_25) {
            return GROUPED_AGE_25;
        } else if (GROUPED_AGE_25 < age && age <= GROUPED_AGE_40) {
            return GROUPED_AGE_40;
        } else {
            return GROUPED_AGE_41;
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
