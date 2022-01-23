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
    Double variance;

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

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public FullStockProfile() {
    }

    public FullStockProfile(FundamentalData fundamentalData, FinancialData financialData, StocksMaster stocksMaster) {
        super(fundamentalData, financialData, stocksMaster);
    }
}
