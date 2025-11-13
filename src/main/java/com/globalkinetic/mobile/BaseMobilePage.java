package com.globalkinetic.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base Page class for Mobile Automation using Appium
 */
public class BaseMobilePage {
    protected static final Logger logger = LoggerFactory.getLogger(BaseMobilePage.class);
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public BaseMobilePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    protected void click(By locator) {
        logger.debug("Clicking element: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void sendKeys(By locator, String text) {
        logger.debug("Sending keys to element: {} with text: {}", locator, text);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        logger.debug("Getting text from element: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getText();
    }

    public boolean isDisplayed(By locator) {
        logger.debug("Checking if element is displayed: {}", locator);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitForElement(By locator) {
        logger.debug("Waiting for element: {}", locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void scrollToElement(String text) {
        logger.debug("Scrolling to element with text: {}", text);
        driver.findElement(By.xpath("//*[@text='" + text + "']"));
    }
}

