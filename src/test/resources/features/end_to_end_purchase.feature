@e2e
Feature: End to end purchase on the AskOmDch store

  As a shopper
  I want to search for a product, add it to the cart and check out
  So that I can complete an order

  Background:
    Given I am on the store page

  @regression
  Scenario Outline: Search a product, add it to the cart and place the order
    When I search for "<searchTerm>"
    Then the product "<product>" is listed in the search results
    When I add the product "<product>" to the cart
    And I open the cart
    Then the cart contains the product "<product>"
    When I set the cart quantity to <quantity>
    And I proceed to checkout
    And I fill in the billing details
    And I place the order with direct bank transfer
    Then the order is placed successfully

    Examples:
      | searchTerm | product     | quantity |
      | Blue       | Blue Tshirt | 2        |
