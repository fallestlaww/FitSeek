package org.example.fitseek.service.impl;

import org.example.fitseek.dto.response.ExerciseResponse;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.repository.ExerciseRepository;
import org.example.fitseek.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public List<Exercise> exerciseListForSplit(int age, double weight, double height, int genderId) {
        List<Exercise> listForSplit = new ArrayList<>();
        Map<Integer, Integer> groups = Map.of(1,4,
                2,4,
                3,4,
                4,3,
                5,3,
                6,3);
        groups.forEach((muscleId, approaches) -> {
            for(int i = 0; i < approaches; i++) {
                exerciseRepository.findFirstByAgeAndWeightAndHeight(age, weight, height, genderId, muscleId)
                        .ifPresent(listForSplit::add);
            }
        });
        return listForSplit;
    }

    @Override
    public List<Exercise> exerciseListForFullBody(int age, double weight, double height, int genderId) {
        List<Exercise> listForFullBody = new ArrayList<>();
        Map<Integer, Integer> groups = Map.of(1,1,
                2,1,
                3,1,
                4,1,
                5,1,
                6,1);
        groups.forEach((muscleId, approaches) -> {
            for(int i = 0; i < approaches; i++) {
                exerciseRepository.findFirstByAgeAndWeightAndHeight(age, weight, height, genderId, muscleId)
                        .ifPresent(listForFullBody::add);
            }
        });
        return listForFullBody;
    }
}
