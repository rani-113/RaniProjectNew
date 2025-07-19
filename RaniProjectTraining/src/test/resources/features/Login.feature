Feature: SauceDemo Login

  As a user of the SauceDemo application
  I want to test login functionality with valid and invalid credentials
  So that I can ensure authentication works properly

  Background:
    Given user is navigated to demo "https://www.saucedemo.com/"

  @Positive
  Scenario: Login with valid credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And clicks the login button
    Then the user should be navigated to the inventory page

  @Negative
  Scenario: Login with locked out user
    When the user enters username "locked_out_user" and password "secret_sauce"
    And clicks the login button
    Then an error message should be displayed saying "Epic sadface: Sorry, this user has been locked out."
