package com.globalkinetic.web.pages;

import com.globalkinetic.web.BasePage;
import com.microsoft.playwright.Page;

/**
 * Page Object for Sauce Demo Products Page
 */
public class ProductsPage extends BasePage {
    
    // Locators
    private static final String PRODUCTS_TITLE = ".product_label";
    private static final String PRODUCTS_CONTAINER = ".inventory_list";
    private static final String ADD_TO_CART_BUTTON = "button[class*='btn_inventory']";
    private static final String SHOPPING_CART_ICON = ".shopping_cart_link";
    private static final String MENU_BUTTON = "#react-burger-menu-btn";
    private static final String LOGOUT_LINK = "#logout_sidebar_link";

    public ProductsPage(Page page) {
        super(page);
    }

    public boolean isProductsPageDisplayed() {
        try {
            // Wait for page to load
            page.waitForLoadState();
            // Try multiple selectors that indicate products page
            try {
                waitForSelector(PRODUCTS_TITLE, 10000);
                return isVisible(PRODUCTS_TITLE);
            } catch (Exception e) {
                // Try alternative selectors
                try {
                    waitForSelector(PRODUCTS_CONTAINER, 10000);
                    return isVisible(PRODUCTS_CONTAINER);
                } catch (Exception e2) {
                    // Try any product item
                    waitForSelector(".inventory_item", 10000);
                    return isVisible(".inventory_item");
                }
            }
        } catch (Exception e) {
            logger.warn("Products page not found: {}", e.getMessage());
            return false;
        }
    }

    public String getProductsPageTitle() {
        return getText(PRODUCTS_TITLE);
    }

    public void addFirstProductToCart() {
        click(ADD_TO_CART_BUTTON + ":first-of-type");
    }

    public void clickShoppingCart() {
        click(SHOPPING_CART_ICON);
    }

    public void logout() {
        click(MENU_BUTTON);
        waitForSelector(LOGOUT_LINK);
        click(LOGOUT_LINK);
    }
}

