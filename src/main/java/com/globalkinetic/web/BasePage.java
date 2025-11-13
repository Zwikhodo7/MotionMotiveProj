package com.globalkinetic.web;

import com.globalkinetic.framework.config.ConfigManager;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Page class for Web Automation using Playwright
 */
public class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected Page page;
    protected BrowserContext context;
    protected Browser browser;

    public BasePage(Page page) {
        this.page = page;
        if (page != null) {
            try {
                this.context = page.context();
                if (this.context != null) {
                    this.browser = context.browser();
                }
            } catch (Exception e) {
                logger.warn("Error accessing page context/browser: {}", e.getMessage());
            }
        }
    }

    public void navigateTo(String url) {
        logger.info("Navigating to: {}", url);
        page.navigate(url);
        page.waitForLoadState();
    }

    public void click(String selector) {
        logger.debug("Clicking element: {}", selector);
        page.click(selector);
    }

    public void fill(String selector, String value) {
        logger.debug("Filling field: {} with value: {}", selector, value);
        page.fill(selector, value);
    }

    public String getText(String selector) {
        logger.debug("Getting text from: {}", selector);
        return page.textContent(selector);
    }

    public boolean isVisible(String selector) {
        logger.debug("Checking visibility of: {}", selector);
        return page.isVisible(selector);
    }

    public void waitForSelector(String selector) {
        logger.debug("Waiting for selector: {}", selector);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(30000));
    }
    
    public void waitForSelector(String selector, int timeoutMs) {
        logger.debug("Waiting for selector: {} with timeout: {}ms", selector, timeoutMs);
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeoutMs));
    }

    public void takeScreenshot(String fileName) {
        logger.info("Taking screenshot: {}", fileName);
        java.io.File screenshotDir = new java.io.File("test-results/screenshots/");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        page.screenshot(new Page.ScreenshotOptions().setPath(new java.io.File(screenshotDir, fileName).toPath()));
    }
}

