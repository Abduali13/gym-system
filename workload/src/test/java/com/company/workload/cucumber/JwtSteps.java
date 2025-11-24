package com.company.workload.cucumber;

import com.company.workload.util.JwtUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
@ScenarioScope
public class JwtSteps {

    @Autowired
    private JwtUtil jwtUtil;

    private String username;
    private String token;
    private boolean lastValidation;

    @Given("a username {string}")
    public void a_username(String username) {
        this.username = username;
    }

    @When("I generate a token for {string}")
    public void i_generate_a_token_for(String u) {
        this.token = jwtUtil.generateToken(u);
        assertNotNull(this.token);
    }

    @Then("validation for {string} should be true")
    public void validation_for_should_be_true(String expected) {
        assertTrue(jwtUtil.validateToken(token, expected));
    }

    @Then("validation for {string} should be false")
    public void validation_for_should_be_false(String expected) {
        assertFalse(jwtUtil.validateToken(token, expected));
    }

    @Given("a malformed token {string}")
    public void a_malformed_token(String t) {
        this.token = t;
    }

    @When("I validate the malformed token for {string}")
    public void i_validate_the_malformed_token_for(String expected) {
        this.lastValidation = jwtUtil.validateToken(token, expected);
    }

    @Then("validation should be false")
    public void validation_should_be_false() {
        assertFalse(lastValidation);
    }
}
