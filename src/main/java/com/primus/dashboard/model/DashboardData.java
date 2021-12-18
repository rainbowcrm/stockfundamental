package com.primus.dashboard.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DashboardData  implements  Serializable{

     CapCardData capCardData ;
     GroupCardData groupCardData ;
     Averages fullDataAvg;
     Averages lCDataAvg;
     Averages sCDataAvg;
     Averages mCDataAvg;
     List<SectorDetails> sectorDetailsList;
     SectorPriceHolder sectorPriceHolder ;
     Map<String,Double > hikeStocks;
     Map<String,Float> uvShares;
     Map<String,Float> ovShares;



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

    public Averages getlCDataAvg() {
        return lCDataAvg;
    }

    public void setlCDataAvg(Averages lCDataAvg) {
        this.lCDataAvg = lCDataAvg;
    }

    public Averages getsCDataAvg() {
        return sCDataAvg;
    }

    public void setsCDataAvg(Averages sCDataAvg) {
        this.sCDataAvg = sCDataAvg;
    }

    public Averages getmCDataAvg() {
        return mCDataAvg;
    }

    public void setmCDataAvg(Averages mCDataAvg) {
        this.mCDataAvg = mCDataAvg;
    }

    public List<SectorDetails> getSectorDetailsList() {
        return sectorDetailsList;
    }

    public void setSectorDetailsList(List<SectorDetails> sectorDetailsList) {
        this.sectorDetailsList = sectorDetailsList;
    }

    public SectorPriceHolder getSectorPriceHolder() {
        return sectorPriceHolder;
    }

    public void setSectorPriceHolder(SectorPriceHolder sectorPriceHolder) {
        this.sectorPriceHolder = sectorPriceHolder;
    }

    public Map<String, Double> getHikeStocks() {
        return hikeStocks;
    }

    public void setHikeStocks(Map<String, Double> hikeStocks) {
        this.hikeStocks = hikeStocks;
    }

    public Map<String, Float> getUvShares() {
        return uvShares;
    }

    public void setUvShares(Map<String, Float> uvShares) {
        this.uvShares = uvShares;
    }

    public Map<String, Float> getOvShares() {
        return ovShares;
    }

    public void setOvShares(Map<String, Float> ovShares) {
        this.ovShares = ovShares;
    }
}


