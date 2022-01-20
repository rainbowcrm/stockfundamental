package com.primus.general.service;

import com.primus.dashboard.service.DashboardService;
import com.primus.stock.master.service.DailyUpdateService;
import com.primus.stock.master.service.FinancialService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stocktransaction.service.StockTransactionService;
import org.apache.log4j.LogManager;
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
        dashboardService.updateDashBoardData(15);
        dashboardService.updateDashBoardData(30);
        dashboardService.updateDashBoardData(45);
        dashboardService.updateDashBoardData(60);
        dashboardService.updateDashBoardData(90);
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
        System.out.println("Daily Record Imported");

    }

    public void readWeeklyFundamentals()
    {
        fundamentalService.saveAllFundamentals("A ");
        fundamentalService.saveAllFundamentals("B ");
        fundamentalService.saveAllFundamentals("X ");
        System.out.println("Completed Fundamentals");
    }

    public void readWeeklyFinancials()
    {
      //  financialService.saveAllFinancialData("A ");
      //  financialService.saveAllFinancialData("B ");
        financialService.saveAllFinancialData("X ");
        System.out.println("Completed Financials");
    }


}
