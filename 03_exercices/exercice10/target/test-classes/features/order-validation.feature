Feature: Order validation

  Scenario: Validate an existing order
    Given cart "cart-6" exists
    When the user validates order for cart "cart-6"
    Then the order confirmation should be "Order validated successfully for cart cart-6"
    And cart "cart-6" should be validated

  Scenario: Validation fails when cart does not exist
    Given cart "cart-missing-3" does not exist
    When the user tries to validate order for cart "cart-missing-3"
    Then a cart error should be returned with message "Cart does not exist"
