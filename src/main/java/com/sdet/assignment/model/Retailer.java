package com.sdet.assignment.model;

import java.util.List;

public class Retailer {
    private String name;
    private List<PriceInfo> prices;
    private String url;

    public Retailer(String name, String url) {
        this.name = name;
        this.url = url;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PriceInfo> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceInfo> prices) {
        this.prices = prices;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
