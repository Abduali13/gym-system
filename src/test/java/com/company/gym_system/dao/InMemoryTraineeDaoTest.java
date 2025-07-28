package com.company.gym_system.dao;

import com.company.gym_system.dao.inmemorydao.InMemoryTraineeDao;
import com.company.gym_system.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTraineeDaoTest {

    private InMemoryTraineeDao dao;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        dao = new InMemoryTraineeDao();
        trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("john.doe");
        trainee.setPassword("password123");
        trainee.setIsActive(true);
        trainee.setUserId(1L);
        trainee.setBirthDate(LocalDate.of(2022, 1, 1));
        trainee.setAddress("100 Main St, New York, NY 10001, USA");
    }

    @Test
    @DisplayName("Should save entity and return it")
    void save_ShouldSaveEntityAndReturnIt() {

        Trainee savedTrainee = dao.save(trainee);

        assertEquals(trainee, savedTrainee);
    }

    @Test
    @DisplayName("Should find entity by ID")
    void findById_ShouldFindEntityById() {

        dao.save(trainee);

        Optional<Trainee> foundTrainee = dao.findById(1L);

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee, foundTrainee.get());
    }

    @Test
    @DisplayName("Should return empty optional when entity not found")
    void findById_ShouldReturnEmptyOptional_WhenEntityNotFound() {

        Optional<Trainee> foundTrainee = dao.findById(1L);

        assertFalse(foundTrainee.isPresent());
    }

    @Test
    @DisplayName("Should find all entities")
    void findAll_ShouldFindAllEntities() {

        dao.save(trainee);

        Trainee anotherTrainee = new Trainee();
        anotherTrainee.setTraineeId(2L);
        anotherTrainee.setFirstName("Jane");
        anotherTrainee.setLastName("Doe");
        anotherTrainee.setUsername("jane.doe");
        anotherTrainee.setPassword("password456");
        anotherTrainee.setIsActive(true);
        anotherTrainee.setUserId(2L);
        anotherTrainee.setBirthDate(LocalDate.of(2022, 1, 2));
        anotherTrainee.setAddress("101 Main St, New York, NY 10001, USA");
        dao.save(anotherTrainee);

        List<Trainee> trainees = dao.findAll();

        assertEquals(2, trainees.size());
        assertTrue(trainees.contains(trainee));
        assertTrue(trainees.contains(anotherTrainee));
    }

    @Test
    @DisplayName("Should delete entity by ID")
    void delete_ShouldDeleteEntityById() {

        dao.save(trainee);

        dao.delete(1L);

        Optional<Trainee> foundTrainee = dao.findById(1L);
        assertFalse(foundTrainee.isPresent());
    }

    @Test
    @DisplayName("Should not throw exception when deleting non-existent entity")
    void delete_ShouldNotThrowException_WhenDeletingNonExistentEntity() {

        assertDoesNotThrow(() -> dao.delete(1L));
    }

    @Test
    @DisplayName("Should update entity when saving with existing ID")
    void save_ShouldUpdateEntity_WhenSavingWithExistingId() {
        dao.save(trainee);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setTraineeId(1L);
        updatedTrainee.setFirstName("Updated");
        updatedTrainee.setLastName("Name");
        updatedTrainee.setUsername("updated.name");
        updatedTrainee.setPassword("newpassword");
        updatedTrainee.setIsActive(false);
        updatedTrainee.setUserId(2L);
        updatedTrainee.setBirthDate(LocalDate.of(2023, 1, 1));
        updatedTrainee.setAddress("123 Main St, New York, NY 10001, USA");

        Trainee result = dao.save(updatedTrainee);

        assertEquals(updatedTrainee, result);
        
        Optional<Trainee> foundTrainee = dao.findById(1L);
        assertTrue(foundTrainee.isPresent());
        assertEquals("Updated", foundTrainee.get().getFirstName());
        assertEquals("Name", foundTrainee.get().getLastName());
        assertEquals("updated.name", foundTrainee.get().getUsername());
        assertEquals("newpassword", foundTrainee.get().getPassword());
        assertEquals(LocalDate.of(2023, 1, 1), foundTrainee.get().getBirthDate());
        assertEquals("123 Main St, New York, NY 10001, USA", foundTrainee.get().getAddress());
        assertEquals(2L, foundTrainee.get().getUserId());
        assertFalse(foundTrainee.get().getIsActive());
    }
}