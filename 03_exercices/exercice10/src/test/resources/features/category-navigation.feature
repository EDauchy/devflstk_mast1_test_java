Feature: Category navigation

  Scenario: Browse products by category
    Given the categories page is available
    And the product repository returns products for category "Electronics"
    When the user selects category "Electronics"
    Then the category results should contain 2 products
    And the product repository should have been queried with category "Electronics"
