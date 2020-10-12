package com.demo.transactionService.model;

import java.util.Date;

public class Product {
    private String productName;
    private String cityName;
    private Double totalAmount;
    private Date date;

    public Product(String productName, String cityName, Double totalAmount, Date date) {
        this.totalAmount = totalAmount;
        this.productName = productName;
        this.cityName = cityName;
        this.date = date;
    }

    public Date getDate() { return date; }

    public String getCityName() {
        return cityName;
    }

    public String getProductName() {
        return productName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

}
