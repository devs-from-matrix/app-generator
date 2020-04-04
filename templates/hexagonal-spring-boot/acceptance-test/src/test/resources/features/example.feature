@Example
Feature: User would like to get examples

  Scenario: User should be able to get all examples
    Given the following examples exists in the library
      | description                 |
      | Twinkle twinkle little star |
    When user requests for all examples
    Then the user gets the following examples
      | description                 |
      | Twinkle twinkle little star |
