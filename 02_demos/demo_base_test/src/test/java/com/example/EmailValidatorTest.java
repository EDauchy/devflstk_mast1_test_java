package com.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de validation des emails")
public class EmailValidatorTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Nested
    @DisplayName("Emails valides")
    class ValidEmails {

        @Test
        @DisplayName("Doit retourner true lorsqu'un email est valide")
        void shouldReturnTrueWhenEmailIsValid() {
            boolean result = emailValidator.isValid("john.doe@example.com");

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("Emails invalides")
    class InvalidEmails {

        @Test
        @DisplayName("Doit retourner false lorsqu'un email ne contient pas arobase")
        void shouldReturnFalseWhenEmailDoesNotContainAtSymbol() {
            boolean result = emailValidator.isValid("john.doe.example.com");

            assertFalse(result);
        }

        @Test
        @DisplayName("Doit retourner false lorsqu'un email est vide")
        void shouldReturnFalseWhenEmailIsBlank() {
            boolean result = emailValidator.isValid("   ");

            assertFalse(result);
        }

        @Test
        @DisplayName("Doit lever une exception lorsqu'un email est null")
        void shouldThrowExceptionWhenEmailIsNull() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> emailValidator.isValid(null)
            );
        }
    }



}
