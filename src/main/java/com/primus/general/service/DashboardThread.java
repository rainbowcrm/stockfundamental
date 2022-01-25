package com.primus.general.service;

import com.primus.dashboard.service.DashboardService;

public class DashboardThread extends Thread{

    DashboardService dashboardService;
    int days ;

    public DashboardThread(DashboardService dashboardService1 , int days1) {
        this.dashboardService = dashboardService1 ;
        this.days = days1;
    }

    @Override
    public void run() {
        dashboardService.updateDashBoardData(days);
    }
}
