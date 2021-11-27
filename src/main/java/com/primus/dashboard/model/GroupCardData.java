package com.primus.dashboard.model;

public class GroupCardData {

    Integer noOfSecurities ;
    Integer gainers;
    Integer groupANo;
    Integer groupAGainer;
    Double  groupAGainPerc;
    Integer groupBNo;
    Integer groupBGainer;
    Double  groupBGainPerc;
    Integer groupXNo;
    Integer groupXGainer;
    Double  groupXGainPerc;
    Double  totalGainPerc;

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

    public Integer getGroupANo() {
        return groupANo;
    }

    public void setGroupANo(Integer groupANo) {
        this.groupANo = groupANo;
    }

    public Integer getGroupAGainer() {
        return groupAGainer;
    }

    public void setGroupAGainer(Integer groupAGainer) {
        this.groupAGainer = groupAGainer;
    }

    public Integer getGroupBNo() {
        return groupBNo;
    }

    public void setGroupBNo(Integer groupBNo) {
        this.groupBNo = groupBNo;
    }

    public Integer getGroupBGainer() {
        return groupBGainer;
    }

    public void setGroupBGainer(Integer groupBGainer) {
        this.groupBGainer = groupBGainer;
    }

    public Integer getGroupXNo() {
        return groupXNo;
    }

    public void setGroupXNo(Integer groupXNo) {
        this.groupXNo = groupXNo;
    }

    public Integer getGroupXGainer() {
        return groupXGainer;
    }

    public void setGroupXGainer(Integer groupXGainer) {
        this.groupXGainer = groupXGainer;
    }

    public GroupCardData()
    {

    }
    public GroupCardData(Integer noOfSecurities, Integer gainers, Integer groupANo, Integer groupAGainer, Integer groupBNo, Integer groupBGainer, Integer groupXNo, Integer groupXGainer) {
        this.noOfSecurities = noOfSecurities;
        this.gainers = gainers;
        this.groupANo = groupANo;
        this.groupAGainer = groupAGainer;
        this.groupBNo = groupBNo;
        this.groupBGainer = groupBGainer;
        this.groupXNo = groupXNo;
        this.groupXGainer = groupXGainer;
    }

    public Double getGroupAGainPerc() {
        return groupAGainPerc;
    }

    public void setGroupAGainPerc(Double groupAGainPerc) {
        this.groupAGainPerc = groupAGainPerc;
    }

    public Double getGroupBGainPerc() {
        return groupBGainPerc;
    }

    public void setGroupBGainPerc(Double groupBGainPerc) {
        this.groupBGainPerc = groupBGainPerc;
    }

    public Double getGroupXGainPerc() {
        return groupXGainPerc;
    }

    public void setGroupXGainPerc(Double groupXGainPerc) {
        this.groupXGainPerc = groupXGainPerc;
    }

    public Double getTotalGainPerc() {
        return totalGainPerc;
    }

    public void setTotalGainPerc(Double totalGainPerc) {
        this.totalGainPerc = totalGainPerc;
    }
}
