Feature: Trainee registration (component tests)

  As a client of the Gym System API
  I want to register trainees
  So that valid registrations succeed and invalid payloads return errors

  Scenario: Successful trainee registration
    Given a valid trainee payload
    When I submit the registration request
    Then the response status should be 200
    And the response should contain a trainee id

  Scenario: Registration fails when required field is missing
    Given a trainee payload missing "firstName"
    When I submit the registration request
    Then the response status should be 400
    And the response should contain an error about "firstName"
