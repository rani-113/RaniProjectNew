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
            // Get the absolute path to the HTML file
            String currentDir = System.getProperty("user.dir");
            String htmlPath = "file://" + currentDir + "/src/main/resources/webapp/index.html";
            
            BaseClass.navigateToUrl(htmlPath);
            logger.info("Navigated to test application page");
        } catch (Exception e) {
            logger.error("Failed to navigate to test application page: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to test application page", e);
        }
    }
} 