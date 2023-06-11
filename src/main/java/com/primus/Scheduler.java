package com.primus;

import com.primus.common.LogWriter;
import com.primus.general.service.GeneralService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stock.master.service.StockMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class Scheduler {

    @Autowired
    GeneralService generalService;

    @Autowired
    FundamentalService fundamentalService;

    @Autowired
    StockMasterService stockMasterService;

    Boolean run = false ;

    @Scheduled(cron = "0 00 21 ? * MON-FRI")
    void someExecution()
    {

            LogWriter.debug( "Daily Processing for " + new java.util.Date());
            generalService.readDailyTransactionData();
            generalService.updateDashBoards();
           // generalService.readWeeklyFundamentals();
            //stockMasterService.updateMarketCap();
            //fundamentalService.updateMarketCap();

    }

    @Scheduled(cron = "0 30 10 ? * SAT")
    void updateFundamentals()
    {
        LogWriter.debug(" Updating fundamentals for " + new java.util.Date());
        generalService.readWeeklyFundamentals();
        generalService.readWeeklyFinancials();
        generalService.updateDashBoards();

    }
}
