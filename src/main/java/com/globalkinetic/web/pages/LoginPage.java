package com.globalkinetic.web.pages;

import com.globalkinetic.web.BasePage;
import com.microsoft.playwright.Page;

/**
 * Page Object for Sauce Demo Login Page
 */
public class LoginPage extends BasePage {
    
    // Locators
    private static final String USERNAME_INPUT = "#user-name";
    private static final String PASSWORD_INPUT = "#password";
    private static final String LOGIN_BUTTON = "#login-button";
    private static final String ERROR_MESSAGE = "[data-test='error']";

    public LoginPage(Page page) {
        super(page);
    }

    public void navigateToLoginPage() {
        navigateTo("https://www.saucedemo.com/v1/");
    }

    public void enterUsername(String username) {
        fill(USERNAME_INPUT, username);
    }

    public void enterPassword(String password) {
        fill(PASSWORD_INPUT, password);
    }

    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getErrorMessage() {
        if (isVisible(ERROR_MESSAGE)) {
            return getText(ERROR_MESSAGE);
        }
        return null;
    }

    public boolean isLoginPageDisplayed() {
        return isVisible(USERNAME_INPUT) && isVisible(PASSWORD_INPUT) && isVisible(LOGIN_BUTTON);
    }
}

