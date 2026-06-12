Feature: Remove product from cart

  Scenario: Decrease quantity when product quantity is greater than one
    Given cart "cart-3" exists
    And product "P004" is already in cart "cart-3" with quantity 3
    When the user removes product "P004" from cart "cart-3"
    Then cart "cart-3" should have product "P004" quantity decreased

  Scenario: Remove product when quantity equals one
    Given cart "cart-4" exists
    And product "P005" is already in cart "cart-4" with quantity 1
    When the user removes product "P005" from cart "cart-4"
    Then product "P005" should be removed from cart "cart-4"

  Scenario: Remove fails when product is not in cart
    Given cart "cart-5" exists
    And product "P006" is not in cart "cart-5"
    When the user tries to remove product "P006" from cart "cart-5"
    Then a cart error should be returned with message "Product not in cart"

  Scenario: Remove fails when cart does not exist
    Given cart "cart-missing-2" does not exist
    When the user tries to remove product "P007" from cart "cart-missing-2"
    Then a cart error should be returned with message "Cart does not exist"
