package com.automation.steps;

import com.automation.base.BaseClass;
import com.automation.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class LoginSteps {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
    private WebDriver driver;
    private LoginPage loginPage;
    
    @Before
    public void setUp() {
        BaseClass.initializeDriver();
        driver = BaseClass.getDriver();
        loginPage = new LoginPage(driver);
        
    }
    
    @After
    public void tearDown() {
        BaseClass.quitDriver();
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersUsernameAndPassword(String username, String password) {
        loginPage.login(username,password);
    }


    @And("clicks the login button")
    public void clicksTheLoginButton() {
        try {
            loginPage.clickLoginButton();
        } catch (Exception e) {
            logger.error("Failed to click login button: " + e.getMessage());
            throw new RuntimeException("Failed to click login button", e);
        }
    }

    @Then("the user should be navigated to the inventory page")
    public void theUserShouldBeNavigatedToTheInventoryPage() {
try{
    String expectedUrl = "https://www.saucedemo.com/inventory.html";
    String actualUrl = driver.getCurrentUrl();
    Assert.assertEquals("User is not navigated to inventory page", expectedUrl, actualUrl);
} catch (Exception e) {
    logger.error("Failed to verify navigation to inventory page: " + e.getMessage());
    throw new RuntimeException("Failed to verify navigation to inventory page", e);
}

    }

    @Then("an error message should be displayed saying {string}")
    public void anErrorMessageShouldBeDisplayedSaying(String expectedErrorMessage) {
        try{
           String actualErrorMessage= loginPage.getErrorMessage();
            Assert.assertEquals("Error message does not match", expectedErrorMessage, actualErrorMessage);
        }catch (Exception e) {
            logger.error("Failed to verify error message: " + e.getMessage());
            throw new RuntimeException("Failed to verify error message", e);
        }
    }
}
