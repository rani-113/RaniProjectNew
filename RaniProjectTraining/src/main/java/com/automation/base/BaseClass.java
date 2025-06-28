package com.automation.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BaseClass {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseClass.class);
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    
    public static void initializeDriver() {
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            
            
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            logger.info("WebDriver initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }
    
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed successfully");
        }
    }
    
    public static WebDriver getDriver() {
        return driver;
    }
    
    public static WebDriverWait getWait() {
        return wait;
    }
    
    public static void navigateToUrl(String url) {
        try {
            driver.get(url);
            logger.info("Navigated to URL: " + url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: " + e.getMessage());
            throw new RuntimeException("Navigation failed", e);
        }
    }
} 