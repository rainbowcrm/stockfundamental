package com.primus.stock.master.service;

import com.primus.stock.master.dao.QuarterReportDAO;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.QuarterReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuarterReportService {

    @Autowired
    QuarterReportDAO quarterReportDAO;

    @Autowired
    FinancialService financialService ;

    public void saveQuarterData(long year, int quarter)
    {
        List<FinancialData> financialDataList = financialService.getAllFinancials() ;
        for (FinancialData financialData : financialDataList) {
            QuarterReport qrt = new QuarterReport(financialData,year,quarter);
            quarterReportDAO.create(qrt);
        }
    }


}
