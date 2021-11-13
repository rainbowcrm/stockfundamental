package com.primus.stock.master.model;

public class ReportData  extends FundamentalData{

    Double industryAvgPE ;
    Double industryAvgPB ;
    Double intrinsicValPE;
    Double intrinsicValPB;

    public ReportData(FundamentalData fundamentalData)
    {
        this.bseCode = fundamentalData.bseCode;
        this.bookValue= fundamentalData.bookValue;
        this.company = fundamentalData.company ;
        this.curPrice = fundamentalData.curPrice ;
        this.eps = fundamentalData.eps;
        this.industry = fundamentalData.industry ;
        this.netIncome = fundamentalData.netIncome ;
        this.qtrRevenue =fundamentalData.qtrRevenue ;
        this.roe= fundamentalData.roe ;
        this.sector = fundamentalData.sector;

    }

    public Double getIndustryAvgPE() {
        return industryAvgPE;
    }

    public void setIndustryAvgPE(Double industryAvgPE) {
        this.industryAvgPE = industryAvgPE;
    }

    public Double getIndustryAvgPB() {
        return industryAvgPB;
    }

    public void setIndustryAvgPB(Double industryAvgPB) {
        this.industryAvgPB = industryAvgPB;
    }

    public Double getIntrinsicValPE() {
        return intrinsicValPE;
    }

    public void setIntrinsicValPE(Double intrinsicValPE) {
        this.intrinsicValPE = intrinsicValPE;
    }

    public Double getIntrinsicValPB() {
        return intrinsicValPB;
    }

    public void setIntrinsicValPB(Double intrinsicValPB) {
        this.intrinsicValPB = intrinsicValPB;
    }
}
