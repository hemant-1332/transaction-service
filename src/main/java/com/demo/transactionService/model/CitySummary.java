package com.demo.transactionService.model;

public class CitySummary {
    private String cityName;
    private Double totalAmount;

    public CitySummary(String cityName, Double totalAmount) {
        this.cityName = cityName;
        this.totalAmount = totalAmount;
    }

    public String getCityName() {
        return cityName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}
