Feature: Add product to cart

  Scenario: Add a new product to cart
    Given cart "cart-1" exists
    And product "P001" exists
    And product "P001" is not in cart "cart-1"
    When the user adds product "P001" to cart "cart-1"
    Then the add to cart confirmation should be "Product P001 added to cart"
    And cart "cart-1" should contain product "P001" with quantity 1

  Scenario: Increase quantity when product already in cart
    Given cart "cart-2" exists
    And product "P002" exists
    And product "P002" is already in cart "cart-2" with quantity 2
    When the user adds product "P002" to cart "cart-2"
    Then the add to cart confirmation should be "Product P002 added to cart"
    And cart "cart-2" should contain product "P002" with quantity 3

  Scenario: Add to cart fails when cart does not exist
    Given cart "cart-missing" does not exist
    And product "P003" exists
    When the user tries to add product "P003" to cart "cart-missing"
    Then a cart error should be returned with message "Cart does not exist"
