package com.demo.transactionService.component;

import com.demo.transactionService.service.SummaryService;
import com.demo.transactionService.service.TransactionInfoService;
import com.demo.transactionService.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.ParseException;

@Component
@Slf4j
public class FileStream implements Serializable {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    TransactionInfoService transactionInfoService;
    @Autowired
    SummaryService summaryService;

    @Value("${app.config.input.static.file}")
    private String PRODUCT_REF_FILE ;
    @Value("${app.config.input.streaming.file}")
    private String STREAMING_FILE_PATH ;

    private static StructType productRefSchema = new StructType()
            .add("productId", DataTypes.IntegerType)
            .add("productName", DataTypes.StringType)
            .add("productManufacturingCity", DataTypes.StringType);

    private static StructType transactionSchema = new StructType()
            .add("transactionId", DataTypes.IntegerType)
            .add("productId", DataTypes.IntegerType)
            .add("transactionAmount", DataTypes.DoubleType)
            .add("transactionDatetime", DataTypes.StringType);

    @PostConstruct
    public void init() throws StreamingQueryException {
        log.info("Initialising streaming read from folder: {}", STREAMING_FILE_PATH);

        Dataset<Row> productRefDf = sparkSession.read()
                .option("header", true)
                .format("csv").schema(productRefSchema)
                .csv(PRODUCT_REF_FILE);
        productRefDf.createOrReplaceTempView("productRefData");
        updateStaticRefData(productRefDf);

        Dataset<Row> transactionDf = sparkSession.readStream()
                .option("header", true)
                .format("csv").schema(transactionSchema)
                .csv(STREAMING_FILE_PATH);
        transactionDf.createOrReplaceTempView("transactionData");

        String q1 = String.format("select * from transactionData");
        Dataset<Row> transInfo = sparkSession.sql(q1);
        sendTransactionsToSink(transInfo);

        String q2 = String.format("select t.productId, p.productName,p.productManufacturingCity," +
                "t.transactionAmount, t.transactionDatetime  from transactionData t " +
                "JOIN productRefData p ON p.productId = t.productId ");

        Dataset<Row> transProductInfo = sparkSession.sql(q2);
        sendTransactionsWithProductInfoToSink(transProductInfo);
    }

    private void sendTransactionsToSink(Dataset<Row> result){
        StreamingQuery q = result
                .writeStream()
                .foreach(new ForeachWriter<Row>() {
                    @Override
                    public boolean open(long partitionId, long epochId) {
                        return true;
                    }
                    @Override
                    public void process(Row value) {
                        log.debug("Received transaction row: {}", value );
                        transactionInfoService.updateSink(value.getInt(0),value.getInt(1), value.getDouble(2), value.getString(3) );
                    }
                    @Override
                    public void close(Throwable errorOrNull) {
                    }
                })
                .start();
    }
    private void updateStaticRefData(Dataset<Row> refData){

        refData.foreach(new ForeachFunction<Row>() {
            @Override
            public void call(Row row) throws Exception {
                log.debug("Loading static row as : {}", row);//ProductId, ProductName, ManufacturingCity
                transactionInfoService.updateStaticSink(row.getInt(0), row.getString(1), row.getString(2));
            }
        });
    }
    private void sendTransactionsWithProductInfoToSink(Dataset<Row> result){
        StreamingQuery q = result
                .writeStream()
                //.outputMode(OutputMode.Update())
                .foreach(new ForeachWriter<Row>() {
                    @Override
                    public boolean open(long partitionId, long epochId) {
                        return true;
                    }

                    @Override
                    public void process(Row value) {
                        log.debug("Received transaction row with productInfo {}", value ); //productId,name,city,Amount,date
                        try {
                            summaryService.updateTransSinkWithProductInfo(value.getString(1), value.getString(2), value.getDouble(3), StreamUtils.stringToDate(value.getString(4)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void close(Throwable errorOrNull) {

                    }
                })
                .start();
    }
}
