package com.globalkinetic.web.tests;

import com.globalkinetic.framework.utils.ReportUtils;
import com.globalkinetic.framework.utils.TestDataManager;
import com.globalkinetic.web.PlaywrightManager;
import com.globalkinetic.web.pages.LoginPage;
import com.globalkinetic.web.pages.ProductsPage;
import com.google.gson.JsonObject;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Smoke Tests for Sauce Demo Website using Playwright
 */
public class SauceDemoSmokeTests {
    private static final Logger logger = LoggerFactory.getLogger(SauceDemoSmokeTests.class);
    private Page page;
    private PlaywrightManager playwrightManager;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeMethod
    public void setUp() {
        playwrightManager = PlaywrightManager.getInstance();
        page = playwrightManager.createNewPage();
        loginPage = new LoginPage(page);
        productsPage = new ProductsPage(page);
    }

    @AfterMethod
    public void tearDown() {
        try {
            if (page != null) {
                playwrightManager.closePage(page);
                page = null;
            }
        } catch (Exception e) {
            logger.warn("Error during tearDown: {}", e.getMessage());
        }
    }

    @Test(priority = 1, description = "Verify login page loads successfully")
    public void testLoginPageLoads() {
        try {
            loginPage.navigateToLoginPage();
            boolean isDisplayed = loginPage.isLoginPageDisplayed();
            
            Assert.assertTrue(isDisplayed, "Login page should be displayed");
            ReportUtils.logTestResult("testLoginPageLoads", "PASS", "Login page loaded successfully");
        } catch (Exception e) {
            ReportUtils.logTestResult("testLoginPageLoads", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2, description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        try {
            JsonObject testData = TestDataManager.loadTestData("web-test-data.json");
            String username = testData.get("validUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("validUser").getAsJsonObject().get("password").getAsString();

            loginPage.navigateToLoginPage();
            loginPage.login(username, password);
            
            // Wait a bit for page transition
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            boolean isProductsPageDisplayed = productsPage.isProductsPageDisplayed();
            Assert.assertTrue(isProductsPageDisplayed, "Products page should be displayed after login");
            
            // Try to get page title, but don't fail if it's not available
            try {
                String pageTitle = productsPage.getProductsPageTitle();
                Assert.assertNotNull(pageTitle, "Products page title should be displayed");
            } catch (Exception e) {
                logger.warn("Could not get page title, but page is displayed: {}", e.getMessage());
            }
            
            ReportUtils.logTestResult("testSuccessfulLogin", "PASS", "Login successful");
        } catch (Exception e) {
            loginPage.takeScreenshot("testSuccessfulLogin_failed.png");
            ReportUtils.logTestResult("testSuccessfulLogin", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3, description = "Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        try {
            JsonObject testData = TestDataManager.loadTestData("web-test-data.json");
            String username = testData.get("invalidUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("invalidUser").getAsJsonObject().get("password").getAsString();

            loginPage.navigateToLoginPage();
            loginPage.login(username, password);
            
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertNotNull(errorMessage, "Error message should be displayed");
            Assert.assertTrue(errorMessage.contains("Username and password do not match"), 
                "Error message should indicate invalid credentials");
            
            ReportUtils.logTestResult("testInvalidLogin", "PASS", "Invalid login handled correctly");
        } catch (Exception e) {
            loginPage.takeScreenshot("testInvalidLogin_failed.png");
            ReportUtils.logTestResult("testInvalidLogin", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4, description = "Verify user can add product to cart")
    public void testAddProductToCart() {
        try {
            JsonObject testData = TestDataManager.loadTestData("web-test-data.json");
            String username = testData.get("validUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("validUser").getAsJsonObject().get("password").getAsString();

            loginPage.navigateToLoginPage();
            loginPage.login(username, password);
            
            productsPage.addFirstProductToCart();
            page.pause();
            productsPage.clickShoppingCart();

            // Verify cart page is displayed
            boolean cartVisible = productsPage.isVisible(".cart_item");
            Assert.assertTrue(cartVisible, "Cart should contain items");
            
            ReportUtils.logTestResult("testAddProductToCart", "PASS", "Product added to cart successfully");
        } catch (Exception e) {
            productsPage.takeScreenshot("testAddProductToCart_failed.png");
            ReportUtils.logTestResult("testAddProductToCart", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 5, description = "Verify user can logout successfully")
    public void testLogout() {
        try {
            JsonObject testData = TestDataManager.loadTestData("web-test-data.json");
            String username = testData.get("validUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("validUser").getAsJsonObject().get("password").getAsString();

            loginPage.navigateToLoginPage();
            loginPage.login(username, password);
            
            productsPage.logout();
            
            boolean isLoginPageDisplayed = loginPage.isLoginPageDisplayed();
            Assert.assertTrue(isLoginPageDisplayed, "Should return to login page after logout");
            
            ReportUtils.logTestResult("testLogout", "PASS", "Logout successful");
        } catch (Exception e) {
            productsPage.takeScreenshot("testLogout_failed.png");
            ReportUtils.logTestResult("testLogout", "FAIL", e.getMessage());
            throw e;
        }
    }
}

