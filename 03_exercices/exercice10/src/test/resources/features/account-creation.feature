Feature: Account creation

  Scenario: Successful account registration
    Given the registration form is available
    And no user exists with username "alice"
    When the user registers with email "alice@example.com", username "alice" and password "Secret1!"
    Then the registration confirmation should be "Account created successfully for alice"
    And the user repository should have saved "alice"

  Scenario: Registration fails when username already exists
    Given the registration form is available
    And a user already exists with username "bob"
    When the user tries to register with email "bob2@example.com", username "bob" and password "Secret1!"
    Then a registration error should be returned with message "Username already exists"
