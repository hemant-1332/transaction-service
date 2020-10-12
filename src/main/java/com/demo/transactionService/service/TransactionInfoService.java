package com.demo.transactionService.service;

import com.demo.transactionService.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import scala.Serializable;
import java.util.HashMap;

@Slf4j
@Service
@ApplicationScope
public class TransactionInfoService implements Serializable {
    HashMap<Integer, Transaction> transactionHashMap = new HashMap<>();
    HashMap<Integer, String> productNameMap = new HashMap<>();
    HashMap<Integer, String> productCityMap = new HashMap<>();

    public void updateStaticSink(int productId, String productName, String mCity){
        productNameMap.put(productId, productName);
        productCityMap.put(productId, mCity);
    }


    public void updateSink(int id, int productId, Double tAmount, String tDatetime){
        String pName = productNameMap.get(productId);
        transactionHashMap.put(id, new Transaction(id, pName, tAmount, tDatetime));
    }

    public Transaction getTransaction(int id){
        if(transactionHashMap.containsKey(id))
            return transactionHashMap.get(id);
        return null;
    }
}
