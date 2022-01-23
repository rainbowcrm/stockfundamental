package com.primus.ui.model;

import com.primus.common.datastructures.DataPair;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;

import java.util.List;
import java.util.Date;

public class FullStockProfile  extends StockCompleteData{

    List<DailyPrice> prices ;

    Double medianPrice;
    Double stdDeviation;
    Double meanPrice;

    public List<DailyPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<DailyPrice> prices) {
        this.prices = prices;
    }

    public Double getMedianPrice() {
        return medianPrice;
    }

    public void setMedianPrice(Double medianPrice) {
        this.medianPrice = medianPrice;
    }


    public Double getStdDeviation() {
        return stdDeviation;
    }

    public void setStdDeviation(Double stdDeviation) {
        this.stdDeviation = stdDeviation;
    }

    public Double getMeanPrice() {
        return meanPrice;
    }

    public void setMeanPrice(Double meanPrice) {
        this.meanPrice = meanPrice;
    }

    public FullStockProfile() {
    }

    public FullStockProfile(FundamentalData fundamentalData, FinancialData financialData, StocksMaster stocksMaster) {
        super(fundamentalData, financialData, stocksMaster);
    }
}
