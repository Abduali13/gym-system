package com.company.gym_system;

import com.company.gym_system.config.SecurityConfig;
import com.company.gym_system.service.GymFacade;
import com.company.gym_system.controller.AuthController;
import com.company.gym_system.controller.TraineeController;
import com.company.gym_system.controller.TrainerController;
import com.company.gym_system.controller.TrainingController;
import com.company.gym_system.controller.TrainingTypeController;
import com.company.gym_system.dto.*;
import com.company.gym_system.entity.*;
import com.company.gym_system.security.AuthGuard;
import com.company.gym_system.service.TraineeService;
import com.company.gym_system.service.TrainerService;
import com.company.gym_system.util.TransactionLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({TraineeController.class, TrainerController.class, TrainingController.class, TrainingTypeController.class, AuthController.class})
@TestPropertySource(properties = "spring.security.jwt.secret=testSecretKeyForJWT")
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class GymFacadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GymFacade gymFacade;
    @MockitoBean
    private TraineeService traineeService;
    @MockitoBean
    private TrainerService trainerService;
    @MockitoBean
    private AuthGuard authGuard;
    @MockitoBean
    private TransactionLogger txLogger;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerTrainee_shouldReturnRegistrationDto() throws Exception {
        TraineeRegistrationRequestDto req = TraineeRegistrationRequestDto.builder()
                .firstName("John").lastName("Doe").birthDate(LocalDate.of(2000, 1, 1)).address("XX").build();
        TraineeRegistrationResponseDto resp = new TraineeRegistrationResponseDto();
        given(txLogger.startTransaction(any(), any())).willReturn("txid");
        given(gymFacade.registerTrainee(any())).willReturn(resp);

        mockMvc.perform(post("/api/v1/trainees/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void listTrainees_shouldReturnList() throws Exception {
        given(gymFacade.listAllTrainees()).willReturn(Collections.singletonList(new Trainee()));

        mockMvc.perform(get("/api/v1/trainees/list-all-trainees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateTrainee_shouldReturnUpdated() throws Exception {
        TraineeUpdateRequestDto updates = TraineeUpdateRequestDto.builder()
                .username("john").password("pass").firstName("John").lastName("Doe")
                .birthDate(LocalDate.of(2000, 1, 2)).address("nowhere").build();
        TraineeUpdateResponseDto resp = new TraineeUpdateResponseDto();
        given(gymFacade.updateTrainee(any())).willReturn(resp);

        mockMvc.perform(put("/api/v1/trainees/update-trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTrainee_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(gymFacade).deleteTrainee(anyString(), anyString());

        mockMvc.perform(delete("/api/v1/trainees/delete-trainee")
                        .param("username", "john")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void getTraineeByUsername_shouldReturnTrainee() throws Exception {
        given(gymFacade.getTraineeByUsername(anyString())).willReturn(new Trainee());

        mockMvc.perform(get("/api/v1/trainees/trainee").param("username", "john"))
                .andExpect(status().isOk());
    }

    @Test
    void getTraineeTrainings_shouldReturnList() throws Exception {
        given(gymFacade.getTraineeTrainings(anyString(), anyString(), any(), any(), any(), any()))
                .willReturn(Collections.singletonList(new Training()));

        mockMvc.perform(get("/api/v1/trainees/trainee-trainings")
                        .param("username", "john")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void findAvailableTrainers_shouldReturnList() throws Exception {
        given(gymFacade.findAvailableTrainers(anyString(), anyString()))
                .willReturn(Collections.singletonList(new TrainerShortProfileDto()));

        mockMvc.perform(get("/api/v1/trainers/active-unassigned-trainers")
                        .param("username", "john").param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTraineeTrainers_shouldReturnList() throws Exception {
        Set<String> trainerUsernames = new HashSet<>(Set.of("trainer1"));
        given(gymFacade.updateTraineeTrainers(anyString(), anyString(), anySet()))
                .willReturn(Collections.singletonList(new TrainerListResponseDto()));

        mockMvc.perform(put("/api/v1/trainees/update-trainee-trainers")
                        .param("username", "john")
                        .param("password", "pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk());
    }

    @Test
    void registerTrainer_shouldReturnRegistrationDto() throws Exception {
        TrainerRegistrationRequestDto req = TrainerRegistrationRequestDto.builder().firstName("T").lastName("L").specialization("Yoga").build();
        TrainerRegistrationResponseDto resp = new TrainerRegistrationResponseDto();
        given(txLogger.startTransaction(any(), any())).willReturn("txid");
        given(gymFacade.registerTrainer(any())).willReturn(resp);

        mockMvc.perform(post("/api/v1/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void listTrainers_shouldReturnList() throws Exception {
        given(gymFacade.listAllTrainers()).willReturn(Collections.singletonList(new Trainer()));

        mockMvc.perform(get("/api/v1/trainers/list-all-trainers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateTrainer_shouldReturnUpdatedDto() throws Exception {
        TrainerUpdateRequestDto updates = TrainerUpdateRequestDto.builder()
                .username("trainer1").firstName("T").lastName("L").specialization("Boxing").build();
        TrainerUpdateResponseDto resp = TrainerUpdateResponseDto.builder().username("trainer1").build();
        given(gymFacade.updateTrainer(any())).willReturn(resp);

        mockMvc.perform(put("/api/v1/trainers/update-trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk());
    }

    @Test
    void changeTraineePassword_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(gymFacade).changeTraineePassword(anyString(), anyString(), anyString());

        mockMvc.perform(put("/api/v1/trainees/change-trainee-password")
                        .param("username", "john")
                        .param("oldPassword", "old")
                        .param("newPassword", "new"))
                .andExpect(status().isOk());
    }

    @Test
    void activateTrainee_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(gymFacade).activateTrainee(anyString(), anyString(), anyBoolean());

        mockMvc.perform(put("/api/v1//trainees/activate-trainee")
                        .param("username", "john")
                        .param("password", "pass")
                        .param("active", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void changeTrainerPassword_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(gymFacade).changeTrainerPassword(anyString(), anyString(), anyString());

        mockMvc.perform(put("/api/v1/trainers/change-trainer-password")
                        .param("username", "trainer1")
                        .param("oldPassword", "old")
                        .param("newPassword", "new"))
                .andExpect(status().isOk());
    }

    @Test
    void activateTrainer_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(gymFacade).activateTrainer(anyString(), anyString(), anyBoolean());

        mockMvc.perform(put("/api/v1/trainers/activate-trainer")
                        .param("username", "trainer1")
                        .param("password", "pass")
                        .param("active", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTrainer_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(gymFacade).deleteTrainer(anyString(), anyString());

        mockMvc.perform(delete("/api/v1/trainers/delete-trainer")
                        .param("username", "trainer1")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void getTrainer_shouldReturnTrainerGetResponseDto() throws Exception {
        TrainerGetResponseDto resp = TrainerGetResponseDto.builder().firstName("trainer1").build();
        given(gymFacade.getTrainerByUsername(anyString(), anyString())).willReturn(resp);

        mockMvc.perform(get("/api/v1/trainers/trainer")
                        .param("username", "trainer1")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void getTrainerTrainings_shouldReturnList() throws Exception {
        given(gymFacade.getTrainerTrainings(anyString(), anyString(), any(), any(), any()))
                .willReturn(Collections.singletonList(new TrainingGetWithTrainerDto()));

        mockMvc.perform(get("/api/v1/trainers/trainer-trainings")
                        .param("username", "trainer1")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void findUnassignedTrainees_shouldReturnList() throws Exception {
        given(gymFacade.findUnassignedTrainees(anyString(), anyString()))
                .willReturn(Collections.singletonList(new Trainee()));

        mockMvc.perform(get("/api/v1/trainees/unassigned-trainees")
                        .param("username", "trainer1")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTrainerTrainees_shouldReturnOk() throws Exception {
        Set<String> traineeUsernames = new HashSet<>(Set.of("trainee1"));
        Mockito.doNothing().when(gymFacade).updateTrainerTrainees(anyString(), anyString(), anySet());

        mockMvc.perform(put("/api/v1/trainers/update-trainer-trainees")
                        .param("username", "trainer1")
                        .param("password", "pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeUsernames)))
                .andExpect(status().isOk());
    }

    @Test
    void addTraining_shouldReturnOk() throws Exception {
        Training dummyTraining = new Training();
        Mockito.doNothing().when(gymFacade).addTraining(any());

        mockMvc.perform(post("/api/v1/trainings/add-training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyTraining)))
                .andExpect(status().isOk());
    }

    @Test
    void listTraining_shouldReturnList() throws Exception {
        given(gymFacade.listAllTrainings()).willReturn(Collections.singletonList(new Training()));

        mockMvc.perform(get("/api/v1/trainings/list-all-training"))
                .andExpect(status().isOk());
    }

    @Test
    void login_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(authGuard).checkAny(anyString(), anyString());
        given(txLogger.startTransaction(any(), any())).willReturn("txid");

        mockMvc.perform(get("/api/v1/auth/login")
                        .param("username", "user")
                        .param("password", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void login_shouldReturnForbidden_onAccessDenied() throws Exception {
        given(txLogger.startTransaction(any(), any())).willReturn("txid");
        Mockito.doThrow(new java.nio.file.AccessDeniedException("fail"))
                .when(authGuard).checkAny(anyString(), anyString());

        mockMvc.perform(get("/api/v1/auth/login")
                        .param("username", "user")
                        .param("password", "pass"))
                .andExpect(status().isForbidden());
    }

    @Test
    void resetPassword_shouldReturnOkIfTrainee() throws Exception {
        given(txLogger.startTransaction(any(), any())).willReturn("txid");
        given(traineeService.getTraineeByUsername(anyString())).willReturn(new Trainee());

        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .param("username", "john")
                        .param("newPassword", "pass"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_shouldReturnOkIfTrainer() throws Exception {
        given(txLogger.startTransaction(any(), any())).willReturn("txid");
        given(traineeService.getTraineeByUsername(anyString())).willReturn(null);
        given(trainerService.getTrainerByUsername(anyString(), anyString())).willReturn(new TrainerGetResponseDto());

        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .param("username", "trainer1")
                        .param("newPassword", "trainerpass"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_shouldReturnNotFoundIfNotFound() throws Exception {
        given(txLogger.startTransaction(any(), any())).willReturn("txid");
        given(traineeService.getTraineeByUsername(anyString())).willReturn(null);
        given(trainerService.getTrainerByUsername(anyString(), anyString())).willReturn(null);

        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .param("username", "missing")
                        .param("newPassword", "none"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTrainingTypes_shouldReturnList() throws Exception {
        given(gymFacade.getTrainingTypes()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/training-types/list-all"))
                .andExpect(status().isOk());
    }


}
