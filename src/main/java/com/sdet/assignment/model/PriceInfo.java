package com.sdet.assignment.model;

public class PriceInfo {
    private Book book;
    private Retailer retailer;
    private double price;
    private boolean isAvailable;
    private int quantity;

    public PriceInfo(Book book, Retailer retailer, double price, boolean isAvailable) {
        this.book = book;
        this.retailer = retailer;
        this.price = price;
        this.isAvailable = isAvailable;
        this.quantity = 1; // Default to 1
    }

    public PriceInfo(Book book, Retailer retailer, double price, boolean isAvailable, int quantity) {
        this.book = book;
        this.retailer = retailer;
        this.price = price;
        this.isAvailable = isAvailable;
        this.quantity = quantity;
    }

    // Getters and setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
