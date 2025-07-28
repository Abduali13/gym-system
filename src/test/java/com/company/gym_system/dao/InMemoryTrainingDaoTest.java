package com.company.gym_system.dao;

import com.company.gym_system.dao.inmemorydao.InMemoryTrainingDao;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTrainingDaoTest {
    private InMemoryTrainingDao dao;
    private Training training;

    @BeforeEach
    void setUp() {
        dao = new InMemoryTrainingDao();
        training = new Training();
        training.setTrainingId(1L);
        training.setTrainingDate(LocalDate.of(2023, 1, 1));
        training.setTrainerId(1L);
        training.setTraineeId(1L);
        training.setTrainingDuration(200);
        training.setTrainingName("Morning Workout");
        training.setTrainingType(TrainingType.STRENGTH);

    }

    @Test
    @DisplayName("Should save entity and return it")
    void save_ShouldSaveEntityAndReturnIt() {

        Training savedTrainee = dao.save(training);

        assertEquals(training, savedTrainee);
    }

    @Test
    @DisplayName("Should find entity by ID")
    void findById_ShouldFindEntityById() {

        dao.save(training);

        Optional<Training> foundTraining = dao.findById(1L);

        assertTrue(foundTraining.isPresent());
        assertEquals(training, foundTraining.get());
    }

    @Test
    @DisplayName("Should return empty optional when entity not found")
    void findById_ShouldReturnEmptyOptional_WhenEntityNotFound() {

        Optional<Training> foundTraining = dao.findById(1L);

        assertFalse(foundTraining.isPresent());
    }

    @Test
    @DisplayName("Should find all entities")
    void findAll_ShouldFindAllEntities() {

        dao.save(training);


        Training anotherTraining = new Training();
        anotherTraining.setTrainingId(2L);
        anotherTraining.setTrainingDate(LocalDate.of(2023, 1, 2));
        anotherTraining.setTrainerId(2L);
        anotherTraining.setTraineeId(2L);
        anotherTraining.setTrainingDuration(201);
        anotherTraining.setTrainingName("Afternoon Workout");
        anotherTraining.setTrainingType(TrainingType.FLEXIBILITY);

        dao.save(anotherTraining);

        List<Training> trainings = dao.findAll();

        assertEquals(2, trainings.size());
        assertTrue(trainings.contains(training));
        assertTrue(trainings.contains(anotherTraining));
    }

    @Test
    @DisplayName("Should delete entity by ID")
    void delete_ShouldDeleteEntityById() {

        dao.save(training);

        Optional<Training> foundTraining = dao.findById(1L);
        assertFalse(foundTraining.isPresent());
    }


    @Test
    @DisplayName("Should update entity when saving with existing ID")
    void save_ShouldUpdateEntity_WhenSavingWithExistingId() {

        dao.save(training);
        training.setTrainingId(1L);
        training.setTrainingDate(LocalDate.of(2023, 12, 12));
        training.setTrainerId(2L);
        training.setTraineeId(3L);
        training.setTrainingDuration(500);
        training.setTrainingName("Updated Workout");
        training.setTrainingType(TrainingType.CARDIO);


        Training updatedTraining = new Training();
        updatedTraining.setTrainingId(1L);

        Training result = dao.save(updatedTraining);

        assertEquals(updatedTraining, result);
        Optional<Training> foundTraining = dao.findById(1L);
        assertTrue(foundTraining.isPresent());

        assertEquals(LocalDate.of(2023, 12, 12), updatedTraining.getTrainingDate());
        assertEquals(2L, updatedTraining.getTrainerId());
        assertEquals(3L, updatedTraining.getTraineeId());
        assertEquals(500, updatedTraining.getTrainingDuration());
        assertEquals("Updated Workout", updatedTraining.getTrainingName());
        assertEquals(TrainingType.CARDIO, updatedTraining.getTrainingType());



    }
}
