package com.primus.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="USER_PREFERENCES")
public class UserPreferences {

    String emailId;
    String landingPage;
    Integer dashboardDays;
    String validationAlgo;
    Integer techDays ;

    @Column(name  ="EMAIL")
    @Id
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Column(name  ="LANDING_PAGE")
    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    @Column(name  ="DASH_DAYS")
    public Integer getDashboardDays() {
        return dashboardDays;
    }

    public void setDashboardDays(Integer dashboardDays) {
        this.dashboardDays = dashboardDays;
    }

    @Column(name  ="VAL_ALGO")
    public String getValidationAlgo() {
        return validationAlgo;
    }

    public void setValidationAlgo(String validationAlgo) {
        this.validationAlgo = validationAlgo;
    }

    @Column(name  ="TECH_DAYS")
    public Integer getTechDays() {
        return techDays;
    }

    public void setTechDays(Integer techDays) {
        this.techDays = techDays;
    }
}
