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

public class AddToCartSteps {
    
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
    
   @When("the user clicks to the add to cart button")
    public void theUserClicksToTheAddToCartButton() {
        try {
            addToCartPage.clickAddToCartButton();
        } catch (Exception e) {
            logger.error("Failed to click login button: " + e.getMessage());
            throw new RuntimeException("Failed to click login button", e);
        }
    }

    @Then("the user should be navigated to the {string}")
    public void theUserShouldBeNavigatedToThe(String expectedTitle) throws InterruptedException {

        String actual = addToCartPage.getHomePageTitle();
        Assert.assertEquals(actual,expectedTitle);
//        boolean isPresent = false;
 //        if (actual.contains(expectedUrl)) {
//isPresent=true
//        }
  //          Assert.assertTrue(isPresent, "Url is not containing the desired value");
        }

    @Then("user logged out from the page")
    public void userLoggedoutFromThePage() {
        try {
            addToCartPage.clickMenuButton();
            addToCartPage.clickLogoutButton();

        }catch (Exception e){

        }
    }
} 
