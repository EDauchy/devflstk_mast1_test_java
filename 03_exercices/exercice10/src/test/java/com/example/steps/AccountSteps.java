package com.example.steps;

import com.example.AccountService;
import com.example.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountSteps {
    private UserRepository userRepository;
    private AccountService accountService;
    private String confirmationMessage;
    private Exception registrationError;

    @Given("the registration form is available")
    public void theRegistrationFormIsAvailable() {
        // Given
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(userRepository);
    }

    @Given("no user exists with username {string}")
    public void noUserExistsWithUsername(String username) {
        // Given
        when(userRepository.existsByUsername(username)).thenReturn(false);
    }

    @Given("a user already exists with username {string}")
    public void aUserAlreadyExistsWithUsername(String username) {
        // Given
        when(userRepository.existsByUsername(username)).thenReturn(true);
    }

    @When("the user registers with email {string}, username {string} and password {string}")
    public void theUserRegisters(String email, String username, String password) {
        // When
        confirmationMessage = accountService.register(email, username, password);
    }

    @When("the user tries to register with email {string}, username {string} and password {string}")
    public void theUserTriesToRegister(String email, String username, String password) {
        // When
        registrationError = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.register(email, username, password)
        );
    }

    @Then("the registration confirmation should be {string}")
    public void theRegistrationConfirmationShouldBe(String expectedMessage) {
        // Then
        assertEquals(expectedMessage, confirmationMessage);
    }

    @Then("the user repository should have saved {string}")
    public void theUserRepositoryShouldHaveSaved(String username) {
        // Then
        verify(userRepository).save(anyString(), eq(username), anyString());
    }

    @Then("a registration error should be returned with message {string}")
    public void aRegistrationErrorShouldBeReturnedWithMessage(String expectedMessage) {
        // Then
        assertEquals(expectedMessage, registrationError.getMessage());
    }
}
