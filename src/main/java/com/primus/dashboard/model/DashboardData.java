package com.primus.dashboard.model;

public class DashboardData {

     CapCardData capCardData ;
     GroupCardData groupCardData ;
     Averages fullDataAvg;

    public CapCardData getCapCardData() {
        return capCardData;
    }

    public void setCapCardData(CapCardData capCardData) {
        this.capCardData = capCardData;
    }

    public GroupCardData getGroupCardData() {
        return groupCardData;
    }

    public void setGroupCardData(GroupCardData groupCardData) {
        this.groupCardData = groupCardData;
    }

    public Averages getFullDataAvg() {
        return fullDataAvg;
    }

    public void setFullDataAvg(Averages fullDataAvg) {
        this.fullDataAvg = fullDataAvg;
    }
}
