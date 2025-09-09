package com.vagent.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object Model for Login page
 */
public class LoginPage extends BasePage {

    // Page elements
    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = "a[href='/register']")
    private WebElement registerLink;

    @FindBy(css = "h2")
    private WebElement pageTitle;

    @FindBy(css = ".ri-robot-2-line")
    private WebElement robotIcon;

    @FindBy(css = "p.text-gray-400")
    private WebElement welcomeMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter email address
     */
    public LoginPage enterEmail(String email) {
        sendTextToElement(emailField, email);
        return this;
    }

    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        sendTextToElement(passwordField, password);
        return this;
    }

    /**
     * Click login button
     */
    public HomePage clickLoginButton() {
        clickElement(loginButton);
        return new HomePage(driver);
    }

    /**
     * Click register link
     */
    public RegisterPage clickRegisterLink() {
        clickElement(registerLink);
        return new RegisterPage(driver);
    }

    /**
     * Perform complete login
     */
    public HomePage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickElement(loginButton);
        
        // Wait for navigation to complete
        waitForPageToLoad(60);
        
        return new HomePage(driver);
    }

    /**
     * Check if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        try {
            waitForElementToBeVisible(pageTitle);
            return pageTitle.getText().contains("VAgent Login");
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
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        waitForElementToBeVisible(loginButton);
        return loginButton.isEnabled();
    }

    /**
     * Get login button text
     */
    public String getLoginButtonText() {
        waitForElementToBeVisible(loginButton);
        return loginButton.getText();
    }
}
