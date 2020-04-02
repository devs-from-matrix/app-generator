@ExamplePoetry
Feature: User would like to get verses from poetry

  Scenario: User should be able to get the verses from the poetry library
    Given the following poem exists in the library
      | poem                        |
      | Twinkle twinkle little star |
    When user requests for verses
    Then the user gets the following verse from poetry library
      | poem                        |
      | Twinkle twinkle little star |
