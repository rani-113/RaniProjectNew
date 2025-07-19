package com.automation.pages;

import com.automation.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddToCartPage {

    private static final Logger logger = LoggerFactory.getLogger(AddToCartPage.class);
    private WebDriver driver;
   private WebDriverWait wait;
//Locators
    private By addToCartButton = By.xpath("//button[@name='add-to-cart-sauce-labs-backpack']");
    private By menuButton= By.id("react-burger-menu-btn");
    private By logoutButton=By.id("logout_sidebar_link");

    public AddToCartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = BaseClass.getWait();
    }

public void clickAddToCartButton(){
    try {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        button.click();

        logger.info("Clicked Login button");
    } catch (Exception e) {
        logger.error("Failed to click Login button: " + e.getMessage());
        throw new RuntimeException("Failed to click Login button", e);
    }
}

public void clickMenuButton(){
        try{
            WebElement menu =wait.until(ExpectedConditions.elementToBeClickable(menuButton));
            menu.click();
            logger.info("Clicked Menu button");

        }catch (Exception e){
            logger.error("Failed to click the menu button: "+ e.getMessage());
            throw new RuntimeException("Failed to click Menu button", e);

        }
}
public void clickLogoutButton(){
        try{
            WebElement logOut=wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
            logOut.click();
            logger.info("clicked logout button");
        }catch (Exception e){
            logger.error("Failed to click the logout button"+e.getMessage());
            throw new RuntimeException("Failed to click the logout button",e);
        }
}
}
