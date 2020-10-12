package com.demo.transactionService.model;

public class ProductSummary {
    private String productName;
    private Double totalAmount;

    public ProductSummary(String productName, Double totalAmount) {
        this.productName = productName;
        this.totalAmount = totalAmount;
    }

    public String getProductName() {
        return productName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}
