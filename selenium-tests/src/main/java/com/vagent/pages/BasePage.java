package com.vagent.pages;

import com.vagent.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

/**
 * Base page class with common functionality for all page objects
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor jsExecutor;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getExplicitWait()));
        this.jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for element to be visible
     */
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable
     */
    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Click element using JavaScript
     */
    protected void clickElementJS(WebElement element) {
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    /**
     * Scroll to element
     */
    protected void scrollToElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Wait for page to load completely
     */
    protected void waitForPageToLoad() {
        // Extended wait for Render free tier
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(120));
        longWait.until(webDriver -> jsExecutor.executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Wait for page to load with custom timeout
     */
    protected void waitForPageToLoad(int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        customWait.until(webDriver -> jsExecutor.executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Send text to element with clear
     */
    protected void sendTextToElement(WebElement element, String text) {
        waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Click element with wait
     */
    protected void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Navigate to URL
     */
    public void navigateToUrl(String url) {
        driver.get(url);
        waitForPageToLoad();
    }

    /**
     * Navigate to URL with extended wait for Render cold start
     */
    public void navigateToUrlWithRetry(String url) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                driver.get(url);
                waitForPageToLoad(120); // 2 minutes for cold start
                return;
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    throw new RuntimeException("Failed to load page after " + maxRetries + " attempts", e);
                }
                // Wait before retry
                try {
                    Thread.sleep(10000); // 10 seconds
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Wait for element with extended timeout for Render delays
     */
    protected void waitForElementWithExtendedTimeout(WebElement element) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(90));
        longWait.until(ExpectedConditions.visibilityOf(element));
    }
}
