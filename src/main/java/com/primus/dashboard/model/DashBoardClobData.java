package com.primus.dashboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="Dashboard_Data")
public class DashBoardClobData {

    Integer days;

    String dashboardData;

    @Column(name  ="DAYS")
    @Id
    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }


    @Column(name  ="CONTENT")
    public String getDashboardData() {
        return dashboardData;
    }

    public void setDashboardData(String dashboardData) {
        this.dashboardData = dashboardData;
    }
}
