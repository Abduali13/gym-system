package com.company.gym_system.service;


import com.company.gym_system.dao.TraineeDao;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.service.impl.TraineeServiceImpl;
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
class TraineeServiceImplTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;
    private Map<String, Object> existingUsernames;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("john.doe");
        trainee.setPassword("password123");
        trainee.setIsActive(true);

        existingUsernames = new HashMap<>();
        traineeService.setExistingUsernames(existingUsernames);
    }

    @Test
    @DisplayName("Should create trainee with generated username and password")
    void create_ShouldCreateTraineeWithGeneratedUsernameAndPassword() {

        Trainee inputTrainee = new Trainee();
        inputTrainee.setFirstName("John");
        inputTrainee.setLastName("Doe");

        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> {
            Trainee savedTrainee = invocation.getArgument(0);
            savedTrainee.setTraineeId(1L);
            return savedTrainee;
        });

        Trainee createdTrainee = traineeService.create(inputTrainee);

        assertNotNull(createdTrainee);
        assertEquals("John", createdTrainee.getFirstName());
        assertEquals("Doe", createdTrainee.getLastName());
        assertEquals("John.Doe", createdTrainee.getUsername());
        assertNotNull(createdTrainee.getPassword());
        assertEquals(10, createdTrainee.getPassword().length());
        assertTrue(createdTrainee.getIsActive());
        assertEquals(1L, createdTrainee.getTraineeId());

        verify(traineeDao).save(any(Trainee.class));
        assertTrue(existingUsernames.containsKey("John.Doe"));
    }

    @Test
    @DisplayName("Should update trainee when it exists")
    void update_ShouldUpdateTrainee_WhenTraineeExists() {

        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));
        when(traineeDao.save(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.update(trainee);

        assertNotNull(updatedTrainee);
        assertEquals(trainee, updatedTrainee);
        verify(traineeDao).findById(1L);
        verify(traineeDao).save(trainee);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent trainee")
    void update_ShouldThrowException_WhenTraineeDoesNotExist() {

        when(traineeDao.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.update(trainee);
        });
        assertEquals("Trainee with ID 1 does not exist.", exception.getMessage());
        verify(traineeDao).findById(1L);
        verify(traineeDao, never()).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Should delete trainee and remove username from map")
    void delete_ShouldDeleteTraineeAndRemoveUsernameFromMap() {

        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));
        existingUsernames.put("john.doe", trainee);

        traineeService.delete(1L);

        verify(traineeDao).findById(1L);
        verify(traineeDao).delete(1L);
        assertFalse(existingUsernames.containsKey("john.doe"));
    }

    @Test
    @DisplayName("Should handle exception when deleting trainee fails")
    void delete_ShouldHandleException_WhenDeletingTraineeFails() {

        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));
        doThrow(new RuntimeException("Delete failed")).when(traineeDao).delete(1L);
        existingUsernames.put("john.doe", trainee);

        traineeService.delete(1L);

        verify(traineeDao).findById(1L);
        verify(traineeDao).delete(1L);
        assertTrue(existingUsernames.containsKey("john.doe"));
    }

    @Test
    @DisplayName("Should list all trainees")
    void listAll_ShouldListAllTrainees() {

        Trainee anotherTrainee = new Trainee();
        anotherTrainee.setTraineeId(2L);
        anotherTrainee.setFirstName("Jane");
        anotherTrainee.setLastName("Doe");
        List<Trainee> trainees = Arrays.asList(trainee, anotherTrainee);
        when(traineeDao.findAll()).thenReturn(trainees);

        List<Trainee> result = traineeService.listAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(trainee));
        assertTrue(result.contains(anotherTrainee));
        verify(traineeDao).findAll();
    }
}