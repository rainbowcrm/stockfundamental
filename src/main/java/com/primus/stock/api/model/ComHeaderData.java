package com.primus.stock.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComHeaderData {

    String industry ;
    String ePS ;
    String pE;
    String pB;
    String sector ;
    String faceVal ;

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getePS() {
        return ePS;
    }

    public void setePS(String ePS) {
        this.ePS = ePS;
    }

    public String getpE() {
        return pE;
    }

    public void setpE(String pE) {
        this.pE = pE;
    }

    public String getpB() {
        return pB;
    }

    public void setpB(String pB) {
        this.pB = pB;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getFaceVal() {
        return faceVal;
    }

    public void setFaceVal(String faceVal) {
        this.faceVal = faceVal;
    }
}


