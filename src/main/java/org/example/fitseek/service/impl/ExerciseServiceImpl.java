package org.example.fitseek.service.impl;

import org.example.fitseek.model.Exercise;
import org.example.fitseek.repository.ExerciseRepository;
import org.example.fitseek.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private static final double GROUPED_WEIGHT_60 = 60;
    private static final double GROUPED_WEIGHT_75 = 75;
    private static final double GROUPED_WEIGHT_90 = 90;
    private static final double GROUPED_WEIGHT_91 = 91;

    @Override
    public List<Exercise> exerciseListForSplit(int age, double weight, double height, Long genderId) {
        final int ageGrouped = groupAge(age);
        final double weightGrouped = groupWeight(weight);
        List<Exercise> listForSplit = new ArrayList<>();
        GROUPS_FOR_SPLIT.forEach((muscleId, approaches) -> {
            for(int i = 0; i < approaches; i++) {
                exerciseRepository.findFirstByAgeAndWeightAndHeight(ageGrouped, weightGrouped, genderId, muscleId)
                        .stream().filter(exercise -> !listForSplit.contains(exercise)).findFirst().map(listForSplit::add);
            }
        });
        return listForSplit;
    }

    @Override
    public List<Exercise> exerciseListForFullBody(int age, double weight, double height, Long genderId) {
        final int ageGrouped = groupAge(age);
        final double weightGrouped = groupWeight(weight);
        List<Exercise> listForFullBody = new ArrayList<>();
        GROUPS_FOR_FULLBODY.forEach((muscleId, approaches) -> {
            for(int i = 0; i < approaches; i++) {
                exerciseRepository.findFirstByAgeAndWeightAndHeight(ageGrouped, weightGrouped, genderId, muscleId)
                        .stream().findFirst().map(listForFullBody::add);
            }
        });
        return listForFullBody;
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

    private double groupWeight(double weight) {
        if (weight <= GROUPED_WEIGHT_60) {
            return GROUPED_WEIGHT_60;
        } else if (GROUPED_WEIGHT_60 < weight && weight <= GROUPED_WEIGHT_75) {
            return GROUPED_WEIGHT_75;
        } else if (GROUPED_WEIGHT_75 < weight && weight <= GROUPED_WEIGHT_90) {
            return GROUPED_WEIGHT_90;
        } else {
            return GROUPED_WEIGHT_91;
        }
    }
}
