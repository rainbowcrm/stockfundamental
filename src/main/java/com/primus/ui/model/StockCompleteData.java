package com.primus.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.stock.master.model.DividentHistory;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.utils.MathUtil;

import java.sql.Date;
import java.util.Map;
import java.util.Objects;
import java.util.List;

public class StockCompleteData {

    String bseCode ;
    String stock ;
    String industry ;
    String sector ;
    String isin;
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
    Double dividentYield;
    Double pe;
    Double pb;
    Double marketCap;
    String groupCap;
    Double profitIncr;

    Double latestDivident;
    Date latestDivExDate;



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

    public Double getPe() {
        return pe;
    }

    public void setPe(Double pe) {
        this.pe = pe;
    }

    public Double getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    @JsonIgnore
    public Map<String,Object> getMap()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this,Map.class);
    }

    public StockCompleteData() {

    }
    public StockCompleteData(FundamentalData fundamentalData, FinancialData financialData, StocksMaster stocksMaster)
    {
        this(fundamentalData,financialData,stocksMaster,null);
    }

    public StockCompleteData(FundamentalData fundamentalData, FinancialData financialData, StocksMaster stocksMaster, List<DividentHistory> dividentHistoryList) {
        this.bseCode = fundamentalData.getBseCode() ;
        this.stock = fundamentalData.getCompany() ;
        this.industry= fundamentalData.getIndustry();
        this.sector = fundamentalData.getSector() ;
        this.group= stocksMaster.getGroupC().trim();
        this.marketCap=fundamentalData.getMarketCap();
        this.groupCap=fundamentalData.getMarketGroup();
        this.faceValue = stocksMaster.getFaceValue() ;
        this.isin = stocksMaster.getIsinNo();
        this.currentPrice = fundamentalData.getCurPrice() ;
        this.eps = fundamentalData.getEps() ;
        this.bookvalue= fundamentalData.getBookValue();
        this.roe = fundamentalData.getRoe() ;
        if (financialData != null) {

            this.revenue = financialData.getRevenue();
            this.expenditure = financialData.getExpenditure();
            this.profit = financialData.getNetProfit();
            this.equity = financialData.getEquit();
            this.divident = financialData.getDivident()==null?0:financialData.getDivident();
            this.dividentYield = MathUtil.round((divident/currentPrice) * 100);


        }
        if(this.eps != 0 ){
            this.pe = Math.round((this.currentPrice/this.eps)*100.0)/100.0;
        }
        if(this.bookvalue != 0) {
            this.pb = Math.round((this.currentPrice / this.bookvalue)*100.0)/100.0;
        }


    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public String getGroupCap() {
        return groupCap;
    }

    public void setGroupCap(String groupCap) {
        this.groupCap = groupCap;
    }

    @Override
    public String toString() {
        return "StockCompleteData{" +
                "bseCode='" + bseCode + '\'' +
                ", stock='" + stock + '\'' +
                ", industry='" + industry + '\'' +
                ", sector='" + sector + '\'' +
                ", currentPrice=" + currentPrice +
                ", pe=" + pe +
                ", pb=" + pb +
                ", marketCap=" + marketCap +
                ", groupCap='" + groupCap + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockCompleteData that = (StockCompleteData) o;
        return bseCode.equals(that.bseCode) &&
                stock.equals(that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bseCode, stock);
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public Double getDividentYield() {
        return dividentYield;
    }

    public void setDividentYield(Double dividentYield) {
        this.dividentYield = dividentYield;
    }

    public Double getProfitIncr() {
        return profitIncr;
    }

    public void setProfitIncr(Double profitIncr) {
        this.profitIncr = profitIncr;
    }


    public Double getLatestDivident() {
        return latestDivident;
    }

    public void setLatestDivident(Double latestDivident) {
        this.latestDivident = latestDivident;
    }

    public Date getLatestDivExDate() {
        return latestDivExDate;
    }

    public void setLatestDivExDate(Date latestDivExDate) {
        this.latestDivExDate = latestDivExDate;
    }
}
