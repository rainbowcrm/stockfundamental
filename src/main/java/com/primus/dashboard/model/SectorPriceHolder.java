package com.primus.dashboard.model;

import java.util.List;
import java.util.Map;

public class SectorPriceHolder {
    List<String> sectors;
    Map<String,List<Double>> dateValues;

    public List<String> getSectors() {
        return sectors;
    }

    public void setSectors(List<String> sectors) {
        this.sectors = sectors;
    }

    public Map<String, List<Double>> getDateValues() {
        return dateValues;
    }

    public void setDateValues(Map<String, List<Double>> dateValues) {
        this.dateValues = dateValues;
    }
}
