package com.automation.steps;

import com.automation.base.BaseClass;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonSteps {
    
    private static final Logger logger = LoggerFactory.getLogger(CommonSteps.class);
    
    @Given("the user is on the test application page")
    public void the_user_is_on_the_test_application_page() {
        try {
            // Get url

            String url = "https://www.saucedemo.com/";
            
            BaseClass.navigateToUrl(url);
            logger.info("Navigated to test application page");
        } catch (Exception e) {
            logger.error("Failed to navigate to test application page: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to test application page", e);
        }
    }
} 