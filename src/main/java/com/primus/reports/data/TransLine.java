package com.primus.reports.data;

import java.util.Date;

public class TransLine {
    Date date;
    Double opening;
    Double closing ;
    Double high;
    Double low;
    Integer volume;

    public TransLine(Date date, Double opening, Double closing, Double high, Double low, Integer volume) {
        this.date = date;
        this.opening = opening;
        this.closing = closing;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    public TransLine() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getOpening() {
        return opening;
    }

    public void setOpening(Double opening) {
        this.opening = opening;
    }

    public Double getClosing() {
        return closing;
    }

    public void setClosing(Double closing) {
        this.closing = closing;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
