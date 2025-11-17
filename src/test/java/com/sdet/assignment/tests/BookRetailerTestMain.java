//package com.sdet.assignment.tests;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.sdet.assignment.model.Book;
//import com.sdet.assignment.model.PriceInfo;
//import com.sdet.assignment.pages.AmazonPage;
//import com.sdet.assignment.pages.BuyBooksIndiaPage;
//import com.sdet.assignment.pages.FlipkartPage;
//import com.sdet.assignment.utils.CsvUtil;
//import com.sdet.assignment.utils.ExtentReportUtil;
//import com.sdet.assignment.utils.JsonUtil;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class BookRetailerTestMain {
//
//    private WebDriver driver;
//    private ExtentReports extent;
//    private ExtentTest test;
//
//    @BeforeClass
//    public void setUp() {
//        // Assuming chromedriver is in the system path
//        driver = new ChromeDriver();
//        extent = ExtentReportUtil.getReporter();
//        test = extent.createTest("Find Cheapest Retailer", "Find the cheapest retailer for a list of books");
//    }
//
//    @Test
//    public void findCheapestRetailer() {
//        List<Book> books = CsvUtil.readBooks("resources/bookList.csv");
//        List<PriceInfo> allPrices = new ArrayList<>();
//
//        AmazonPage amazonPage = new AmazonPage(driver);
//        FlipkartPage flipkartPage = new FlipkartPage(driver);
//        BuyBooksIndiaPage buyBooksIndiaPage = new BuyBooksIndiaPage(driver);
//
//        for (Book book : books) {
//            ExtentTest bookTest = test.createNode("Book: " + book.getName());
//            PriceInfo amazonPrice = amazonPage.getPrice(book);
//            allPrices.add(amazonPrice);
//            logPrice(bookTest, amazonPrice);
//
//            PriceInfo flipkartPrice = flipkartPage.getPrice(book);
//            allPrices.add(flipkartPrice);
//            logPrice(bookTest, flipkartPrice);
//
//            PriceInfo buyBooksIndiaPrice = buyBooksIndiaPage.getPrice(book);
//            allPrices.add(buyBooksIndiaPrice);
//            logPrice(bookTest, buyBooksIndiaPrice);
//        }
//
//        Map<String, Double> retailerTotals = new HashMap<>();
//        for (PriceInfo priceInfo : allPrices) {
//            if (priceInfo.isAvailable()) {
//                retailerTotals.merge(priceInfo.getRetailer().getName(), priceInfo.getPrice() * priceInfo.getBook().getQuantity(), Double::sum);
//            }
//        }
//
//        String cheapestRetailer = "";
//        double minTotal = Double.MAX_VALUE;
//        for (Map.Entry<String, Double> entry : retailerTotals.entrySet()) {
//            if (entry.getValue() < minTotal) {
//                minTotal = entry.getValue();
//                cheapestRetailer = entry.getKey();
//            }
//        }
//
//        test.info("Cheapest Retailer: " + cheapestRetailer);
//        test.info("Total Price: " + minTotal);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("cheapestRetailer", cheapestRetailer);
//        result.put("totalPrice", minTotal);
//        result.put("books", allPrices);
//
//        JsonUtil.writeJson(result, "output.json");
//    }
//
//    private void logPrice(ExtentTest test, PriceInfo priceInfo) {
//        if (priceInfo.isAvailable()) {
//            test.pass(priceInfo.getRetailer().getName() + ": " + priceInfo.getPrice());
//        } else {
//            test.fail(priceInfo.getRetailer().getName() + ": Not Available");
//        }
//    }
//
//    @AfterClass
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//        if (extent != null) {
//            extent.flush();
//        }
//    }
//
//
//}
