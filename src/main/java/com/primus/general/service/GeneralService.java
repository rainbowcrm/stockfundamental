package com.primus.general.service;

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

    public void readDailyTransactionData()
    {

        stockTransactionService.saveDailyTransactions("A ");
        stockTransactionService.saveDailyTransactions("B ");
        stockTransactionService.saveDailyTransactions("X ");
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
