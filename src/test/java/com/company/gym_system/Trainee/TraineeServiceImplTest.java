package com.company.gym_system.Trainee;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import com.company.gym_system.service.AuthGuard;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.User;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import com.company.gym_system.repository.TrainingRepository;
import com.company.gym_system.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private AuthGuard authGuard;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldSaveTraineeAndReturn() {
        // Arrange
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        String address = "Test Address";
        String firstName = "John";
        String lastName = "Doe";

        Trainee trainee = new Trainee();
        trainee.setBirthDate(birthDate);
        trainee.setAddress(address);

        when(traineeRepository.existsByUser_Username(firstName + lastName)).thenReturn(false);
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trainee saved = traineeService.create(firstName, lastName, birthDate, address);

        // Assert
        assertNotNull(saved);
        assertEquals(address, saved.getAddress());
        assertEquals(birthDate, saved.getBirthDate());
        assertNotNull(saved.getUser());
        assertEquals(firstName, saved.getUser().getFirstName());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void update_ShouldUpdateTraineeDetails() throws AccessDeniedException {
        String username = "john";
        String password = "pass";

        Trainee existing = new Trainee();
        existing.setUser(new User());

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(existing));
        when(traineeRepository.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Trainee updated = traineeService.update(username, password, new Trainee());

        // Assert
        assertNotNull(updated);
        verify(authGuard).checkTrainee(username, password);
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void delete_ShouldRemoveTrainee() throws AccessDeniedException {
        String username = "john";
        String password = "pass";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        // Act
        traineeService.delete(username, password);

        // Assert
        verify(authGuard).checkTrainee(username, password);
        verify(traineeRepository).delete(trainee);
    }

    @Test
    void changePassword_ShouldUpdateUserPassword() {
        String username = "john";
        String oldPwd = "old";
        String newPwd = "new";

        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));

        // Act
        traineeService.changePassword(username, oldPwd, newPwd);

        // Assert
        assertEquals(newPwd, trainee.getUser().getPassword());
        verify(traineeRepository).save(trainee);
    }

    @Test
    void findAvailableTrainers_ShouldReturnFilteredList() {
        String username = "john";
        String password = "pass";

        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        Trainer trainer1 = new Trainer();
        trainer1.setUser(new User());
        Trainer trainer2 = new Trainer();
        trainer2.setUser(new User());
        trainee.setTrainers(List.of(trainer1));

        when(traineeRepository.findByUser_Username(username)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));

        List<Trainer> available = traineeService.findAvailableTrainers(username, password);

        assertTrue(available.contains(trainer2));
        assertFalse(available.contains(trainer1));
    }
}
