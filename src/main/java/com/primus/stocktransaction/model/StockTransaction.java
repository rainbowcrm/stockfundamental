package com.primus.stocktransaction.model;

import com.primus.stock.master.model.StocksMaster;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="STOCK_TRANSACTION")
public class StockTransaction {

    long id;
    String api_code ;
    String security_name;
    StocksMaster stocksMaster ;
    Date transDate;
    Double openPrice;
    Double lowPrice;
    Double highPrice;
    Double closePrice;
    Integer volume;


    @Column(name  ="ID")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name  ="API_CODE")
    public String getApi_code() {
        return api_code;
    }

    public void setApi_code(String api_code) {
        this.api_code = api_code;
    }

    @Column(name  ="SECURITY_NAME")
    public String getSecurity_name() {
        return security_name;
    }

    public void setSecurity_name(String security_name) {
        this.security_name = security_name;
    }

    @ManyToOne(cascade= CascadeType.DETACH)
    @JoinColumn(name  ="STOCK_ID")
    public StocksMaster getStocksMaster() {
        return stocksMaster;
    }

    public void setStocksMaster(StocksMaster stocksMaster) {
        this.stocksMaster = stocksMaster;
    }

    @Column(name  ="TRANS_DATE")
    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    @Column(name  ="OPEN_PRICE")
    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    @Column(name  ="LOW_PRICE")
    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    @Column(name  ="HIGH_PRICE")
    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    @Column(name  ="CLOSE_PRICE" )
    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
