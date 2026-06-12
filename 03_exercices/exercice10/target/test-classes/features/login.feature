Feature: User login

  Scenario: Successful login redirects to home page
    Given the login form is available
    And user "carol" can authenticate with password "Secret1!"
    When the user logs in with username "carol" and password "Secret1!"
    Then the login should be successful
    And the user should be redirected to the home page
    And the login message should be "Welcome carol"

  Scenario: Failed login shows error message
    Given the login form is available
    And user "dave" cannot authenticate with password "WrongPass!"
    When the user logs in with username "dave" and password "WrongPass!"
    Then the login should fail
    And the login message should be "Invalid username or password"
    And the user should not be redirected to the home page
