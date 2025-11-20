package com.company.gym_system.cucumber;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Component
@ScenarioScope
public class IntegrationSteps {

    @Autowired
    private MockMvc mockMvc;

    private String lastBody;

    @When("I request actuator health of {string}")
    public void i_request_actuator_health_of(String service) throws Exception {
        MvcResult result = mockMvc.perform(get("/actuator/health")).andReturn();
        lastBody = result.getResponse().getContentAsString();
    }

    @When("I request actuator health of {string} at {string}")
    public void i_request_actuator_health_of_at(String service, String url) {
        RestTemplate rt = new RestTemplate();
        try {
            lastBody = rt.getForObject(url, String.class);
        } catch (Exception ex) {
            lastBody = ex.getMessage() == null ? "" : ex.getMessage();
        }
    }

    @Then("the health status should contain {string}")
    public void the_health_status_should_contain(String expected) {
        assertTrue(lastBody != null && lastBody.contains(expected), "Expected to contain '" + expected + "' but was: " + lastBody);
    }

    @Then("the health status should not contain {string}")
    public void the_health_status_should_not_contain(String expected) {
        assertFalse(lastBody != null && lastBody.contains(expected), "Expected NOT to contain '" + expected + "' but was: " + lastBody);
    }
}
