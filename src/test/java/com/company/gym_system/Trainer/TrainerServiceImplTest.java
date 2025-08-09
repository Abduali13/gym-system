package com.company.gym_system.Trainer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.company.gym_system.config.AuthGuard;
import com.company.gym_system.entity.Trainee;
import com.company.gym_system.entity.Trainer;
import com.company.gym_system.entity.Training;
import com.company.gym_system.entity.User;
import com.company.gym_system.repository.TraineeRepository;
import com.company.gym_system.repository.TrainerRepository;
import com.company.gym_system.repository.TrainingRepository;
import com.company.gym_system.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private AuthGuard authGuard;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldSaveTrainerAndReturn() {
        String fn = "John";
        String ln = "Smith";
        String specialty = "Yoga";

        Trainer trainer = new Trainer();
        trainer.setUser(new User());

        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer saved = trainerService.create(fn, ln, specialty);

        assertNotNull(saved);
        assertEquals(fn, saved.getUser().getFirstName());
        assertEquals(ln, saved.getUser().getLastName());
        assertEquals(specialty, saved.getSpecialization());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void update_ShouldUpdateTrainerDetails() throws AccessDeniedException {
        String username = "john";
        String password = "pass";

        Trainer existing = new Trainer();
        existing.setUser(new User());

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(existing));
        when(trainerRepository.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer updated = trainerService.update(username, password, new Trainer());

        assertNotNull(updated);
        verify(authGuard).checkTrainer(username, password);
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void delete_ShouldRemoveTrainer() throws AccessDeniedException {
        String username = "john";
        String password = "pass";
        Trainer trainer = new Trainer();
        trainer.setUser(new User());

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));

        trainerService.delete(username, password);

        verify(authGuard).checkTrainer(username, password);
        verify(trainerRepository).delete(trainer);
    }

    @Test
    void changePassword_ShouldUpdateUserPassword() {
        String username = "john";
        String oldPwd = "old";
        String newPwd = "new";

        Trainer trainer = new Trainer();
        trainer.setUser(new User());

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));

        trainerService.changePassword(username, oldPwd, newPwd);

        assertEquals(newPwd, trainer.getUser().getPassword());
        verify(trainerRepository).save(trainer);
    }

    @Test
    void getTrainings_ShouldReturnList() throws AccessDeniedException {
        String username = "john";
        String password = "pass";

        when(trainingRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(new Training()));

        List<Training> trainings = trainerService.getTrainings(username, password, null, null, null);

        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());
        verify(authGuard).checkTrainer(username, password);
    }


    @Test
    void findUnassignedTrainees_ShouldReturnFilteredList() {
        String username = "john";
        String password = "pass";

        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        Trainee trainee1 = new Trainee();
        trainee1.setUser(new User());
        Trainee trainee2 = new Trainee();
        trainee2.setUser(new User());
        trainer.setTrainees(List.of(trainee1));

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));
        when(traineeRepository.findAll()).thenReturn(List.of(trainee1, trainee2));

        List<Trainee> available = trainerService.findUnassignedTrainees(username, password);

        assertTrue(available.contains(trainee2));
        assertFalse(available.contains(trainee1));
    }

    @Test
    void updateTrainees_ShouldAssignNewTrainees() {
        String username = "john";
        String password = "pass";

        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        Trainee trainee = new Trainee();
        trainee.setUser(new User());

        when(trainerRepository.findByUser_Username(username)).thenReturn(Optional.of(trainer));
        when(traineeRepository.findByUser_Username(anyString())).thenReturn(Optional.of(trainee));

        trainerService.updateTrainees(username, password, Set.of("traineeUser"));

        verify(trainerRepository).save(trainer);
    }
}
