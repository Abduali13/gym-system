package com.company.gym_system.dao;

import com.company.gym_system.dao.inmemorydao.InMemoryTrainerDao;
import com.company.gym_system.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTrainerDaoTest {
    private InMemoryTrainerDao dao;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        dao = new InMemoryTrainerDao();
        trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername("john.doe");
        trainer.setPassword("password123");
        trainer.setIsActive(true);
        trainer.setUserId(1L);
        trainer.setSpecialization("Cardio");
    }

    @Test
    @DisplayName("Should save entity and return it")
    void save_ShouldSaveEntityAndReturnIt() {

        Trainer savedTrainee = dao.save(trainer);

        assertEquals(trainer, savedTrainee);
    }

    @Test
    @DisplayName("Should find entity by ID")
    void findById_ShouldFindEntityById() {

        dao.save(trainer);

        Optional<Trainer> foundTrainer = dao.findById(1L);

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer, foundTrainer.get());
    }

    @Test
    @DisplayName("Should return empty optional when entity not found")
    void findById_ShouldReturnEmptyOptional_WhenEntityNotFound() {

        Optional<Trainer> foundTrainer = dao.findById(1L);

        assertFalse(foundTrainer.isPresent());
    }

    @Test
    @DisplayName("Should find all entities")
    void findAll_ShouldFindAllEntities() {

        dao.save(trainer);

        Trainer anotherTrainer = new Trainer();
        anotherTrainer.setTrainerId(2L);
        anotherTrainer.setFirstName("Jane");
        anotherTrainer.setLastName("Doe");
        anotherTrainer.setUsername("jane.doe");
        anotherTrainer.setPassword("password456");
        anotherTrainer.setIsActive(true);
        anotherTrainer.setSpecialization("Strength");
        anotherTrainer.setUserId(2L);
        dao.save(anotherTrainer);

        List<Trainer> trainers = dao.findAll();

        assertEquals(2, trainers.size());
        assertTrue(trainers.contains(trainer));
        assertTrue(trainers.contains(anotherTrainer));
    }

    @Test
    @DisplayName("Should delete entity by ID")
    void delete_ShouldDeleteEntityById() {

        dao.save(trainer);

        Optional<Trainer> foundTrainee = dao.findById(1L);
        assertFalse(foundTrainee.isPresent());
    }


    @Test
    @DisplayName("Should update entity when saving with existing ID")
    void save_ShouldUpdateEntity_WhenSavingWithExistingId() {

        dao.save(trainer);

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setTrainerId(1L);
        updatedTrainer.setFirstName("Updated");
        updatedTrainer.setLastName("Name");
        updatedTrainer.setUsername("updated.name");
        updatedTrainer.setPassword("newpassword");
        updatedTrainer.setIsActive(false);
        updatedTrainer.setUserId(2L);
        updatedTrainer.setSpecialization("Strength");

        Trainer result = dao.save(updatedTrainer);

        assertEquals(updatedTrainer, result);

        Optional<Trainer> foundTrainer = dao.findById(1L);
        assertTrue(foundTrainer.isPresent());
        assertEquals("Updated", foundTrainer.get().getFirstName());
        assertEquals("Name", foundTrainer.get().getLastName());
        assertEquals("updated.name", foundTrainer.get().getUsername());
        assertEquals("newpassword", foundTrainer.get().getPassword());
        assertEquals(2L, foundTrainer.get().getUserId());
        assertEquals("Strength", foundTrainer.get().getSpecialization());
        assertFalse(foundTrainer.get().getIsActive());
    }
}
