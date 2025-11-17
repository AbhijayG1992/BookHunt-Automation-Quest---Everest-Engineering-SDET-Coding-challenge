package com.sdet.assignment.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.sdet.assignment.model.Book;
import com.sdet.assignment.model.PriceInfo;
import com.sdet.assignment.pages.AmazonPage;
import com.sdet.assignment.pages.FlipkartPage;
import com.sdet.assignment.pages.BuyBooksIndiaPage;
import com.sdet.assignment.utils.CsvUtil;
import com.sdet.assignment.utils.ExtentReportUtil;
import com.sdet.assignment.utils.JsonUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRetailerTest {

    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        extent = ExtentReportUtil.getReporter();
    }

    @Test(enabled = false)
    public void findCheapestRetailer() {
        test = extent.createTest("Find Cheapest Retailer", "Find the cheapest retailer for a list of books");
        List<Book> books = CsvUtil.readBooks("resources/bookList.csv");
        List<PriceInfo> allPrices = new ArrayList<>();

        AmazonPage amazonPage = new AmazonPage(driver);
        FlipkartPage flipkartPage = new FlipkartPage(driver);
        BuyBooksIndiaPage buyBooksIndiaPage = new BuyBooksIndiaPage(driver);

        for (Book book : books) {
            ExtentTest bookTest = test.createNode("Book: " + book.getName());
            
            // Amazon
            amazonPage.navigateTo();
            amazonPage.searchForBook(book.getName());
            amazonPage.isBookDisplayed(book.getName());
            
            PriceInfo amazonPrice = amazonPage.checkPrice(book);
            allPrices.add(amazonPrice);
            logPrice(bookTest, amazonPrice);

//            // Flipkart
//            PriceInfo flipkartPrice = flipkartPage.getPrice(book);
//            allPrices.add(flipkartPrice);
//            logPrice(bookTest, flipkartPrice);
//
//            // BuyBooksIndia
//            PriceInfo buyBooksIndiaPrice = buyBooksIndiaPage.getPrice(book);
//            allPrices.add(buyBooksIndiaPrice);
//            logPrice(bookTest, buyBooksIndiaPrice);
        }

        Map<String, Double> retailerTotals = new HashMap<>();
        for (PriceInfo priceInfo : allPrices) {
            if (priceInfo.isAvailable()) {
                retailerTotals.merge(priceInfo.getRetailer().getName(), priceInfo.getPrice() * priceInfo.getBook().getQuantity(), Double::sum);
            }
        }

        String cheapestRetailer = "";
        double minTotal = Double.MAX_VALUE;
        for (Map.Entry<String, Double> entry : retailerTotals.entrySet()) {
            if (entry.getValue() < minTotal) {
                minTotal = entry.getValue();
                cheapestRetailer = entry.getKey();
            }
        }

        test.info("Cheapest Retailer: " + cheapestRetailer);
        test.info("Total Price: " + minTotal);

        Map<String, Object> result = new HashMap<>();
        result.put("cheapestRetailer", cheapestRetailer);
        result.put("totalPrice", minTotal);
        result.put("books", allPrices);

        JsonUtil.writeJson(result, "output.json");
    }

    @Test(priority = 2)
    public void addToCartAndVerifyPrice() {
        test = extent.createTest("Add to Cart and Verify Price", "Add books to the cart and verify the total price for each");
        List<Book> books = CsvUtil.readBooks("resources/bookList.csv");
        AmazonPage amazonPage = new AmazonPage(driver);

        for (Book book : books) {
            ExtentTest bookTest = test.createNode("Book: " + book.getName());
            System.out.println("--- Starting test for book: " + book.getName() + " ---");

            amazonPage.navigateTo();
            System.out.println("Navigated to Amazon homepage.");

            amazonPage.searchForBook(book.getName());
            System.out.println("Searched for book: " + book.getName());
            Assert.assertTrue(amazonPage.isBookDisplayed(book.getName()), "Book search results are not displayed for " + book.getName());

            PriceInfo priceInfo = amazonPage.checkPrice(book);
            System.out.println("Lowest price found: " + priceInfo.getPrice());
            if (!priceInfo.isAvailable()) {
                bookTest.skip("Book is not available, skipping add to cart test.");
                System.out.println("Book is not available. Skipping test.");
                continue;
            }

            amazonPage.clickLowestPriceSearchResult(book.getName());
            System.out.println("Clicked on the lowest priced search result.");

            amazonPage.switchToNewTab();
            System.out.println("Switched to the new tab.");

            amazonPage.selectQuantity(book.getQuantity());
            System.out.println("Selected quantity: " + book.getQuantity());

            amazonPage.clickAddToCart();
            System.out.println("Clicked 'Add to Cart'.");

            amazonPage.navigateToCart();
            System.out.println("Navigated to the cart page.");

            double actualSubtotal = amazonPage.getCartSubtotal();
            double expectedSubtotal = priceInfo.getPrice() * book.getQuantity();
            System.out.println("Actual subtotal: " + actualSubtotal);
            System.out.println("Expected subtotal: " + expectedSubtotal);

            Assert.assertEquals(actualSubtotal, expectedSubtotal, "Cart subtotal does not match the expected price for " + book.getName());
            bookTest.pass("Cart subtotal verified successfully for " + book.getName());
            System.out.println("--- Test for book: " + book.getName() + " passed. ---");

            amazonPage.clearCart();
            System.out.println("Cleared the cart.");
            
            amazonPage.switchToFirstTab();
            System.out.println("Moved to Tab 1");
        }
    }

    private void logPrice(ExtentTest test, PriceInfo priceInfo) {
        if (priceInfo.isAvailable()) {
            test.pass(priceInfo.getRetailer().getName() + ": " + priceInfo.getPrice());
        } else {
            test.fail(priceInfo.getRetailer().getName() + ": Not Available");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        if (extent != null) {
            extent.flush();
        }
    }
}
