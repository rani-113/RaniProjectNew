@sanity @regression
Feature: Registration Functionality
  As a new user
  I want to be able to register for a new account
  So that I can create my profile

  Background:
    Given the user is on the test application page

  @positive @registration
  Scenario: Successful registration with valid data
    When the user clicks on the register tab
    And the user enters full name "John Doe"
    And the user enters email "john.doe@example.com"
    And the user enters registration username "johndoe"
    And the user enters registration password "password123"
    And the user clicks the register button
    Then the user should see registration message "Registration successful!"

  @negative @registration
  Scenario: Failed registration with empty fields
    When the user clicks on the register tab
    And the user enters full name ""
    And the user enters email ""
    And the user enters registration username ""
    And the user enters registration password ""
    And the user clicks the register button
    Then the user should see registration message "Please fill all fields!"
