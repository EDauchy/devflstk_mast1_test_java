Feature: Product search

  Scenario: Search products by keyword
    Given the search bar is available
    And the product repository returns products for keyword "phone"
    When the user searches for keyword "phone"
    Then the search results should contain 2 products
    And the product repository should have been queried with keyword "phone"

  Scenario: Search products by maximum price
    Given the search bar is available
    And the product repository returns products with max price 50.0
    When the user searches for products with max price 50.0
    Then the search results should contain 1 products
    And the product repository should have been queried with max price 50.0
