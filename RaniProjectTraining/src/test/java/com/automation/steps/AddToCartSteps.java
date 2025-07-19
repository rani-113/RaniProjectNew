package com.automation.steps;

import com.automation.base.BaseClass;
import com.automation.pages.AddToCartPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class AddToCartSteps {

    private static final Logger logger = LoggerFactory.getLogger(AddToCartSteps.class);
    private WebDriver driver;
    private AddToCartPage addToCartPage;

    @Before
    public void setUp() {
        BaseClass.initializeDriver();
        driver = BaseClass.getDriver();
        addToCartPage = new AddToCartPage(driver);
    }


    @After
    public void tearDown() {
        BaseClass.quitDriver();
    }

    @When("the user clicks the add to cart button")
    public void theUserClicksTheAddToCartButton() {
        try {
            addToCartPage.clickAddToCartButton();

        } catch (Exception e) {
            logger.error("Failed to click Addtocart button: " + e.getMessage());
            throw new RuntimeException("Failed to click Addtocart button", e);
        }
    }

    @Then("user logs out from the page")
    public void userLogsOutFromThePage() {
        try {
            addToCartPage.clickMenuButton();
            addToCartPage.clickLogoutButton();

        } catch (Exception e) {
            logger.error("Failed to click logout button: " + e.getMessage());
            throw new RuntimeException("Failed to click logout button", e);
        }
    }
}
