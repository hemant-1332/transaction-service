package com.demo.transactionService.model;

import lombok.Data;
import lombok.Setter;

import java.util.List;

public class TransSummaryByCity {
    private List<CitySummary> summary;

    public TransSummaryByCity(List<CitySummary> summary) {
        this.summary = summary;
    }

    public List<CitySummary> getSummary() {
        return summary;
    }
}
