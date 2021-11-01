package com.primus.stock.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrRate {

    String lTP;

    public String getlTP() {
        return lTP;
    }

    public void setlTP(String lTP) {
        this.lTP = lTP;
    }
}
