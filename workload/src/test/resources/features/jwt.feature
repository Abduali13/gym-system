Feature: Jwt utility component tests

  As a workload service
  I want to generate and validate JWTs
  So that authenticated flows can be verified

  Scenario: generate and validate token for correct username
    Given a username "system"
    When I generate a token for "system"
    Then validation for "system" should be true

  Scenario: validation fails for different username
    Given a username "system"
    When I generate a token for "system"
    Then validation for "other" should be false

  Scenario: token parsing of malformed token should fail
    Given a malformed token "abc.def.ghi"
    When I validate the malformed token for "system"
    Then validation should be false
