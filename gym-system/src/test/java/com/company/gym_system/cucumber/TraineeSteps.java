package com.company.gym_system.cucumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@ScenarioScope
public class TraineeSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> payload;
    private ResultActions result;

    @Given("a valid trainee payload")
    public void a_valid_trainee_payload() {
        payload = new HashMap<>();
        payload.put("firstName", "John");
        payload.put("lastName", "Doe");
        payload.put("address", "Somewhere");
        payload.put("birthDate", LocalDate.of(2000,1,1).toString());
    }

    @Given("a trainee payload missing {string}")
    public void a_trainee_payload_missing(String field) {
        a_valid_trainee_payload();
        payload.remove(field);
    }

    @When("I submit the registration request")
    public void i_submit_the_registration_request() throws Exception {
        String json = objectMapper.writeValueAsString(payload);
        result = mockMvc.perform(post("/api/v1/trainees/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer statusCode) throws Exception {
        assertNotNull(result);
        result.andExpect(status().is(statusCode));
    }

    @Then("the response should contain a trainee id")
    public void the_response_should_contain_a_trainee_id() throws Exception {
        String content = result.andReturn().getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);
        // Accept either {"id": ...} or nested payload; require "id" somewhere at root
        assertTrue(node.has("id"), "Expected response JSON to contain 'id' but was: " + content);
    }

    @Then("the response should contain an error about {string}")
    public void the_response_should_contain_an_error_about(String field) throws Exception {
        String content = result.andReturn().getResponse().getContentAsString();
        assertTrue(content.contains(field), "Expected error message to reference field '" + field + "' but was: " + content);
    }
}
