package com.primus.stocktransaction.service;

import com.primus.common.LogWriter;
import com.primus.stock.api.service.APIService;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stock.master.service.StockMasterService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class StockTransactionService {

    @Autowired
    StockMasterService stockMasterService;

    @Autowired
    APIService apiService;

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    FundamentalService fundamentalService;

    public void saveDailyTransactions(String groupC)
    {
        List<StocksMaster> stocksMasterList =  stockMasterService.getAllTrackedStocks(groupC);
        for (StocksMaster stocksMaster : stocksMasterList) {
            try {
                Map<String, Object> scripHeaderData = apiService.getScripHeaderData(stocksMaster.getBseCode());
                Map<String, String> headerMap = (Map) scripHeaderData.get("Header");
                Double openPrice =  Double.parseDouble(headerMap.get("Open"));
                Double highPrice =  Double.parseDouble(headerMap.get("High"));
                Double lowPrice =   Double.parseDouble(headerMap.get("Low"));
                Double closePrice=  Double.parseDouble(headerMap.get("LTP"));
                String transDateStr = headerMap.get("Ason");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy | hh:mm");
                Date transDate = simpleDateFormat.parse(transDateStr);

                StockTransaction stockTransaction = new StockTransaction();
                stockTransaction.setApi_code(stocksMaster.getApiCode());
                stockTransaction.setSecurity_name(stocksMaster.getSecurityName());
                stockTransaction.setStocksMaster(stocksMaster);
                stockTransaction.setOpenPrice(openPrice);
                stockTransaction.setClosePrice(closePrice);
                stockTransaction.setLowPrice(lowPrice);
                stockTransaction.setHighPrice(highPrice);
                stockTransaction.setTransDate(transDate);
                LogWriter.debug(stockTransaction.toString());
                stockTransactionDAO.update(stockTransaction);

                FundamentalData fundamentalData = fundamentalService.getFundamentalData(stocksMaster.getBseCode());
                if (fundamentalData != null ) {
                    fundamentalData.setCurPrice(closePrice);
                    LogWriter.debug(fundamentalData.toString());
                    fundamentalService.updateFundamentals(fundamentalData);
                }
                Thread.sleep(100);

            }catch (Exception exception) {
                LogWriter.logException("Ex in StockTransactionservice" ,this.getClass(),exception);
            }
        }

    }

}


