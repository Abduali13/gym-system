//package com.company.workload.api;
//
//import com.company.workload.TrainerWorkloadApplication;
//import com.company.workload.util.JwtUtil;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = TrainerWorkloadApplication.class)
//@AutoConfigureMockMvc
//class WorkloadControllerSecurityTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Test
//    void whenNoToken_thenUnauthorized() throws Exception {
//        mockMvc.perform(get("/workloads/john?year=2024&month=10"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void whenValidToken_thenPostOk() throws Exception {
//        String token = jwtUtil.generateToken("gym-system");
//        String json = "{\n" +
//                "  \"trainerUsername\": \"john\",\n" +
//                "  \"trainerFirstName\": \"John\",\n" +
//                "  \"trainerLastName\": \"Doe\",\n" +
//                "  \"active\": true,\n" +
//                "  \"trainingDate\": \"2024-10-05\",\n" +
//                "  \"trainingDuration\": 45,\n" +
//                "  \"action\": \"ADD\"\n" +
//                "}";
//
//        mockMvc.perform(post("/workloads")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                        .content(json))
//                .andExpect(status().isOk());
//    }
//}
