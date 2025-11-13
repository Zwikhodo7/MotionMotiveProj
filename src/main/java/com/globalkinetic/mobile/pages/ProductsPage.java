package com.globalkinetic.mobile.pages;

import com.globalkinetic.mobile.BaseMobilePage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

/**
 * Page Object for Mobile App Products Page
 */
public class ProductsPage extends BaseMobilePage {
    
    private static final By PRODUCTS_TITLE = By.xpath("//android.widget.TextView[@text='Products']");
    private static final By CATALOG_OPTION = By.xpath("//android.widget.TextView[@text='Catalog']");
    private static final By FIRST_PRODUCT = By.xpath("(//android.view.ViewGroup[@content-desc='store item'])[1]");
    private static final By ADD_TO_CART_BUTTON = By.xpath("//android.view.ViewGroup[@content-desc='Add To Cart button']");
    private static final By CART_ICON = By.xpath("//android.view.ViewGroup[@content-desc='cart badge']");
    private static final By MENU_BUTTON = By.xpath("//android.view.ViewGroup[@content-desc='open menu']");
    private static final By LOGOUT_BUTTON = By.xpath("//android.view.ViewGroup[@content-desc='menu item log out']");
    private static final By LOGOUT_CONFIRMATION_BUTTON = By.xpath("//android.widget.Button[@text='LOG OUT']");
    private static final By OK_BUTTON = By.xpath("//android.widget.Button[@text='OK']");

    public ProductsPage(AppiumDriver driver) {
        super(driver);
    }

    public void navigateToProductsPage() {
        click(MENU_BUTTON);
        click(CATALOG_OPTION);
    }

    public boolean isProductsPageDisplayed()
    {
        waitForElement(PRODUCTS_TITLE);
        return isDisplayed(PRODUCTS_TITLE);
    }

    public void selectFirstProduct() {
        click(FIRST_PRODUCT);
        //TODO: Add validation/assertions of single product page elements
    }

    public  boolean isAddToCartButtonDisplayed(){
        waitForElement(ADD_TO_CART_BUTTON);
        return isDisplayed(ADD_TO_CART_BUTTON);
    }

    public void addToCart() {
        click(ADD_TO_CART_BUTTON);
    }

    public void clickCartIcon() {
        click(CART_ICON);
    }


    public void logout() {
        click(MENU_BUTTON);
        waitForElement(LOGOUT_BUTTON);
        click(LOGOUT_BUTTON);
        click(LOGOUT_CONFIRMATION_BUTTON);
        click(OK_BUTTON);
    }

    public boolean isCartIconDisplayed() {
        return isDisplayed(CART_ICON);
    }
}

