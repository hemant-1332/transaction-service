package com.demo.transactionService.service;

import com.demo.transactionService.model.Product;
import com.demo.transactionService.model.TransSummaryByCity;
import com.demo.transactionService.model.TransSummaryByProducts;
import com.demo.transactionService.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
@ApplicationScope
public class SummaryService implements Serializable {

    List<Product> productList = new ArrayList<>();

    public void updateTransSinkWithProductInfo(String productName, String cityName, Double amount, Date date){
        Product p = new Product(productName, cityName, amount, date);
        productList.add(p);
    }

    public TransSummaryByProducts getSummaryByProducts(int days) {
        TransSummaryByProducts summary= new TransSummaryByProducts(StreamUtils.getGroupedSumByProducts(productList, days));
        return summary;
    }
    public TransSummaryByCity getSummaryByCity(int days) {
        TransSummaryByCity summary= new TransSummaryByCity(StreamUtils.getGroupedSumByCity(productList, days));
        return summary;
    }
}
