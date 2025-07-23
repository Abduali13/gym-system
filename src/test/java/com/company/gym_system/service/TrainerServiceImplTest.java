package com.company.gym_system.service;

import com.company.gym_system.dao.GenericDao;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private GenericDao<Trainer> trainerDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;
    private Map<String, Object> existingUsernames;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("john.doe");
        trainer.setPassword("password123");
        trainer.setIsActive(true);
        trainer.setSpecialization("Cardio");

        existingUsernames = new HashMap<>();
        trainerService.setExistingUsernames(existingUsernames);
    }

    @Test
    @DisplayName("Should create trainer with generated username and password")
    void create_ShouldCreateTrainerWithGeneratedUsernameAndPassword() {
        // Arrange
        Trainer inputTrainer = new Trainer();
        inputTrainer.setFirstName("John");
        inputTrainer.setLastName("Doe");
        inputTrainer.setSpecialization("Cardio");

        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer savedTrainer = invocation.getArgument(0);
            savedTrainer.setTrainerId(1L);
            return savedTrainer;
        });

        // Act
        Trainer createdTrainer = trainerService.create(inputTrainer);

        // Assert
        assertNotNull(createdTrainer);
        assertEquals("John", createdTrainer.getFirstName());
        assertEquals("Doe", createdTrainer.getLastName());
        assertEquals("John.Doe", createdTrainer.getUsername());
        assertNotNull(createdTrainer.getPassword());
        assertEquals(10, createdTrainer.getPassword().length());
        assertTrue(createdTrainer.getIsActive());
        assertEquals(1L, createdTrainer.getTrainerId());
        assertEquals("Cardio", createdTrainer.getSpecialization());

        verify(trainerDao).save(any(Trainer.class));
        assertTrue(existingUsernames.containsKey("John.Doe"));
    }

    @Test
    @DisplayName("Should update trainer when it exists")
    void update_ShouldUpdateTrainer_WhenTrainerExists() {
        // Arrange
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainerDao.save(trainer)).thenReturn(trainer);

        // Act
        Trainer updatedTrainer = trainerService.update(trainer);

        // Assert
        assertNotNull(updatedTrainer);
        assertEquals(trainer, updatedTrainer);
        verify(trainerDao).findById(1L);
        verify(trainerDao).save(trainer);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent trainer")
    void update_ShouldThrowException_WhenTrainerDoesNotExist() {
        // Arrange
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.update(trainer);
        });
        assertEquals("Trainer with ID 1 does not exist.", exception.getMessage());
        verify(trainerDao).findById(1L);
        verify(trainerDao, never()).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Should list all trainers")
    void listAll_ShouldListAllTrainers() {
        // Arrange
        Trainer anotherTrainer = new Trainer();
        anotherTrainer.setTrainerId(2L);
        anotherTrainer.setSpecialization("Strength");
        List<Trainer> trainers = Arrays.asList(trainer, anotherTrainer);
        when(trainerDao.findAll()).thenReturn(trainers);

        // Act
        List<Trainer> result = trainerService.listAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(trainer));
        assertTrue(result.contains(anotherTrainer));
        verify(trainerDao).findAll();
    }
}
