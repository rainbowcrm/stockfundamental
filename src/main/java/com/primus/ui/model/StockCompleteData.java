package com.primus.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;

import java.util.Map;

public class StockCompleteData {

    String bseCode ;
    String stock ;
    String industry ;
    String sector ;
    String group ;
    Double faceValue ;
    Double currentPrice;
    Double eps;
    Double bookvalue ;
    Double roe ;
    Double revenue;
    Double expenditure ;
    Double profit;
    Double equity;
    Double divident ;

    public String getBseCode() {
        return bseCode;
    }

    public void setBseCode(String bseCode) {
        this.bseCode = bseCode;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Double getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Double faceValue) {
        this.faceValue = faceValue;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getEps() {
        return eps;
    }

    public void setEps(Double eps) {
        this.eps = eps;
    }

    public Double getBookvalue() {
        return bookvalue;
    }

    public void setBookvalue(Double bookvalue) {
        this.bookvalue = bookvalue;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    public Double getDivident() {
        return divident;
    }

    public void setDivident(Double divident) {
        this.divident = divident;
    }

    @JsonIgnore
    public Map<String,Object> getMap()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this,Map.class);
    }

    public StockCompleteData() {
    }
    public StockCompleteData(FundamentalData fundamentalData, FinancialData financialData, StocksMaster stocksMaster) {
        this.bseCode = fundamentalData.getBseCode() ;
        this.stock = fundamentalData.getCompany() ;
        this.industry= fundamentalData.getIndustry();
        this.sector = fundamentalData.getSector() ;
        this.group= stocksMaster.getGroupC().trim();
        this.faceValue = stocksMaster.getFaceValue() ;
        this.currentPrice = fundamentalData.getCurPrice() ;
        this.eps = fundamentalData.getEps() ;
        this.bookvalue= fundamentalData.getBookValue();
        this.roe = fundamentalData.getRoe() ;
        if (financialData != null) {

            this.revenue = financialData.getRevenue();
            this.expenditure = financialData.getExpenditure();
            this.profit = financialData.getNetProfit();
            this.equity = financialData.getEquit();
            this.divident = financialData.getDivident();
        }

    }
}
