package com.sdet.assignment.pages;

import com.sdet.assignment.model.Book;
import com.sdet.assignment.model.PriceInfo;
import com.sdet.assignment.model.Retailer;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BuyBooksIndiaPage extends BasePage {

    @FindBy(id = "search-input")
    private WebElement searchBox;

    @FindBy(className = "search-button")
    private WebElement searchButton;

    public BuyBooksIndiaPage(WebDriver driver) {
        super(driver);
    }

    public PriceInfo getPrice(Book book) {
        driver.get("https://www.buybooksindia.com");
        searchBox.sendKeys(book.getName());
        searchBox.sendKeys(Keys.ENTER);

        // Placeholder for price retrieval
        double price = 0;
        boolean isAvailable = false;
        try {
            WebElement priceElement = driver.findElement(By.cssSelector(".price"));
            price = Double.parseDouble(priceElement.getText().replaceAll("[^\\d.]", ""));
            isAvailable = true;
        } catch (Exception e) {
            System.out.println("Book not found on BuyBooksIndia: " + book.getName());
        }

        return new PriceInfo(book, new Retailer("BuyBooksIndia", "https://www.buybooksindia.com"), price, isAvailable);
    }
}
