package com.globalkinetic.mobile.pages;

import com.globalkinetic.mobile.BaseMobilePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Page Object for Mobile App Login Page
 */
public class LoginPage extends BaseMobilePage {
    
    private static final By USERNAME_FIELD = By.xpath("//android.widget.EditText[@content-desc='Username input field']");
    private static final By PASSWORD_FIELD = By.xpath("//android.widget.EditText[@content-desc='Password input field']");
    private static final By LOGIN_BUTTON = By.xpath("//android.view.ViewGroup[@content-desc='Login button']");
    private static final By ERROR_MESSAGE = By.xpath("//android.widget.TextView[@text='Provided credentials do not match any user in this service.']");

    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        sendKeys(USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        sendKeys(PASSWORD_FIELD, password);
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
        if (isDisplayed(ERROR_MESSAGE)) {
            return getText(ERROR_MESSAGE);
        }
        return null;
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(USERNAME_FIELD) && isDisplayed(PASSWORD_FIELD) && isDisplayed(LOGIN_BUTTON);
    }
}

