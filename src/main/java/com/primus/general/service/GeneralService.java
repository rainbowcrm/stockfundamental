package com.primus.general.service;

import com.primus.common.LogWriter;
import com.primus.dashboard.service.DashboardService;
import com.primus.stock.master.service.DailyUpdateService;
import com.primus.stock.master.service.FinancialService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stocktransaction.service.StockTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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

    @Autowired
    CacheManager cacheManager;

    public void updateDashBoards()
    {
        cacheManager.getCache("SwingTradeCache").clear();
        cacheManager.getCache("OVCache").clear();
        cacheManager.getCache("UVCache").clear();

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
        System.out.println("Start Time" + new java.util.Date().toString());

        ReadDailyRecord readDailyRecordA = new ReadDailyRecord(dailyUpdateService,stockTransactionService, "A ");
        readDailyRecordA.start();


        ReadDailyRecord readDailyRecordB = new ReadDailyRecord(dailyUpdateService,stockTransactionService, "B ");
        readDailyRecordB.start();

        ReadDailyRecord readDailyRecordC = new ReadDailyRecord(dailyUpdateService,stockTransactionService, "C ");
        readDailyRecordC.start();

        LogWriter.debug("Daily Record Imported");

    }



    public void readWeeklyFundamentals()
    {
        ReadWeeklyRecord readDailyRecordA = new ReadWeeklyRecord(fundamentalService, "A ");
        readDailyRecordA.start();
        /*fundamentalService.saveAllFundamentals("A ");
        fundamentalService.saveAllFundamentals("B ");
        fundamentalService.saveAllFundamentals("X ");*/
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


class ReadWeeklyRecord extends  Thread {


    FundamentalService fundamentalService ;

    String group;

    public ReadWeeklyRecord(FundamentalService fundamentalService,String group){
        this.fundamentalService =  fundamentalService;
        this.group=group ;
    }

    public void run(){
       fundamentalService.saveAllFundamentals(group);

    }

}
class ReadDailyRecord extends  Thread {

    DailyUpdateService dailyUpdateService ;

    StockTransactionService stockTransactionService ;

    String group ;
    public ReadDailyRecord(DailyUpdateService dailyUpdateService , StockTransactionService stockTransactionService, String group) {
        this.dailyUpdateService = dailyUpdateService ;
        this.stockTransactionService = stockTransactionService ;
        this.group = group ;
    }


    public void run(){
        if (dailyUpdateService.getDailyService(group, new java.util.Date()) == null) {
            stockTransactionService.saveDailyTransactions(group);
            dailyUpdateService.updateDailyService(group, new java.util.Date() );
            System.out.println("End Time " + group + new java.util.Date().toString());
        }

    }
}
