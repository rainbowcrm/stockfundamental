package com.primus.dashboard.model;

public class CapCardData {

    Integer noOfSecurities ;
    Integer gainers;
    Integer capLNo;
    Integer capLGainer;
    Double  capLGainPerc;
    Integer capMNo;
    Integer capMGainer;
    Double  capMGainPerc;
    Integer capSNo;
    Integer capSGainer;
    Double  capSGainPerc;

    public Integer getNoOfSecurities() {
        return noOfSecurities;
    }

    public void setNoOfSecurities(Integer noOfSecurities) {
        this.noOfSecurities = noOfSecurities;
    }

    public Integer getGainers() {
        return gainers;
    }

    public void setGainers(Integer gainers) {
        this.gainers = gainers;
    }

    public Integer getCapLNo() {
        return capLNo;
    }

    public void setCapLNo(Integer capLNo) {
        this.capLNo = capLNo;
    }

    public Integer getCapLGainer() {
        return capLGainer;
    }

    public void setCapLGainer(Integer capLGainer) {
        this.capLGainer = capLGainer;
    }

    public Integer getCapMNo() {
        return capMNo;
    }

    public void setCapMNo(Integer capMNo) {
        this.capMNo = capMNo;
    }

    public Integer getCapMGainer() {
        return capMGainer;
    }

    public void setCapMGainer(Integer capMGainer) {
        this.capMGainer = capMGainer;
    }

    public Integer getCapSNo() {
        return capSNo;
    }

    public void setCapSNo(Integer capSNo) {
        this.capSNo = capSNo;
    }

    public Integer getCapSGainer() {
        return capSGainer;
    }

    public void setCapSGainer(Integer capSGainer) {
        this.capSGainer = capSGainer;
    }

    public CapCardData() {
    }

    public CapCardData(Integer noOfSecurities, Integer gainers, Integer capLNo, Integer capLGainer, Integer capMNo, Integer capMGainer, Integer capSNo, Integer capSGainer) {
        this.noOfSecurities = noOfSecurities;
        this.gainers = gainers;
        this.capLNo = capLNo;
        this.capLGainer = capLGainer;
        this.capMNo = capMNo;
        this.capMGainer = capMGainer;
        this.capSNo = capSNo;
        this.capSGainer = capSGainer;
    }

    public Double getCapLGainPerc() {
        return capLGainPerc;
    }

    public void setCapLGainPerc(Double capLGainPerc) {
        this.capLGainPerc = capLGainPerc;
    }

    public Double getCapMGainPerc() {
        return capMGainPerc;
    }

    public void setCapMGainPerc(Double capMGainPerc) {
        this.capMGainPerc = capMGainPerc;
    }

    public Double getCapSGainPerc() {
        return capSGainPerc;
    }

    public void setCapSGainPerc(Double capSGainPerc) {
        this.capSGainPerc = capSGainPerc;
    }
}
