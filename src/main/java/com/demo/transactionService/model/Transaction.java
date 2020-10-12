package com.demo.transactionService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class Transaction implements Serializable {
    private int transactionId;
    private String productName;
    private Double transactionAmount;
    private String transactionDatetime;

    public Transaction(int transactionId, String productName, Double transactionAmount, String transactionDatetime) {
        this.transactionId = transactionId;
        this.productName = productName;
        this.transactionAmount = transactionAmount;
        this.transactionDatetime = transactionDatetime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", productId=" + productName +
                ", transactionAmount=" + transactionAmount +
                ", transactionDatetime='" + transactionDatetime + '\'' +
                '}';
    }
}
