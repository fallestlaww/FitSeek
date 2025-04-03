package org.example.fitseek.service;

import org.example.fitseek.model.Exercise;

import java.util.List;

public interface ExerciseService {
    List<Exercise> exerciseListForSplit(int age, double weight, Long genderId);
    List<Exercise> exerciseListForFullBody(int age, double weight,  Long genderId);
}
