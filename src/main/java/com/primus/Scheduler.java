package com.primus;

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

    Boolean run = true ;

    @Scheduled(cron = "0 56 20 ? * MON-FRI")
    void someExecution()
    {
        if (!run) {
            run = true;
            System.out.println("Hello " + new java.util.Date());
            generalService.readDailyTransactionData();
           // generalService.readWeeklyFundamentals();
            //stockMasterService.updateMarketCap();
            //fundamentalService.updateMarketCap();
        }

    }

}
