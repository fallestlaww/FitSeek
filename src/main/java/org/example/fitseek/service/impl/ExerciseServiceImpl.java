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
    public List<Exercise> exerciseListForSplit(int age, double weight, double height, Long genderId) {
        List<Exercise> listForSplit = new ArrayList<>();
        Map<Long, Integer> groups = Map.of(1L,4,
                2L,4,
                3L,4,
                4L,3,
                5L,3,
                6L,3);
        groups.forEach((muscleId, approaches) -> {
            for(int i = 0; i < approaches; i++) {
                exerciseRepository.findFirstByAgeAndWeightAndHeight(age, weight, height, genderId, muscleId)
                        .ifPresent(listForSplit::add);
            }
        });
        return listForSplit;
    }

    @Override
    public List<Exercise> exerciseListForFullBody(int age, double weight, double height, Long genderId) {
        List<Exercise> listForFullBody = new ArrayList<>();
        Map<Long, Integer> groups = Map.of(1L,1,
                2L,1,
                3L,1,
                4L,1,
                5L,1,
                6L,1);
        groups.forEach((muscleId, approaches) -> {
            for(int i = 0; i < approaches; i++) {
                exerciseRepository.findFirstByAgeAndWeightAndHeight(age, weight, height, genderId, muscleId)
                        .ifPresent(listForFullBody::add);
            }
        });
        return listForFullBody;
    }
}
