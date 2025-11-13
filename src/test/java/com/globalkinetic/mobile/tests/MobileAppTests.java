package com.globalkinetic.mobile.tests;

import com.globalkinetic.framework.utils.ReportUtils;
import com.globalkinetic.framework.utils.TestDataManager;
import com.globalkinetic.mobile.AppiumManager;
import com.globalkinetic.mobile.pages.LoginPage;
import com.globalkinetic.mobile.pages.ProductsPage;
import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Mobile Automation Tests for My Demo App using Appium
 */
public class MobileAppTests {
    private AppiumDriver driver;
    private AppiumManager appiumManager;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeMethod
    public void setUp() {
        appiumManager = AppiumManager.getInstance();
        driver = appiumManager.initializeDriver();
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            appiumManager.quitDriver();
        }
    }

    @Test(priority = 1, description = "Verify login page loads successfully")
    public void testLoginPageLoads() {
        try {
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
            JsonObject testData = TestDataManager.loadTestData("mobile-test-data.json");
            String username = testData.get("validUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("validUser").getAsJsonObject().get("password").getAsString();

            testLogout();
            loginPage.login(username, password);
            productsPage.navigateToProductsPage();
            boolean isProductsPageDisplayed = productsPage.isProductsPageDisplayed();
            Assert.assertTrue(isProductsPageDisplayed, "Products page should be displayed after login");
            ReportUtils.logTestResult("testSuccessfulLogin", "PASS", "Login successful");

        } catch (Exception e) {
            ReportUtils.logTestResult("testSuccessfulLogin", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3, description = "Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        try {
            JsonObject testData = TestDataManager.loadTestData("mobile-test-data.json");
            String username = testData.get("invalidUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("invalidUser").getAsJsonObject().get("password").getAsString();

            testLogout();
            loginPage.login(username, password);
            
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertNotNull(errorMessage, "Error message should be displayed");
            
            ReportUtils.logTestResult("testInvalidLogin", "PASS", "Invalid login handled correctly");
        } catch (Exception e) {
            ReportUtils.logTestResult("testInvalidLogin", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4, description = "Verify single product page details")
    public void testNavigationToProductDetails() {
        try {
            JsonObject testData = TestDataManager.loadTestData("mobile-test-data.json");
            String username = testData.get("validUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("validUser").getAsJsonObject().get("password").getAsString();

            testLogout();
            loginPage.login(username, password);
            productsPage.navigateToProductsPage();
            productsPage.selectFirstProduct();
            
            // Verify product details page is displayed by checking for add to cart button
            boolean cartButtonDisplayed = productsPage.isAddToCartButtonDisplayed();
            Assert.assertTrue(cartButtonDisplayed, "Product details should be displayed");
            
            ReportUtils.logTestResult("testNavigationToProductDetails", "PASS", "Navigation to product details successful");
        } catch (Exception e) {
            ReportUtils.logTestResult("testNavigationToProductDetails", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 5, description = "E2E - Login and purchase a product")
    public void testAddProductToCart() {
        try {
            JsonObject testData = TestDataManager.loadTestData("mobile-test-data.json");
            String username = testData.get("validUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("validUser").getAsJsonObject().get("password").getAsString();

            testLogout();
            loginPage.login(username, password);
            productsPage.navigateToProductsPage();
            productsPage.selectFirstProduct();
            productsPage.addToCart();
            
            // Navigate back and check cart icon
            driver.navigate().back();
            boolean cartIconDisplayed = productsPage.isCartIconDisplayed();
            Assert.assertTrue(cartIconDisplayed, "Cart icon should be displayed after adding product");
            
            ReportUtils.logTestResult("testAddProductToCart", "PASS", "Product added to cart successfully");
        } catch (Exception e) {
            ReportUtils.logTestResult("testAddProductToCart", "FAIL", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 6, description = "Verify logout functionality")
    public void testLogout() {
        try {
            JsonObject testData = TestDataManager.loadTestData("mobile-test-data.json");
            String username = testData.get("validUser").getAsJsonObject().get("username").getAsString();
            String password = testData.get("validUser").getAsJsonObject().get("password").getAsString();

          //  loginPage.login(username, password);
            productsPage.logout();
            
            boolean isLoginPageDisplayed = loginPage.isLoginPageDisplayed();
            Assert.assertTrue(isLoginPageDisplayed, "You are successfully logged out.");
            
            ReportUtils.logTestResult("testLogout", "PASS", "Logout successful");
        } catch (Exception e) {
            ReportUtils.logTestResult("testLogout", "FAIL", e.getMessage());
            throw e;
        }
    }
}

