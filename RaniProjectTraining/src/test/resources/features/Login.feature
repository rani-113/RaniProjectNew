@regression
Feature: Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access my account

  Background:
    Given the user is on the test application page

  @positive @login
  Scenario: Successful login with valid credentials
    When the user enters login username "testuser"
    And the user enters login password "password123"
    And the user clicks the login button
    Then the user should see login message "Login successful!"

  @negative @login
  Scenario: Failed login with invalid credentials
    When the user enters login username "invaliduser"
    And the user enters login password "wrongpassword"
    And the user clicks the login button
    Then the user should see login message "Invalid credentials!"
