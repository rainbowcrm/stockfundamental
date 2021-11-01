package com.primus.stock.api.model;

import java.util.Map;

public class FundamentalData {
    Map comHeaderData ;
    Map scripHeaderData ;

    public Map getComHeaderData() {
        return comHeaderData;
    }

    public void setComHeaderData(Map comHeaderData) {
        this.comHeaderData = comHeaderData;
    }

    public Map getScripHeaderData() {
        return scripHeaderData;
    }

    public void setScripHeaderData(Map scripHeaderData) {
        this.scripHeaderData = scripHeaderData;
    }

    public FundamentalData(Map comHeaderData, Map scripHeaderData) {
        this.comHeaderData = comHeaderData;
        this.scripHeaderData = scripHeaderData;
    }
}
