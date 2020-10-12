package com.demo.transactionService.controller;

import com.demo.transactionService.model.*;
import com.demo.transactionService.service.SummaryService;
import com.demo.transactionService.service.TransactionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/assignment")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionInfoService transactionInfoService;
    @Autowired
    private SummaryService summaryService;

    @ResponseBody
    @RequestMapping(value = "/transaction/{transaction_id}", method = RequestMethod.GET)
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("transaction_id") int id) throws StreamingQueryException {
        return new ResponseEntity<>(transactionInfoService.getTransaction(id), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/transactionSummaryByProducts/{days}", method = RequestMethod.GET)
    public ResponseEntity<TransSummaryByProducts> getTransactionSummaryByProducts(@PathVariable("days") int days) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(summaryService.getSummaryByProducts(days), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/transactionSummaryByManufacturingCity/{days}", method = RequestMethod.GET)
    public ResponseEntity<TransSummaryByCity> getTransactionByCity(@PathVariable("days") int days) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(summaryService.getSummaryByCity(days), HttpStatus.OK);
    }
}
