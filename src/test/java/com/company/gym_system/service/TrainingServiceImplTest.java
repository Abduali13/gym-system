package com.company.gym_system.service;

import com.company.gym_system.dao.TrainingDao;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
import com.company.gym_system.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = TrainingType.CARDIO;

        training = new Training();
        training.setTrainingId(1L);
        training.setTraineeId(1L);
        training.setTrainerId(1L);
        training.setTrainingName("Morning Workout");
        training.setTrainingType(trainingType);
        training.setTrainingDate(LocalDate.of(2023, 1, 1));
        training.setTrainingDuration(200);
    }

    @Test
    @DisplayName("Should create training")
    void create_ShouldCreateTraining() {

        when(trainingDao.save(any(Training.class))).thenAnswer(invocation -> {
            Training savedTraining = invocation.getArgument(0);
            savedTraining.setTrainingId(1L);
            return savedTraining;
        });

        Training createdTraining = trainingService.create(training);

        assertNotNull(createdTraining);
        assertEquals(1L, createdTraining.getTrainingId());
        assertEquals(1L, createdTraining.getTraineeId());
        assertEquals(1L, createdTraining.getTrainerId());
        assertEquals("Morning Workout", createdTraining.getTrainingName());
        assertEquals(trainingType, createdTraining.getTrainingType());
        assertEquals(LocalDate.of(2023, 1, 1), createdTraining.getTrainingDate());
        assertEquals(200, createdTraining.getTrainingDuration());

        verify(trainingDao).save(any(Training.class));
    }

    @Test
    @DisplayName("Should list all trainings")
    void listAll_ShouldListAllTrainings() {

        TrainingType anotherTrainingType = TrainingType.STRENGTH;


        Training anotherTraining = new Training();
        anotherTraining.setTrainingId(2L);
        anotherTraining.setTraineeId(2L);
        anotherTraining.setTrainerId(2L);
        anotherTraining.setTrainingName("Evening Workout");
        anotherTraining.setTrainingType(anotherTrainingType);
        anotherTraining.setTrainingDate(LocalDate.of(2023, 1, 2));
        anotherTraining.setTrainingDuration(120);

        List<Training> trainings = Arrays.asList(training, anotherTraining);
        when(trainingDao.findAll()).thenReturn(trainings);

        List<Training> result = trainingService.listAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(training));
        assertTrue(result.contains(anotherTraining));
        verify(trainingDao).findAll();
    }
}