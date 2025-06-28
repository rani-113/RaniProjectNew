package com.automation.pages;

import com.automation.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationPage {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By nameField = By.id("registerName");
    private By emailField = By.id("registerEmail");
    private By usernameField = By.id("registerUsername");
    private By passwordField = By.id("registerPassword");
    private By registerButton = By.cssSelector("#registerForm button[type='submit']");
    private By registerMessage = By.id("registerMessage");
    private By registerTab = By.xpath("//div[contains(@class, 'nav-tab') and normalize-space(text())='Register']");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = BaseClass.getWait();
    }

    public void clickRegisterTab() {
        try {
            // Sleep 1 second to ensure page is loaded
            Thread.sleep(1000);
            // Wait for the Register tab to be present in the DOM
            wait.until(ExpectedConditions.presenceOfElementLocated(registerTab));
            java.util.List<WebElement> tabs = driver.findElements(registerTab);
            if (tabs.isEmpty()) {
                logger.error("Register tab not found! Page source:\n" + driver.getPageSource());
                logger.error("Register tab not found! Cannot proceed with registration.");
                throw new RuntimeException("Register tab not found!");
            }
            WebElement tab = tabs.get(0);
            // Check if the tab is already active
            String classAttr = tab.getAttribute("class");
            if (classAttr != null && classAttr.contains("active")) {
                logger.info("Register tab is already active, skipping click");
                return;
            }
            wait.until(ExpectedConditions.elementToBeClickable(tab));
            tab.click();
            logger.info("Clicked on Register tab");
        } catch (Exception e) {
            logger.error("Failed to click Register tab: " + e.getMessage());
            throw new RuntimeException("Failed to click Register tab", e);
        }
    }

    public void enterName(String name) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(nameField));
            element.clear();
            element.sendKeys(name);
            logger.info("Entered name: " + name);
        } catch (Exception e) {
            logger.error("Failed to enter name: " + e.getMessage());
            throw new RuntimeException("Failed to enter name", e);
        }
    }

    public void enterEmail(String email) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(emailField));
            element.clear();
            element.sendKeys(email);
            logger.info("Entered email: " + email);
        } catch (Exception e) {
            logger.error("Failed to enter email: " + e.getMessage());
            throw new RuntimeException("Failed to enter email", e);
        }
    }

    public void enterUsername(String username) {
        try {
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

    public void clickRegisterButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(registerButton));
            button.click();
            logger.info("Clicked Register button");
        } catch (Exception e) {
            logger.error("Failed to click Register button: " + e.getMessage());
            throw new RuntimeException("Failed to click Register button", e);
        }
    }

    public String getRegisterMessage() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(registerMessage));
            String text = message.getText();
            logger.info("Registration message: " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get registration message: " + e.getMessage());
            throw new RuntimeException("Failed to get registration message", e);
        }
    }

    public boolean isRegistrationSuccessful() {
        String message = getRegisterMessage();
        return message.contains("Registration successful");
    }

    public void register(String name, String email, String username, String password) {
        clickRegisterTab();
        enterName(name);
        enterEmail(email);
        enterUsername(username);
        enterPassword(password);
        clickRegisterButton();
    }
}