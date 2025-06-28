package com.automation.steps;

import com.automation.base.BaseClass;
import com.automation.pages.RegistrationPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class RegistrationSteps {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrationSteps.class);
    private WebDriver driver;
    private RegistrationPage registrationPage;
    
    @Before
    public void setUp() {
        BaseClass.initializeDriver();
        driver = BaseClass.getDriver();
        registrationPage = new RegistrationPage(driver);
    }
    
    @After
    public void tearDown() {
        BaseClass.quitDriver();
    }
    
    @When("the user clicks on the register tab")
    public void the_user_clicks_on_the_register_tab() {
        try {
            registrationPage.clickRegisterTab();
        } catch (Exception e) {
            logger.error("Failed to click register tab: " + e.getMessage());
            throw new RuntimeException("Failed to click register tab", e);
        }
    }
    
    @When("the user enters full name {string}")
    public void the_user_enters_full_name(String name) {
        try {
            registrationPage.enterName(name);
        } catch (Exception e) {
            logger.error("Failed to enter full name: " + e.getMessage());
            throw new RuntimeException("Failed to enter full name", e);
        }
    }
    
    @When("the user enters email {string}")
    public void the_user_enters_email(String email) {
        try {
            registrationPage.enterEmail(email);
        } catch (Exception e) {
            logger.error("Failed to enter email: " + e.getMessage());
            throw new RuntimeException("Failed to enter email", e);
        }
    }
    
    @When("the user enters registration username {string}")
    public void the_user_enters_registration_username(String username) {
        try {
            registrationPage.enterUsername(username);
        } catch (Exception e) {
            logger.error("Failed to enter registration username: " + e.getMessage());
            throw new RuntimeException("Failed to enter registration username", e);
        }
    }
    
    @When("the user enters registration password {string}")
    public void the_user_enters_registration_password(String password) {
        try {
            registrationPage.enterPassword(password);
        } catch (Exception e) {
            logger.error("Failed to enter registration password: " + e.getMessage());
            throw new RuntimeException("Failed to enter registration password", e);
        }
    }
    
    @When("the user clicks the register button")
    public void the_user_clicks_the_register_button() {
        try {
            registrationPage.clickRegisterButton();
        } catch (Exception e) {
            logger.error("Failed to click register button: " + e.getMessage());
            throw new RuntimeException("Failed to click register button", e);
        }
    }
    
    @Then("the user should see registration message {string}")
    public void the_user_should_see_registration_message(String expectedMessage) {
        try {
            String actualMessage = registrationPage.getRegisterMessage();
            Assert.assertTrue(actualMessage.contains(expectedMessage), 
                "Expected message '" + expectedMessage + "' but got '" + actualMessage + "'");
            logger.info("Registration message verification successful: " + expectedMessage);
        } catch (Exception e) {
            logger.error("Failed to verify registration message: " + e.getMessage());
            throw new RuntimeException("Failed to verify registration message", e);
        }
    }
} 