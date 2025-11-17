package com.sdet.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetailerPriceInfo {
    @JsonProperty("Price")
    private String price;
    @JsonProperty("Is available")
    private boolean isAvailable;

    public RetailerPriceInfo(double price, boolean isAvailable) {
        this.price = String.format("₹%.2f", price);
        this.isAvailable = isAvailable;
    }

    // Getters and setters
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
