package com.primus.valuation.service;

import com.primus.common.datastructures.DataPair;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.FinancialService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.ui.model.DailyPrice;
import com.primus.ui.model.FullStockProfile;
import com.primus.ui.model.TechnicalData;
import com.primus.utils.MathUtil;
import com.primus.valuation.data.StockValuationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ValuationHelper {

    @Autowired
    FundamentalService fundamentalService ;

    @Autowired
    FinancialService financialService;

    @Autowired
    StockTransactionDAO stockTransactionDAO ;

    public List<StockValuationData> getStockCompleteDataList(List<StocksMaster> stocksMasterList,Boolean includeTechnicals)
    {
        List<FundamentalData> fundamentalDataList = fundamentalService.listData(0,900,"","");
        List<FinancialData> financialDataList = financialService.getAllFinancials() ;

        List<StockValuationData> stockCompleteDataList = new ArrayList<>();
        for (FundamentalData fundamentalData : fundamentalDataList) {
            FinancialData financialDataSel = financialDataList.stream().filter( financialData ->
            {  return financialData.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null) ;
            StocksMaster stocksMasterSel = stocksMasterList.stream().filter(stocksMaster ->
            { return  stocksMaster.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null);
            StockValuationData stockCompleteData = new StockValuationData(fundamentalData,financialDataSel,stocksMasterSel);
            if(includeTechnicals) {
                setTechnicals(stockCompleteData,90);
            }
            stockCompleteDataList.add(stockCompleteData);
        }
        return stockCompleteDataList;
    }

    public   void setTechnicals(FullStockProfile stockCompleteData,int days)
    {
        Date toDate = new java.util.Date();
        Date fromDate = new Date(toDate.getTime() - days * 24l  * 3600l * 1000l ) ;
        List<StockTransaction> stockTransactionList = stockTransactionDAO.getDataForStock(fromDate,toDate,stockCompleteData.getStock());
        List<DailyPrice> dataPair = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        List<Double> closingPrices = new ArrayList<>();
        for (StockTransaction stockTransaction :stockTransactionList) {
            String str = sdf.format(stockTransaction.getTransDate());
            dataPair.add(new DailyPrice(str,stockTransaction.getOpenPrice(),stockTransaction.getLowPrice(),stockTransaction.getClosePrice(),
                    stockTransaction.getHighPrice()));
            closingPrices.add(stockTransaction.getClosePrice());

        }

        stockCompleteData.setPrices(dataPair);
        stockCompleteData.setTechnicalData(getPivotPoints(stockTransactionList));
        if (closingPrices.size() >0 ) {
            Double medPrice = MathUtil.round(MathUtil.getMedian(closingPrices));
            Double meanPrice = MathUtil.round(MathUtil.getMean(closingPrices));
            Double stdDeviation = MathUtil.round(MathUtil.getStandardDeviation(closingPrices));
            Double relDeviation =MathUtil.round(MathUtil.getRelStandardDeviation(closingPrices));
            stockCompleteData.setMedianPrice(medPrice);
            stockCompleteData.setMeanPrice(meanPrice);
            stockCompleteData.setStdDeviation(stdDeviation);
            stockCompleteData.setRelStdDeviation(relDeviation);
            DataPair<Double, Double> minMax = MathUtil.getMinMax(closingPrices);
            stockCompleteData.setMinPrice(minMax.getValue1());
            stockCompleteData.setMaxPrice(minMax.getValue2());
            Double rootSize = Math.sqrt(closingPrices.size());
            stockCompleteData.setVolatality(MathUtil.round(rootSize * stdDeviation));
            stockCompleteData.setPercVariation(MathUtil.round(((minMax.getValue2() - minMax.getValue1()) / minMax.getValue1()) * 100));
        }

    }

    public TechnicalData getPivotPoints(List<StockTransaction> stockTransactionList)
    {
        if( stockTransactionList.size() > 0 ) {
            Double high = Double.MIN_VALUE;
            Double low = Double.MAX_VALUE;
            for (StockTransaction stockTransaction : stockTransactionList) {
                if (stockTransaction.getHighPrice() > high) {
                    high = stockTransaction.getHighPrice();
                }
                if (stockTransaction.getLowPrice() < low) {
                    low = stockTransaction.getLowPrice();
                }
            }
            Double close = stockTransactionList.get(stockTransactionList.size() - 1).getClosePrice();
            Double pp = MathUtil.round((high + low + close) / 3);
            Double support1 = MathUtil.round((pp * 2) - high);
            Double support2 = MathUtil.round(pp - (high - low));
            Double resistance1 = MathUtil.round((pp * 2) - low);
            Double resisance2 = MathUtil.round(pp + (high - low));
            TechnicalData technicalData = new TechnicalData(pp, resistance1, resisance2, support1, support2);
            return technicalData;
        }else
                return new TechnicalData();



    }

}
