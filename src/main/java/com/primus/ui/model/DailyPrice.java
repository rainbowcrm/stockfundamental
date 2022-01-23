package com.primus.ui.model;

public class DailyPrice {
    String date;
    Double openingPrice;
    Double lowPrice;
    Double closingPrice;
    Double highPrice;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(Double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(Double closingPrice) {
        this.closingPrice = closingPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public DailyPrice() {
    }

    public DailyPrice(String date, Double openingPrice, Double lowPrice, Double closingPrice, Double highPrice) {
        this.date = date;
        this.openingPrice = openingPrice;
        this.lowPrice = lowPrice;
        this.closingPrice = closingPrice;
        this.highPrice = highPrice;
    }
}
