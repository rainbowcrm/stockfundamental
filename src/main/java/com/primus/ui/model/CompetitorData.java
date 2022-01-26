package com.primus.ui.model;


import java.util.List;

public class CompetitorData {

    Double medianPE;
    Double medianPB;
    Double medianROE;
    Double meanDivYield;

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
}
