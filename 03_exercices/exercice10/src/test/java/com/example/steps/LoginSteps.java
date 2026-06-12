package com.example.steps;

import com.example.AuthService;
import com.example.LoginResult;
import com.example.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginSteps {
    private UserRepository userRepository;
    private AuthService authService;
    private LoginResult loginResult;

    @Given("the login form is available")
    public void theLoginFormIsAvailable() {
        // Given
        userRepository = mock(UserRepository.class);
        authService = new AuthService(userRepository);
    }

    @Given("user {string} can authenticate with password {string}")
    public void userCanAuthenticate(String username, String password) {
        // Given
        when(userRepository.authenticate(username, password)).thenReturn(true);
    }

    @Given("user {string} cannot authenticate with password {string}")
    public void userCannotAuthenticate(String username, String password) {
        // Given
        when(userRepository.authenticate(username, password)).thenReturn(false);
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsIn(String username, String password) {
        // When
        loginResult = authService.login(username, password);
    }

    @Then("the login should be successful")
    public void theLoginShouldBeSuccessful() {
        // Then
        assertTrue(loginResult.isSuccess());
    }

    @Then("the login should fail")
    public void theLoginShouldFail() {
        // Then
        assertFalse(loginResult.isSuccess());
    }

    @Then("the user should be redirected to the home page")
    public void theUserShouldBeRedirectedToHomePage() {
        // Then
        assertTrue(loginResult.isRedirectedToHome());
    }

    @Then("the user should not be redirected to the home page")
    public void theUserShouldNotBeRedirectedToHomePage() {
        // Then
        assertFalse(loginResult.isRedirectedToHome());
    }

    @Then("the login message should be {string}")
    public void theLoginMessageShouldBe(String expectedMessage) {
        // Then
        assertEquals(expectedMessage, loginResult.getMessage());
    }
}
