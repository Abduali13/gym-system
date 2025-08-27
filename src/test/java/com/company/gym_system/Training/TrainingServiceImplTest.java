//package com.company.gym_system.Training;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import com.company.gym_system.security.AuthGuard;
//import com.company.gym_system.entity.*;
//import com.company.gym_system.repository.TraineeRepository;
//import com.company.gym_system.repository.TrainerRepository;
//import com.company.gym_system.repository.TrainingRepository;
//import com.company.gym_system.repository.TrainingTypeRepository;
//import com.company.gym_system.service.impl.TrainingServiceImpl;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//class TrainingServiceImplTest {
//
//    @Mock
//    private TraineeRepository traineeRepository;
//    @Mock
//    private TrainerRepository trainerRepository;
//    @Mock
//    private TrainingRepository trainingRepository;
//    @Mock
//    private TrainingTypeRepository trainingTypeRepository;
//    @Mock
//    private AuthGuard authGuard;
//
//    @InjectMocks
//    private TrainingServiceImpl trainingService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void create_ShouldSaveTrainingAndReturn() {
//        Training training = new Training();
//        when(trainingRepository.save(training)).thenReturn(training);
//
//        Training saved = trainingService.create(training);
//
//        assertNotNull(saved);
//        verify(trainingRepository).save(training);
//    }
//
//    @Test
//    void get_ShouldReturnTrainingWhenFound() {
//        Training training = new Training();
//        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));
//
//        Training found = trainingService.get(1L);
//
//        assertNotNull(found);
//        assertEquals(training, found);
//    }
//
//    @Test
//    void listAll_ShouldReturnAllTrainings() {
//        when(trainingRepository.findAll()).thenReturn(List.of(new Training(), new Training()));
//
//        List<Training> list = trainingService.listAll();
//
//        assertEquals(2, list.size());
//        verify(trainingRepository).findAll();
//    }
//
//    @Test
//    void addTraining_ShouldSaveTrainingWithCorrectRelations() throws java.nio.file.AccessDeniedException {
//        String traineeUsername = "traineeUser";
//        String trainerUsername = "trainerUser";
//        LocalDate date = LocalDate.now();
//        int duration = 60;
//        String typeName = "Cardio";
//
//        Trainer trainer = new Trainer();
//        trainer.setUser(new User());
//        Trainee trainee = new Trainee();
//        trainee.setUser(new User());
//        TrainingType trainingType = new TrainingType();
//        trainingType.setTrainingTypeName(typeName);
//
//        when(trainerRepository.findByUser_Username(trainerUsername)).thenReturn(Optional.of(trainer));
//        when(traineeRepository.findByUser_Username(traineeUsername)).thenReturn(Optional.of(trainee));
//        when(trainingTypeRepository.findByTrainingTypeName(typeName)).thenReturn(Optional.of(trainingType));
//        when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Training result = trainingService.addTraining(
//                trainerUsername, "password",
//                traineeUsername, trainerUsername,
//                date, duration, typeName);
//
//        assertNotNull(result.getTrainer());
//        assertNotNull(result.getTrainee());
//        assertNotNull(result.getTrainingType());
//        verify(trainingRepository).save(any(Training.class));
//    }
//
//    @Test
//    void addTraining_ShouldThrowIfTrainingTypeNotFound() {
//        when(trainingTypeRepository.findByTrainingTypeName("Unknown")).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> trainingService.addTraining(
//                "trainerUser", "password",
//                "traineeUser", "trainerUser",
//                LocalDate.now(), 30, "Unknown"));
//    }
//
//    @Test
//    void addTraining_ShouldThrowIfAuthFails() throws java.nio.file.AccessDeniedException {
//        doThrow(new RuntimeException("Denied"))
//                .when(authGuard).checkTrainer(anyString(), anyString());
//
//        assertThrows(RuntimeException.class, () -> trainingService.addTraining(
//                "trainerUser", "badPwd",
//                "traineeUser", "trainerUser",
//                LocalDate.now(), 30, "Cardio"));
//    }
//}
