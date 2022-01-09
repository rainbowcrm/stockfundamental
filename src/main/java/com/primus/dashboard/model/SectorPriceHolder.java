package com.primus.dashboard.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SectorPriceHolder  implements Serializable {
    List<String> sectors;
    List<Map.Entry<String,List<Double>>> dateValues;

    public List<String> getSectors() {
        return sectors;
    }

    public void setSectors(List<String> sectors) {
        this.sectors = sectors;
    }

    public List<Map.Entry<String, List<Double>>> getDateValues() {
        return dateValues;
    }

    public void setDateValues(List<Map.Entry<String, List<Double>>> dateValues) {
        this.dateValues = dateValues;
    }
}
