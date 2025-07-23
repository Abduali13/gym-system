package com.company.gym_system.dao;

import com.company.gym_system.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDaoTest {

    private InMemoryDao<Trainee> dao;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        dao = new InMemoryDao<>();
        trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("john.doe");
        trainee.setPassword("password123");
        trainee.setIsActive(true);
    }

    @Test
    @DisplayName("Should save entity and return it")
    void save_ShouldSaveEntityAndReturnIt() {
        // Act
        Trainee savedTrainee = dao.save(trainee);

        // Assert
        assertEquals(trainee, savedTrainee);
    }

    @Test
    @DisplayName("Should find entity by ID")
    void findById_ShouldFindEntityById() {
        // Arrange
        dao.save(trainee);

        // Act
        Optional<Trainee> foundTrainee = dao.findById(1L);

        // Assert
        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee, foundTrainee.get());
    }

    @Test
    @DisplayName("Should return empty optional when entity not found")
    void findById_ShouldReturnEmptyOptional_WhenEntityNotFound() {
        // Act
        Optional<Trainee> foundTrainee = dao.findById(1L);

        // Assert
        assertFalse(foundTrainee.isPresent());
    }

    @Test
    @DisplayName("Should find all entities")
    void findAll_ShouldFindAllEntities() {
        // Arrange
        dao.save(trainee);

        Trainee anotherTrainee = new Trainee();
        anotherTrainee.setTraineeId(2L);
        anotherTrainee.setFirstName("Jane");
        anotherTrainee.setLastName("Doe");
        anotherTrainee.setUsername("jane.doe");
        anotherTrainee.setPassword("password456");
        anotherTrainee.setIsActive(true);
        dao.save(anotherTrainee);

        // Act
        List<Trainee> trainees = dao.findAll();

        // Assert
        assertEquals(2, trainees.size());
        assertTrue(trainees.contains(trainee));
        assertTrue(trainees.contains(anotherTrainee));
    }

    @Test
    @DisplayName("Should delete entity by ID")
    void delete_ShouldDeleteEntityById() {
        // Arrange
        dao.save(trainee);

        // Act
        dao.delete(1L);

        // Assert
        Optional<Trainee> foundTrainee = dao.findById(1L);
        assertFalse(foundTrainee.isPresent());
    }

    @Test
    @DisplayName("Should not throw exception when deleting non-existent entity")
    void delete_ShouldNotThrowException_WhenDeletingNonExistentEntity() {
        // Act & Assert
        assertDoesNotThrow(() -> dao.delete(1L));
    }

    @Test
    @DisplayName("Should update entity when saving with existing ID")
    void save_ShouldUpdateEntity_WhenSavingWithExistingId() {
        // Arrange
        dao.save(trainee);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setTraineeId(1L);
        updatedTrainee.setFirstName("Updated");
        updatedTrainee.setLastName("Name");
        updatedTrainee.setUsername("updated.name");
        updatedTrainee.setPassword("newpassword");
        updatedTrainee.setIsActive(false);

        // Act
        Trainee result = dao.save(updatedTrainee);

        // Assert
        assertEquals(updatedTrainee, result);
        
        Optional<Trainee> foundTrainee = dao.findById(1L);
        assertTrue(foundTrainee.isPresent());
        assertEquals("Updated", foundTrainee.get().getFirstName());
        assertEquals("Name", foundTrainee.get().getLastName());
        assertEquals("updated.name", foundTrainee.get().getUsername());
        assertEquals("newpassword", foundTrainee.get().getPassword());
        assertFalse(foundTrainee.get().getIsActive());
    }
}