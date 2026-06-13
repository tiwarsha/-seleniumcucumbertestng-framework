@smoke
Feature: Browser smoke

  Scenario: Open browser and print hello
    Given I open the configured application
    Then I print hello
