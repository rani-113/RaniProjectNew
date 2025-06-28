package com.automation.steps;

import com.automation.base.BaseClass;
import com.automation.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
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
    
    @When("the user enters login username {string}")
    public void the_user_enters_login_username(String username) {
        try {
            loginPage.enterUsername(username);
        } catch (Exception e) {
            logger.error("Failed to enter login username: " + e.getMessage());
            throw new RuntimeException("Failed to enter login username", e);
        }
    }
    
    @When("the user enters login password {string}")
    public void the_user_enters_login_password(String password) {
        try {
            loginPage.enterPassword(password);
        } catch (Exception e) {
            logger.error("Failed to enter login password: " + e.getMessage());
            throw new RuntimeException("Failed to enter login password", e);
        }
    }
    
    @When("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        try {
            loginPage.clickLoginButton();
        } catch (Exception e) {
            logger.error("Failed to click login button: " + e.getMessage());
            throw new RuntimeException("Failed to click login button", e);
        }
    }

    @Then("the user should see login message {string}")
    public void the_user_should_see_login_message(String expectedMessage) {
        try {
            String actualMessage = loginPage.getLoginMessage();
            Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' but got '" + actualMessage + "'");
            logger.info("Login message verification successful: " + expectedMessage);
        } catch (Exception e) {
            logger.error("Failed to verify login message: " + e.getMessage());
            throw new RuntimeException("Failed to verify login message", e);
        }
    }
} 