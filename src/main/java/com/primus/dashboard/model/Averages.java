package com.primus.dashboard.model;

import java.io.Serializable;

public class Averages implements Serializable {
    Double rateOfIncrease;
    Double peRatio;
    Double pbRatio ;
    Double roe;

    public Double getRateOfIncrease() {
        return rateOfIncrease;
    }

    public void setRateOfIncrease(Double rateOfIncrease) {
        this.rateOfIncrease = rateOfIncrease;
    }

    public Double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(Double peRatio) {
        this.peRatio = peRatio;
    }

    public Double getPbRatio() {
        return pbRatio;
    }

    public void setPbRatio(Double pbRatio) {
        this.pbRatio = pbRatio;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }
}
