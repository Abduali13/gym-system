Feature: Integration smoke tests between microservices

  As a platform operator
  I want to verify that each microservice reports healthy status
  So that integration readiness is validated

  Scenario: actuator health of gym-system is UP
    When I request actuator health of "gym-system"
    Then the health status should contain "UP"

  Scenario: actuator health of workload-service is UP (requires workload running)
    When I request actuator health of "workload-service" at "http://localhost:8081/actuator/health"
    Then the health status should contain "UP"

  Scenario: remote service unreachable reported as not UP
    When I request actuator health of "workload-service" at "http://localhost:9999/actuator/health"
    Then the health status should not contain "UP"
