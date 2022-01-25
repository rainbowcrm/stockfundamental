package com.primus.general.service;

import com.primus.common.LogWriter;
import com.primus.dashboard.service.DashboardService;
import com.primus.stock.master.service.DailyUpdateService;
import com.primus.stock.master.service.FinancialService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stocktransaction.service.StockTransactionService;
import org.apache.log4j.LogManager;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralService {

    @Autowired
    FundamentalService fundamentalService ;

    @Autowired
    FinancialService financialService ;

    @Autowired
    StockTransactionService stockTransactionService;

    @Autowired
    DailyUpdateService dailyUpdateService ;

    @Autowired
    DashboardService dashboardService ;

    public void updateDashBoards()
    {
        DashboardThread dashboardThread = new DashboardThread(dashboardService,30);
        dashboardThread.start();

        DashboardThread dashboardThread1 = new DashboardThread(dashboardService,60);
        dashboardThread1.start();

        DashboardThread dashboardThread2 = new DashboardThread(dashboardService,45);
        dashboardThread2.start();

        DashboardThread dashboardThread3 = new DashboardThread(dashboardService,90);
        dashboardThread3.start();

        DashboardThread dashboardThread4 = new DashboardThread(dashboardService,15);
        dashboardThread4.start();

        LogWriter.debug("Dashboards updated");
    }
    public void readDailyTransactionData()
    {

        if (dailyUpdateService.getDailyService("A ", new java.util.Date()) == null) {
            stockTransactionService.saveDailyTransactions("A ");
            dailyUpdateService.updateDailyService("A ", new java.util.Date() );
        }
        if (dailyUpdateService.getDailyService("B ", new java.util.Date()) == null) {
            stockTransactionService.saveDailyTransactions("B ");
            dailyUpdateService.updateDailyService("B ", new java.util.Date() );
        }
        if (dailyUpdateService.getDailyService("X ", new java.util.Date()) == null) {
            stockTransactionService.saveDailyTransactions("X ");
            dailyUpdateService.updateDailyService("X ", new java.util.Date() );
        }
        LogWriter.debug("Daily Record Imported");

    }

    public void readWeeklyFundamentals()
    {
        fundamentalService.saveAllFundamentals("A ");
        fundamentalService.saveAllFundamentals("B ");
        fundamentalService.saveAllFundamentals("X ");
        fundamentalService.updateMarketCap();
        LogWriter.debug("Completed Fundamentals");
    }

    public void readWeeklyFinancials()
    {
        financialService.saveAllFinancialData("A ");
        financialService.saveAllFinancialData("B ");
        financialService.saveAllFinancialData("X ");
        LogWriter.debug("Completed Financials");
    }


}
