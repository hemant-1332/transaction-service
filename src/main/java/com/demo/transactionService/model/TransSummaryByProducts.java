package com.demo.transactionService.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransSummaryByProducts {
    private List<ProductSummary> summary;

    public TransSummaryByProducts( List<ProductSummary> summary) {
        this.summary = summary;
    }

    public  List<ProductSummary> getSummary() {
        return summary;
    }
}
