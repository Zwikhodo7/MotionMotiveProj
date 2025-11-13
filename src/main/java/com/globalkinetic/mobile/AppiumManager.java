package com.globalkinetic.mobile;

import com.globalkinetic.framework.config.ConfigManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Appium Manager for mobile automation setup
 */
public class AppiumManager {
    private static final Logger logger = LoggerFactory.getLogger(AppiumManager.class);
    private static AppiumManager instance;
    private AppiumDriver driver;
    private ConfigManager configManager;

    private AppiumManager() {
        configManager = ConfigManager.getInstance();
    }

    public static AppiumManager getInstance() {
        if (instance == null) {
            synchronized (AppiumManager.class) {
                if (instance == null) {
                    instance = new AppiumManager();
                }
            }
        }
        return instance;
    }

    public AppiumDriver initializeDriver() {
        try {
            String appiumServerUrl = configManager.getProperty("appium.server.url", "http://127.0.0.1:4723");
            String appPath = configManager.getProperty("app.path", "");
            String deviceName = configManager.getProperty("device.name", "Android Emulator");
            String platformVersion = configManager.getProperty("platform.version", "11.0");
            String automationName = configManager.getProperty("automation.name", "UiAutomator2");

            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName("Android");
            options.setDeviceName(deviceName);
            options.setPlatformVersion(platformVersion);
            options.setAutomationName(automationName);
            
            if (!appPath.isEmpty()) {
                options.setApp(appPath);
            } else {
                String appPackage = configManager.getProperty("app.package", "com.saucelabs.mydemoapp.rn");
                String appActivity = configManager.getProperty("app.activity", ".MainActivity");
                options.setAppPackage(appPackage);
                options.setAppActivity(appActivity);
            }

            options.setNoReset(false);
            options.setFullReset(false);
            options.setNewCommandTimeout(Duration.ofSeconds(300));

            driver = new AndroidDriver(new URL(appiumServerUrl), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            logger.info("Appium driver initialized successfully");
            return driver;
        } catch (MalformedURLException e) {
            logger.error("Error initializing Appium driver", e);
            throw new RuntimeException("Failed to initialize Appium driver", e);
        }
    }

    //If ever I need to call drivr methods like navigating back to previus page, will call this function
    public AppiumDriver getDriver() {
        if (driver == null) {
            driver = initializeDriver();
        }
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            logger.info("Appium driver closed");
        }
    }
}

