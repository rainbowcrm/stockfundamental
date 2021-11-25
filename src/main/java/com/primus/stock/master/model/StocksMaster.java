package com.primus.stock.master.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="STOCK_MASTER")
public class StocksMaster {

 long id;
 String securityCode;
 String issuerName;
 String securityId;
 String securityName;
 String status;
 String groupC;
 Double faceValue;
 String isinNo;
 String industry;
 String apiCode;
 Boolean isTracked;
 String bseCode;
 Boolean useJavaAPI ;
 Double marketCap;
 Double marketCapFF;
 String marketGroup;


    @Column(name  ="ID")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name  ="SECURITY_CODE")
    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Column(name  ="ISSUER_NAME")
    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    @Column(name  ="SECURITY_ID")
    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    @Column(name  ="SECURITY_NAME")
    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    @Column(name  ="STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name  ="GROUPC")
    public String getGroupC() {
        return groupC;
    }

    public void setGroupC(String groupC) {
        this.groupC = groupC;
    }

    @Column(name  ="FACE_VALUE")
    public Double getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Double faceValue) {
        this.faceValue = faceValue;
    }

    @Column(name  ="ISIN_NO")
    public String getIsinNo() {
        return isinNo;
    }

    public void setIsinNo(String isinNo) {
        this.isinNo = isinNo;
    }

    @Column(name  ="INDUSTRY")
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Column(name  ="API_CODE")
    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    @Column(name  ="IS_TRACKED")
    public Boolean getTracked() {
        return isTracked;
    }

    public void setTracked(Boolean tracked) {
        isTracked = tracked;
    }

    @Column(name  ="BSE_CODE")
    public String getBseCode() {
        return bseCode;
    }

    public void setBseCode(String bseCode) {
        this.bseCode = bseCode;
    }

    @Column(name  ="USE_JAVA_API")
    public Boolean getUseJavaAPI() {
        return useJavaAPI;
    }

    public void setUseJavaAPI(Boolean useJavaAPI) {
        this.useJavaAPI = useJavaAPI;
    }

    @Column(name  ="MARKET_CAPT")
    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    @Column(name  ="MARKET_CAPT_FF")
    public Double getMarketCapFF() {
        return marketCapFF;
    }

    public void setMarketCapFF(Double marketCapFF) {
        this.marketCapFF = marketCapFF;
    }

    @Column(name  ="MARKET_CAP_GROUP")
    public String getMarketGroup() {
        return marketGroup;
    }

    public void setMarketGroup(String marketGroup) {
        this.marketGroup = marketGroup;
    }
}
