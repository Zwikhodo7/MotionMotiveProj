package com.globalkinetic.web;

import com.globalkinetic.framework.config.ConfigManager;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Playwright Manager for browser initialization and management
 */
public class PlaywrightManager {
    private static final Logger logger = LoggerFactory.getLogger(PlaywrightManager.class);
    private static PlaywrightManager instance;
    private Playwright playwright;
    private ConfigManager configManager;
    private final Object playwrightLock = new Object();

    private PlaywrightManager() {
        configManager = ConfigManager.getInstance();
    }

    public static PlaywrightManager getInstance() {
        if (instance == null) {
            synchronized (PlaywrightManager.class) {
                if (instance == null) {
                    instance = new PlaywrightManager();
                }
            }
        }
        return instance;
    }

    private void initializePlaywright() {
        synchronized (playwrightLock) {
            if (playwright == null) {
                try {
                    playwright = Playwright.create();
                    logger.info("Playwright instance created");
                } catch (Exception e) {
                    logger.error("Error creating Playwright instace", e);
                    throw new RuntimeException("Failed to create Playright instance", e);
                }
            }
        }
    }

    public Page createNewPage() {
        // Ensure Playright is initialized
        if (playwright == null) {
            initializePlaywright();
        }

        // Create a new broser instace for each test to avoid conflicts
        String browserType = configManager.getProperty("browser.type", "chromium");
        boolean headless = configManager.getBooleanProperty("browser.headless", false);

        BrowserType browserTypeInstance;
        String browserTypeLower = browserType.toLowerCase();
        if ("firefox".equals(browserTypeLower)) {
            browserTypeInstance = playwright.firefox();
        } else if ("webkit".equals(browserTypeLower)) {
            browserTypeInstance = playwright.webkit();
        } else {
            browserTypeInstance = playwright.chromium();
        }

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(headless);

        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            launchOptions.setArgs(java.util.Arrays.asList(
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--no-sandbox"
            ));
        }
        
        Browser newBrowser = browserTypeInstance.launch(launchOptions);
        logger.info("New browser instance created: {}", browserType);
        
        // Create a new context for the test
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1920, 1080);
        
        BrowserContext context = newBrowser.newContext(contextOptions);
        logger.info("New context created");
        
        Page page = context.newPage();
        logger.info("New page created with new browser and context");
        return page;
    }

    public void closePage(Page page) {
        if (page != null) {
            try {
                BrowserContext context = page.context();
                Browser browserInstance = null;
                if (context != null) {
                    browserInstance = context.browser();
                }
                page.close();
                // Close the context after closing the page
                if (context != null) {
                    context.close();
                }
                // Close the broser instance
                if (browserInstance != null) {
                    browserInstance.close();
                }
                logger.info("Page, context, and browser closed");
            } catch (Exception e) {
                logger.warn("Error closing page: {}", e.getMessage());
            }
        }
    }
}

