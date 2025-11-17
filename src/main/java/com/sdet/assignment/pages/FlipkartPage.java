package com.sdet.assignment.pages;

import com.sdet.assignment.model.Book;
import com.sdet.assignment.model.PriceInfo;
import com.sdet.assignment.model.Retailer;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FlipkartPage extends BasePage {

    @FindBy(name = "q")
    private WebElement searchBox;

    @FindBy(className = "_34RNph")
    private WebElement searchButton;

    public FlipkartPage(WebDriver driver) {
        super(driver);
    }

    public PriceInfo getPrice(Book book) {
        driver.get("https://www.flipkart.com");
        searchBox.sendKeys(book.getName());
        searchBox.sendKeys(Keys.ENTER);

        // Placeholder for price retrieval
        double price = 0;
        boolean isAvailable = false;
        try {
            WebElement priceElement = driver.findElement(By.cssSelector("._30jeq3"));
            price = Double.parseDouble(priceElement.getText().replaceAll("[^\\d.]", ""));
            isAvailable = true;
        } catch (Exception e) {
            System.out.println("Book not found on Flipkart: " + book.getName());
        }

        return new PriceInfo(book, new Retailer("Flipkart", "https://www.flipkart.com"), price, isAvailable);
    }
}
