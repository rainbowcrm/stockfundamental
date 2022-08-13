package com.primus.reports.data;

public class TransReportData {
    String security;
    String industry;
    String group;
    String marketCapGroup;
    String sector;

    Double openingPrice;
    Double finalPricde;
    Double change;

    Double relDeviation;
    Double median ;


    public TransReportData() {
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMarketCapGroup() {
        return marketCapGroup;
    }

    public void setMarketCapGroup(String marketCapGroup) {
        this.marketCapGroup = marketCapGroup;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public TransReportData(String security, String industry, String group, String marketCapGroup, String sector) {
        this.security = security;
        this.industry = industry;
        this.group = group;
        this.marketCapGroup = marketCapGroup;
        this.sector = sector;
    }

    public Double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(Double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public Double getFinalPricde() {
        return finalPricde;
    }

    public void setFinalPricde(Double finalPricde) {
        this.finalPricde = finalPricde;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getRelDeviation() {
        return relDeviation;
    }

    public void setRelDeviation(Double relDeviation) {
        this.relDeviation = relDeviation;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(Double median) {
        this.median = median;
    }

    @Override
    public String toString() {
        return "TransReportData{" +
                "security='" + security + '\'' +
                ", industry='" + industry + '\'' +
                ", group='" + group + '\'' +
                ", marketCapGroup='" + marketCapGroup + '\'' +
                ", sector='" + sector + '\'' +
                ", openingPrice=" + openingPrice +
                ", finalPricde=" + finalPricde +
                ", change=" + change +
                ", relDeviation=" + relDeviation +
                ", median=" + median +
                '}';
    }
}
