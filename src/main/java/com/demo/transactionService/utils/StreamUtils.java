package com.demo.transactionService.utils;

import com.demo.transactionService.model.CitySummary;
import com.demo.transactionService.model.Product;
import com.demo.transactionService.model.ProductSummary;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class StreamUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * Filter List by date
     * Group based on productName
     * Aggregate transactionAmount for each unique productName
     *
     * @param list
     * @param days
     * @return Summary
     */

    public static List<ProductSummary> getGroupedSumByProducts(List<Product> list, int days){
        log.info("Fetching summary by productName for last {} days", days);
        if(list != null){
            log.debug("List length {}", list.size());
            List<ProductSummary> products = new ArrayList<>();
            Map<String, Double> result = list.stream()
                    .filter(user -> datetoLocalDate(user.getDate()).isAfter(LocalDate.now().minusDays(days)))
                    .collect(Collectors.groupingBy(Product::getProductName, Collectors.summingDouble(Product::getTotalAmount)));
            log.info("Calculated grouped result: {}", result.toString());
            for(Map.Entry<String, Double> entry : result.entrySet()){
                ProductSummary p = new ProductSummary(entry.getKey(), entry.getValue());
                products.add(p);
            }
            return products;
        }
        return null;
    }

    /**
     * Filter List by date
     * Group based on Manufacturing CityName
     * Aggregate transactionAmount for each unique CityName
     *
     * @param list
     * @param days
     * @return Summary
     */

    public static List<CitySummary> getGroupedSumByCity(List<Product> list, int days){
        log.info("Fetching summary by city name for last {} days", days);
        if(list != null){
            log.debug("List length {}", list.size());
            List<CitySummary> products = new ArrayList<>();
            Map<String, Double> result = list.stream()
                    .filter(user -> datetoLocalDate(user.getDate()).isAfter(LocalDate.now().minusDays(days)))
                    .collect(Collectors.groupingBy(Product::getCityName, Collectors.summingDouble(Product::getTotalAmount)));
            log.info("Calculated grouped result: {}", result.toString());
            for(Map.Entry<String, Double> entry : result.entrySet()){
                CitySummary p = new CitySummary(entry.getKey(), entry.getValue());
                products.add(p);
            }
            return products;
        }
        return null;
    }

    public static LocalDate datetoLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date stringToDate(String dateStr) throws ParseException {
        return sdf.parse(dateStr);
    }
}
