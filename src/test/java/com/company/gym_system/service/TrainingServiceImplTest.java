package com.company.gym_system.service;

import com.company.gym_system.dao.GenericDao;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
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
    private GenericDao<Training> trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Cardio");

        training = new Training();
        training.setId(1L);
        training.setTraineeId(1L);
        training.setTrainerId(1L);
        training.setTrainingName("Morning Workout");
        training.setTrainingType(trainingType);
        training.setTrainingDate(LocalDate.of(2023, 1, 1));
        training.setTrainingDuration("60 minutes");
    }

    @Test
    @DisplayName("Should create training")
    void create_ShouldCreateTraining() {
        // Arrange
        when(trainingDao.save(any(Training.class))).thenAnswer(invocation -> {
            Training savedTraining = invocation.getArgument(0);
            savedTraining.setId(1L);
            return savedTraining;
        });

        // Act
        Training createdTraining = trainingService.create(training);

        // Assert
        assertNotNull(createdTraining);
        assertEquals(1L, createdTraining.getId());
        assertEquals(1L, createdTraining.getTraineeId());
        assertEquals(1L, createdTraining.getTrainerId());
        assertEquals("Morning Workout", createdTraining.getTrainingName());
        assertEquals(trainingType, createdTraining.getTrainingType());
        assertEquals(LocalDate.of(2023, 1, 1), createdTraining.getTrainingDate());
        assertEquals("60 minutes", createdTraining.getTrainingDuration());

        verify(trainingDao).save(any(Training.class));
    }

    @Test
    @DisplayName("Should list all trainings")
    void listAll_ShouldListAllTrainings() {
        // Arrange
        TrainingType anotherTrainingType = new TrainingType();
        anotherTrainingType.setTrainingTypeName("Strength");

        Training anotherTraining = new Training();
        anotherTraining.setId(2L);
        anotherTraining.setTraineeId(2L);
        anotherTraining.setTrainerId(2L);
        anotherTraining.setTrainingName("Evening Workout");
        anotherTraining.setTrainingType(anotherTrainingType);
        anotherTraining.setTrainingDate(LocalDate.of(2023, 1, 2));
        anotherTraining.setTrainingDuration("45 minutes");

        List<Training> trainings = Arrays.asList(training, anotherTraining);
        when(trainingDao.findAll()).thenReturn(trainings);

        // Act
        List<Training> result = trainingService.listAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(training));
        assertTrue(result.contains(anotherTraining));
        verify(trainingDao).findAll();
    }
}