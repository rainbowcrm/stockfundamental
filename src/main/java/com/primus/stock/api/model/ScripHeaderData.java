package com.primus.stock.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScripHeaderData {
    CurrRate currRate ;

    public CurrRate getCurrRate() {
        return currRate;
    }

    public void setCurrRate(CurrRate currRate) {
        this.currRate = currRate;
    }
}
