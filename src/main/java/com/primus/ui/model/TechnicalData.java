package com.primus.ui.model;

public class TechnicalData {

    Double pivotPoint;
    Double resistance1;
    Double resistance2;
    Double support1;
    Double support2;

    public Double getPivotPoint() {
        return pivotPoint;
    }

    public void setPivotPoint(Double pivotPoint) {
        this.pivotPoint = pivotPoint;
    }

    public Double getResistance1() {
        return resistance1;
    }

    public void setResistance1(Double resistance1) {
        this.resistance1 = resistance1;
    }

    public Double getResistance2() {
        return resistance2;
    }

    public void setResistance2(Double resistance2) {
        this.resistance2 = resistance2;
    }

    public Double getSupport1() {
        return support1;
    }

    public void setSupport1(Double support1) {
        this.support1 = support1;
    }

    public Double getSupport2() {
        return support2;
    }

    public void setSupport2(Double support2) {
        this.support2 = support2;
    }

    public TechnicalData() {
    }

    public TechnicalData(Double pivotPoint, Double resistance1, Double resistance2, Double support1, Double support2) {
        this.pivotPoint = pivotPoint;
        this.resistance1 = resistance1;
        this.resistance2 = resistance2;
        this.support1 = support1;
        this.support2 = support2;
    }
}
