package com.primus.ui.model;


import java.util.List;

public class CompetitorData {

    Double medianPE;
    Double medianPB;
    Double medianROE;
    Double meanDivYield;

    Double capMedianPE;
    Double capMedianPB;
    Double capMedianROE;
    Double capMeanDivYield;



    List<StockCompleteData> competitorDataList ;

    public Double getMedianPE() {
        return medianPE;
    }

    public void setMedianPE(Double medianPE) {
        this.medianPE = medianPE;
    }

    public Double getMedianPB() {
        return medianPB;
    }

    public void setMedianPB(Double medianPB) {
        this.medianPB = medianPB;
    }

    public Double getMedianROE() {
        return medianROE;
    }

    public void setMedianROE(Double medianROE) {
        this.medianROE = medianROE;
    }

    public Double getMeanDivYield() {
        return meanDivYield;
    }

    public void setMeanDivYield(Double meanDivYield) {
        this.meanDivYield = meanDivYield;
    }

    public List<StockCompleteData> getCompetitorDataList() {
        return competitorDataList;
    }

    public void setCompetitorDataList(List<StockCompleteData> competitorDataList) {
        this.competitorDataList = competitorDataList;
    }

    public Double getCapMedianPE() {
        return capMedianPE;
    }

    public void setCapMedianPE(Double capMedianPE) {
        this.capMedianPE = capMedianPE;
    }

    public Double getCapMedianPB() {
        return capMedianPB;
    }

    public void setCapMedianPB(Double capMedianPB) {
        this.capMedianPB = capMedianPB;
    }

    public Double getCapMedianROE() {
        return capMedianROE;
    }

    public void setCapMedianROE(Double capMedianROE) {
        this.capMedianROE = capMedianROE;
    }

    public Double getCapMeanDivYield() {
        return capMeanDivYield;
    }

    public void setCapMeanDivYield(Double capMeanDivYield) {
        this.capMeanDivYield = capMeanDivYield;
    }
}
