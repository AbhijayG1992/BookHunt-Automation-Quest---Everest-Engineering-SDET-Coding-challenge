package com.sdet.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class BookInfo {
    @JsonProperty("Book Name")
    private String bookName;
    @JsonProperty("Publisher")
    private String publisher;
    @JsonProperty("Author")
    private String author;
    @JsonProperty("Qty")
    private int qty;
    private Map<String, RetailerPriceInfo> retailers = new HashMap<>();

    public BookInfo(Book book) {
        this.bookName = book.getName();
        this.publisher = book.getPublisher();
        this.author = book.getAuthor();
        this.qty = book.getQuantity();
    }

    public void addRetailerPriceInfo(String retailerName, RetailerPriceInfo priceInfo) {
        this.retailers.put(retailerName, priceInfo);
    }

    // Getters and setters
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Map<String, RetailerPriceInfo> getRetailers() {
        return retailers;
    }

    public void setRetailers(Map<String, RetailerPriceInfo> retailers) {
        this.retailers = retailers;
    }
}
