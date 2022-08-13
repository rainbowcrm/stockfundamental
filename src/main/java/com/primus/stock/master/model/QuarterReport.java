package com.primus.stock.master.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name ="quarter_reports")
public class QuarterReport {

    Long id;
    String bseCode ;
    String stock ;
    Long year;
    Integer quarter;
    Double revenue;
    Double expenditure ;
    Double profit;
    Double equity;
    Double divident ;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name  ="BSE_CODE")
    public String getBseCode() {
        return bseCode;
    }

    public void setBseCode(String bseCode) {
        this.bseCode = bseCode;
    }

    @Column(name  ="SECURITY_NAME")
    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Column(name  ="YEAR")
    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    @Column(name  ="QUARTER")
    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
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
    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    @Column(name  ="EQUITY")
    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    @Column(name  ="DIVIDENT")
    public Double getDivident() {
        return divident;
    }

    public void setDivident(Double divident) {
        this.divident = divident;
    }

    public QuarterReport() {
    }

    public QuarterReport(FinancialData financialData, long year, int quarter) {
        this.bseCode = financialData.getBseCode();
        this.stock = financialData.getCompany();
        this.year = year;
        this.quarter = quarter;
        this.revenue = financialData.getRevenue();
        this.expenditure = financialData.getExpenditure();
        this.profit = financialData.getNetProfit();
        this.equity = financialData.getEquit();
        this.divident = financialData.getDivident();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuarterReport that = (QuarterReport) o;
        return
                Objects.equals(bseCode, that.bseCode) &&
                Objects.equals(stock, that.stock) &&
                Objects.equals(revenue, that.revenue) &&
                Objects.equals(expenditure, that.expenditure) &&
                Objects.equals(profit, that.profit) &&
                Objects.equals(equity, that.equity) &&
                Objects.equals(divident, that.divident);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bseCode, stock, year, quarter, revenue, expenditure, profit, equity, divident);
    }
}
