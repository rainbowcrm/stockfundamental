package com.primus.stock.master.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name ="FUNDAMENTALS")
public class FundamentalData {

    String bseCode;
    String company;
    String industry;
    String sector;
    Double curPrice ;
    Double eps;
    Double bookValue;
    Double qtrRevenue;
    Double netIncome;
    Double roe;

    Date priceLastUpdated;
    Date fundaLastUpdated;

    Double marketCap;
    Double marketCapFF;
    String marketGroup;



    @Column(name  ="BSE_CODE")
    @Id
    public String getBseCode() {
        return bseCode;
    }

    public void setBseCode(String bseCode) {
        this.bseCode = bseCode;
    }



    @Column(name  ="COMPANY")
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Column(name  ="INDUSTRY")
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Column(name  ="SECTOR")
    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Column(name  ="CURR_PRICE")
    public Double getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(Double curPrice) {
        this.curPrice = curPrice;
    }

    @Column(name  ="EPS")
    public Double getEps() {
        return eps;
    }

    public void setEps(Double eps) {
        this.eps = eps;
    }

    @Column(name  ="BOOK_VALUE")
    public Double getBookValue() {
        return bookValue;
    }

    public void setBookValue(Double bookValue) {
        this.bookValue = bookValue;
    }

    @Column(name  ="QTR_REVENUE")
    public Double getQtrRevenue() {
        return qtrRevenue;
    }

    public void setQtrRevenue(Double qtrRevenue) {
        this.qtrRevenue = qtrRevenue;
    }

    @Column(name  ="NET_INCOME")
    public Double getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(Double netIncome) {
        this.netIncome = netIncome;
    }

    @Column(name  ="ROE")
    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    @Column(name  ="PRICE_LAST_UPDATED")
    public Date getPriceLastUpdated() {
        return priceLastUpdated;
    }

    public void setPriceLastUpdated(Date priceLastUpdated) {
        this.priceLastUpdated = priceLastUpdated;
    }

    @Column(name  ="FUND_LAST_UPDATED")
    public Date getFundaLastUpdated() {
        return fundaLastUpdated;
    }

    public void setFundaLastUpdated(Date fundaLastUpdated) {
        this.fundaLastUpdated = fundaLastUpdated;
    }

    @Override
    public String toString() {
        return "FundamentalData{" +
                "bseCode='" + bseCode + '\'' +
                ", company='" + company + '\'' +
                ", curPrice=" + curPrice +
                '}';
    }

    @Column(name  ="MARKET_CAPT")
    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    @Column(name  ="MARKET_CAPT_FF")
    public Double getMarketCapFF() {
        return marketCapFF;
    }

    public void setMarketCapFF(Double marketCapFF) {
        this.marketCapFF = marketCapFF;
    }

    @Column(name  ="MARKET_CAP_GROUP")
    public String getMarketGroup() {
        return marketGroup;
    }

    public void setMarketGroup(String marketGroup) {
        this.marketGroup = marketGroup;
    }
}
