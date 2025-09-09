package com.vagent.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for Register page
 */
public class RegisterPage extends BasePage {

    // Page elements
    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement registerButton;

    @FindBy(css = "a[href='/login']")
    private WebElement loginLink;

    @FindBy(css = "h2")
    private WebElement pageTitle;

    @FindBy(css = ".ri-robot-2-line")
    private WebElement robotIcon;

    @FindBy(css = "p.text-gray-400")
    private WebElement welcomeMessage;

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter email address
     */
    public RegisterPage enterEmail(String email) {
        sendTextToElement(emailField, email);
        return this;
    }

    /**
     * Enter password
     */
    public RegisterPage enterPassword(String password) {
        sendTextToElement(passwordField, password);
        return this;
    }

    /**
     * Click register button
     */
    public HomePage clickRegisterButton() {
        clickElement(registerButton);
        return new HomePage(driver);
    }

    /**
     * Click login link
     */
    public LoginPage clickLoginLink() {
        clickElement(loginLink);
        return new LoginPage(driver);
    }

    /**
     * Perform complete registration
     */
    public HomePage register(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickElement(registerButton);
        
        // Wait for navigation to complete - could go to home or login page
        waitForPageToLoad(60);
        
        // Check if we're redirected to login page (common behavior)
        String currentUrl = getCurrentUrl();
        if (currentUrl.contains("/login")) {
            // If redirected to login, need to login with the same credentials
            LoginPage loginPage = new LoginPage(driver);
            return loginPage.login(email, password);
        }
        
        // Otherwise, we should be on home page
        return new HomePage(driver);
    }

    /**
     * Check if register page is displayed
     */
    public boolean isRegisterPageDisplayed() {
        try {
            waitForElementToBeVisible(pageTitle);
            return pageTitle.getText().contains("VAgent Register");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get welcome message text
     */
    public String getWelcomeMessage() {
        waitForElementToBeVisible(welcomeMessage);
        return welcomeMessage.getText();
    }

    /**
     * Check if robot icon is displayed
     */
    public boolean isRobotIconDisplayed() {
        try {
            waitForElementToBeVisible(robotIcon);
            return robotIcon.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if email field is displayed
     */
    public boolean isEmailFieldDisplayed() {
        try {
            waitForElementToBeVisible(emailField);
            return emailField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        try {
            waitForElementToBeVisible(passwordField);
            return passwordField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if register button is enabled
     */
    public boolean isRegisterButtonEnabled() {
        waitForElementToBeVisible(registerButton);
        return registerButton.isEnabled();
    }

    /**
     * Get register button text
     */
    public String getRegisterButtonText() {
        waitForElementToBeVisible(registerButton);
        return registerButton.getText();
    }
}
