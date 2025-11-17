package com.sdet.assignment.pages;

import com.sdet.assignment.model.Book;
import com.sdet.assignment.model.PriceInfo;
import com.sdet.assignment.model.Retailer;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AmazonPage extends BasePage {

    private final String URL = "https://www.amazon.in";
    private final Retailer RETAILER = new Retailer("Amazon", URL);

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(xpath = "//div[@data-component-type='s-search-result']//span[@class='a-price-whole']")
    private List<WebElement> resultPrices;

    @FindBy(xpath="//div[@data-component-type='s-search-result']//h2" )
    private List<WebElement> SearchResultsLinks;

    @FindBy(xpath = "//input[@id='add-to-cart-button']")
    private WebElement addToCartButton;

    @FindBy(xpath = "//select[@id='quantity']")
    private WebElement quantityDropdown;

    @FindBy(xpath = "//div[@id='nav-cart-count-container']")
    private WebElement cartButton;

    @FindBy(xpath = "//span[@id='sc-subtotal-amount-activecart']//span[contains(@class, 'sc-price')]")
    private WebElement cartSubtotal;

    private final By SearchResultsLinksContainer = By.xpath("//div[@data-component-type='s-search-result']");
    private final By deleteItemFromCartButton = By.xpath("//input[@value='Delete']");

    public AmazonPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        driver.get(URL);
        driver.manage().window().maximize();

    }

    public void searchForBook(String bookTitle) {
        this.searchBox.clear();
        this.searchBox.sendKeys("book "+bookTitle);
        this.searchBox.sendKeys(Keys.ENTER);
    }

    public boolean isBookDisplayed(String bookTitle) {
        try {
        	
        	String SearchResultsLinksContainer = "//div[@data-component-type='s-search-result']";
            String BookSearchResultxpath = SearchResultsLinksContainer + "//span[(text() = '" + bookTitle + "')]";
        	
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(BookSearchResultxpath)));
            return !SearchResultsLinks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLowestPriceSearchResult(String bookTitle) {
    	
    	String SearchResultsLinksContainer = "//div[@data-component-type='s-search-result']";
        String BookSearchResultxpath = SearchResultsLinksContainer + "//span[(text() = '" + bookTitle + "')]";
        
        System.out.println(BookSearchResultxpath);
    	
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(BookSearchResultxpath)));

        WebElement elementToClick = null;
        double lowestPrice = Double.MAX_VALUE;
        
        String BookPriceXpath = "(//div[@data-component-type='s-search-result'][.//span[text()='" + bookTitle + "'] and .//a[text()='Paperback']]//span[@class='a-price-whole'])[1]";

        for (WebElement result : SearchResultsLinks) {
            try {
                WebElement priceElement = result.findElement(By.xpath(BookPriceXpath));
                String priceText = priceElement.getText().replaceAll(",", "");
                if (priceText.isEmpty()) {
                    continue;
                }
                double currentPrice = Double.parseDouble(priceText);

                if (currentPrice < lowestPrice) {
                    lowestPrice = currentPrice;
                    elementToClick = result.findElement(By.xpath("//div[@data-component-type='s-search-result']//h2"));
                }
            } catch (NoSuchElementException e) {
                // This result doesn't have a price, so we skip it.
            } catch (NumberFormatException e) {
                System.out.println("Could not parse price for a search result: " + e.getMessage());
            }
        }

        if (elementToClick != null) {
            wait.until(ExpectedConditions.elementToBeClickable(elementToClick)).click();
        } else if (!SearchResultsLinks.isEmpty()) {
            // Fallback: if no prices were found, click the first result.
            try {
                WebElement firstLink = SearchResultsLinks.get(0).findElement(By.xpath("//div[@data-component-type='s-search-result']//h2"));
                wait.until(ExpectedConditions.elementToBeClickable(firstLink)).click();
            } catch (NoSuchElementException e) {
                System.out.println("Could not find any search result link to click as a fallback.");
            }
        } else {
            System.out.println("No search results found to click.");
        }
    }
    
    public void switchToNewTab() {
        String originalHandle = driver.getWindowHandle();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        Set<String> allHandles = driver.getWindowHandles();
        for (String handle : allHandles) {
            if (!originalHandle.equals(handle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }   
    
    public void switchToFirstTab() {
    	ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    	driver.switchTo().window(tabs.get(1));
    	driver.close();
    	driver.switchTo().window(tabs.get(0));
    }

    public void selectQuantity(int quantity) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(quantityDropdown));
            Select quantitySelect = new Select(quantityDropdown);
            quantitySelect.selectByValue(String.valueOf(quantity));
            
            driver.findElement(By.xpath("//a[text()='" + quantity + " ']")).click();
  
        } catch (Exception e) {
            System.out.println("Could not select quantity. Defaulting to 1. " + e.getMessage());
        }
    }

    public void clickAddToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
    }

    public void navigateToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(cartButton)).click();
    }

    public double getCartSubtotal() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(cartSubtotal));
            String subtotalText = cartSubtotal.getText().replaceAll("[₹,]", "").trim();
            return Double.parseDouble(subtotalText);
        } catch (Exception e) {
            System.out.println("Could not retrieve cart subtotal. " + e.getMessage());
            return 0.0;
        }
    }

    public void clearCart() {
        navigateToCart();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            List<WebElement> deleteButtons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(deleteItemFromCartButton));
            for (WebElement deleteButton : deleteButtons) {
                wait.until(ExpectedConditions.elementToBeClickable(deleteItemFromCartButton)).click();
                wait.until(ExpectedConditions.stalenessOf(deleteButton));
            }
        } catch (Exception e) {
            System.out.println("Cart is already empty or could not find delete buttons.");
        }
    }

    public PriceInfo checkPrice(Book book) {
        double lowestPrice = Double.MAX_VALUE;
        boolean isAvailable = false;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(SearchResultsLinksContainer));

            for (WebElement result : SearchResultsLinks) {
                try {
                    WebElement priceElement = result.findElement(By.xpath("//div[@data-component-type='s-search-result']//span[@class='a-price-whole']"));
                    String priceText = priceElement.getText().replaceAll(",", "");
                    if (priceText.isEmpty()) {
                        continue;
                    }
                    double currentPrice = Double.parseDouble(priceText);
                    if (currentPrice < lowestPrice) {
                        lowestPrice = currentPrice;
                    }
                    isAvailable = true;
                } catch (NoSuchElementException e) {
                    // This result doesn't have a price, so we skip it.
                } catch (NumberFormatException e) {
                    System.out.println("Could not parse price for a search result: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Could not find price for '" + book.getName() + "' on Amazon. " + e.getMessage());
        }
        
        if (lowestPrice == Double.MAX_VALUE) {
            lowestPrice = 0; // No price found
        }

        return new PriceInfo(book, RETAILER, lowestPrice, isAvailable);
    }
}
