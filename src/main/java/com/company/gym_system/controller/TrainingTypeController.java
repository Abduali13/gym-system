package com.company.gym_system.controller;

import com.company.gym_system.entity.TrainingType;
import com.company.gym_system.service.GymFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training-types")
public class TrainingTypeController {

    private final GymFacade gymFacade;

    @GetMapping("/list-all")
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        return ResponseEntity.ok(gymFacade.getTrainingTypes());
    }
}
