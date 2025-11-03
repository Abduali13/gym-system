package com.company.gym_system.controller;

import com.company.gym_system.entity.Training;
import com.company.gym_system.service.GymFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainings")
public class TrainingController {

    private final GymFacade gymFacade;

    @PostMapping("/add-training")
    public ResponseEntity<Void> addTraining(@RequestBody Training training) {
        gymFacade.addTraining(training);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        gymFacade.deleteTraining(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list-all-training")
    public ResponseEntity<List<Training>> listTraining() {
        return ResponseEntity.ok(gymFacade.listAllTrainings());
    }

}
