Feature: Login and Add to Cart Functionality

  As a SauceDemo user
  I want to log in and add items to the cart
  So that I can proceed to checkout

  @AddCart
  Scenario: Login and add item to cart
    When the user enters username "standard_user" and password "secret_sauce"
    And clicks the login button
    Then the user should be navigated to the inventory page
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should display "1"
