package com.primus.stock.master.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="FINANCIALS")
public class FinancialData {

    String bseCode;
    String company;
    String industry;
    Double revenue ;
    Double expenditure;
    Double netProfit;
    Double equit;
    Double divident;

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

    @Column(name  ="REVENUE")
    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    @Column(name  ="EXPENDITURE")
    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    @Column(name  ="PROFIT")
    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    @Column(name  ="EQUITY")
    public Double getEquit() {
        return equit;
    }

    public void setEquit(Double equit) {
        this.equit = equit;
    }

    @Column(name  ="DIVIDENT")
    public Double getDivident() {
        return divident;
    }

    public void setDivident(Double divident) {
        this.divident = divident;
    }
}
