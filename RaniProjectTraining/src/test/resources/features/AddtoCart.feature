Feature: Login and Add to Cart Functionality

  As a SauceDemo user
  I want to log in and add items to the cart
  So that I can proceed to checkout

  Background:
    Scenario Outline: Pre-requisite
    Given user is navigated to demo "<url>"
    When the user enters username "<username>" and password "<password>"
    Examples:
    |url  | username      | password     |
    | https://www.saucedemo.com/| standard_user | secret_sauce |

@Testing
  Scenario: Login and add item to cart
    Then the user should be navigated to the "Swag Labs"
    #Rework
    When the user clicks to the add to cart button
    #logout
Then user logged out from the page
