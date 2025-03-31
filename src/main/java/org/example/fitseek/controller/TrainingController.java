package org.example.fitseek.controller;

import io.jsonwebtoken.JwtException;
import org.example.fitseek.config.jwt.JwtUtils;
import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.dto.request.UserInformationRequest;
import org.example.fitseek.dto.response.ExerciseResponse;
import org.example.fitseek.dto.response.UserResponse;
import org.example.fitseek.model.Exercise;
import org.example.fitseek.model.TrainingType;
import org.example.fitseek.model.User;
import org.example.fitseek.service.ExerciseService;
import org.example.fitseek.service.TrainingService;
import org.example.fitseek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TrainingController {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("/male-training")
    public ResponseEntity<?> chooseTypeMaleTraining(@RequestBody TrainingTypeRequest trainingTypeRequest) {
       try {
           TrainingType trainingType = trainingService.chooseTraining(trainingTypeRequest);
           if (trainingType.getName().equalsIgnoreCase("Split"))
               return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/male-training/split")).build();
           if (trainingType.getName().equalsIgnoreCase("FullBody"))
               return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/male-training/fullbody")).build();
           return ResponseEntity.ok().build();
       }
       catch (NullPointerException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
    }

    @PostMapping("/female-training")
    public ResponseEntity<?> chooseTypeFemaleTraining(@RequestBody TrainingTypeRequest trainingTypeRequest) {
        try {
            TrainingType trainingType = trainingService.chooseTraining(trainingTypeRequest);
            if(trainingType.getName().equalsIgnoreCase("Split"))
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/female-training/split")).build();
            if(trainingType.getName().equalsIgnoreCase("FullBody"))
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/female-training/fullbody")).build();
            return ResponseEntity.ok().build();
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/male-training/split")
    public ResponseEntity<?> splitTrainingMale(@RequestBody UserInformationRequest userInformationRequest) {
        try {
            List<Exercise> exercises = exerciseService.exerciseListForSplit(userInformationRequest.getAge(), userInformationRequest.getWeight(),
                    userInformationRequest.getHeight(), 1);
            List<ExerciseResponse> exerciseResponses = new ArrayList<>();
            for(Exercise exercise : exercises) {
                ExerciseResponse exerciseResponse = new ExerciseResponse(exercise);
                exerciseResponses.add(exerciseResponse);
            }
            return ResponseEntity.status(HttpStatus.OK).body(exerciseResponses);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/female-training/split")
    public ResponseEntity<?> splitTrainingFemale(@RequestBody UserInformationRequest userInformationRequest) {
        try {
            List<Exercise> exercises = exerciseService.exerciseListForSplit(userInformationRequest.getAge(), userInformationRequest.getWeight(),
                    userInformationRequest.getHeight(), 2);
            List<ExerciseResponse> exerciseResponses = new ArrayList<>();
            for(Exercise exercise : exercises) {
                ExerciseResponse exerciseResponse = new ExerciseResponse(exercise);
                exerciseResponses.add(exerciseResponse);
            }
            return ResponseEntity.status(HttpStatus.OK).body(exerciseResponses);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/male-training/fullbody")
    public ResponseEntity<?> fullBodyTrainingMale(@RequestBody UserInformationRequest userInformationRequest) {
        try {
            List<Exercise> exercises = exerciseService.exerciseListForFullBody(userInformationRequest.getAge(), userInformationRequest.getWeight(),
                    userInformationRequest.getHeight(), 1);
            List<ExerciseResponse> exerciseResponses = new ArrayList<>();
            for(Exercise exercise : exercises) {
                ExerciseResponse exerciseResponse = new ExerciseResponse(exercise);
                exerciseResponses.add(exerciseResponse);
            }
            return ResponseEntity.status(HttpStatus.OK).body(exerciseResponses);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/female-training/fullbody")
    public ResponseEntity<?> fullBodyTrainingFemale(@RequestBody UserInformationRequest userInformationRequest) {
        try {
            List<Exercise> exercises = exerciseService.exerciseListForFullBody(userInformationRequest.getAge(), userInformationRequest.getWeight(),
                    userInformationRequest.getHeight(), 2);
            List<ExerciseResponse> exerciseResponses = new ArrayList<>();
            for(Exercise exercise : exercises) {
                ExerciseResponse exerciseResponse = new ExerciseResponse(exercise);
                exerciseResponses.add(exerciseResponse);
            }
            return ResponseEntity.status(HttpStatus.OK).body(exerciseResponses);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
