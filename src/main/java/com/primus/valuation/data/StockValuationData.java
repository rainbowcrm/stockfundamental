package com.primus.valuation.data;

import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.ui.model.FullStockProfile;
import com.primus.ui.model.StockCompleteData;

public class StockValuationData extends FullStockProfile {

    private float overValuedBy;

    public float getOverValuedBy() {
        return overValuedBy;
    }

    private IntrinsicData intrinsicData;

    private Double fairPrice;

    public void setOverValuedBy(float overValuedBy) {
        this.overValuedBy = overValuedBy;
    }

    public StockValuationData() {
    }

    public StockValuationData(FundamentalData fundamentalData, FinancialData financialData, StocksMaster stocksMaster) {
        super(fundamentalData, financialData, stocksMaster);
    }

    @Override
    public String toString() {
           return "StockCompleteData{" +
                "bseCode='" + getBseCode() + '\'' +
                ", stock='" + getStock() + '\'' +
                ", industry='" + getIndustry() + '\'' +
                ", sector='" + getSector() + '\'' +
                ", currentPrice=" + getCurrentPrice() +
                ", pe=" + getPe() +
                ", pb=" + getPb() +
                ", groupCap='" + getGroupCap() + '\'' +
                ", overValuedBy='" + overValuedBy + '\'' +
                '}';
    }

    public IntrinsicData getIntrinsicData() {
        return intrinsicData;
    }

    public void setIntrinsicData(IntrinsicData intrinsicData) {
        this.intrinsicData = intrinsicData;
    }

    public Double getFairPrice() {
        return fairPrice;
    }

    public void setFairPrice(Double fairPrice) {
        this.fairPrice = fairPrice;
    }
}
