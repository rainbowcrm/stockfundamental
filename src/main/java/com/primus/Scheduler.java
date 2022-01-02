package com.primus;

import com.primus.general.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class Scheduler {

    @Autowired
    GeneralService generalService;

    Boolean run = true;

    @Scheduled(cron = "*/60 * * * * *")
    void someExecution()
    {
        if (!run) {
            run = true;
            System.out.println("Hello " + new java.util.Date());
          //  generalService.readDailyTransactionData();
        }

    }

}
