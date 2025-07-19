Feature: Login and Add to Cart Functionality

  As a SauceDemo user
  I want to log in and add items to the cart
  So that I can proceed to checkout

  Background:
    Given user is navigated to demo "https://www.saucedemo.com/"
    When the user enters username "standard_user" and password "secret_sauce"
    And clicks the login button
    Then the user should be navigated to the inventory page

  @Testing
  Scenario: Login and add item to cart
    When the user clicks the add to cart button
    Then user logs out from the page
