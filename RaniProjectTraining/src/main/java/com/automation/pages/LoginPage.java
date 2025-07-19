package com.automation.pages;

import com.automation.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage=By.xpath("//h3[@data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = BaseClass.getWait();
    }


    public void enterUsername(String username) {
        try {
            Thread.sleep(2000);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(usernameField));
            element.clear();
            element.sendKeys(username);
            logger.info("Entered username: " + username);
        } catch (Exception e) {
            logger.error("Failed to enter username: " + e.getMessage());
            throw new RuntimeException("Failed to enter username", e);
        }
    }

    public void enterPassword(String password) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
            element.clear();
            element.sendKeys(password);
            logger.info("Entered password");
        } catch (Exception e) {
            logger.error("Failed to enter password: " + e.getMessage());
            throw new RuntimeException("Failed to enter password", e);
        }
    }

    public void clickLoginButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            button.click();
            logger.info("Clicked Login button");
        } catch (Exception e) {
            logger.error("Failed to click Login button: " + e.getMessage());
            throw new RuntimeException("Failed to click Login button", e);
        }
    }

//    public String getLoginMessage() {
//        try {
//            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated());
//            String text = message.getText();
//            logger.info("Login message: " + text);
//            return text;
//        } catch (Exception e) {
//            logger.error("Failed to get login message: " + e.getMessage());
//            throw new RuntimeException("Failed to get login message", e);
//        }
//    }
    public String getErrorMessage(){
        try{
            WebElement error=wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            String errorText=error.getText();
            logger.info("Login message: " + errorText);
            return errorText;
        } catch (Exception e) {
            logger.error("Failed to get login message: " + e.getMessage());
            throw new RuntimeException("Failed to get login message", e);
        }
    }

//    public boolean isLoginSuccessful() {
//        String message = getLoginMessage();
//        return message.contains("Login successful");
//    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}