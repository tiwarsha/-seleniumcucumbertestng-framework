@e2e
Feature: Hello World application

  Scenario: Greeting from the Spring Boot backend is rendered by the React app
    Given I open the hello world application
    Then the page title contains "Hello World App"
    And the greeting reads "Hello World from Spring Boot"

  Scenario: Greeting is reloaded on demand
    Given I open the hello world application
    When I refresh the greeting
    Then the greeting reads "Hello World from Spring Boot"
