package com.primus.stock.master.service;

import com.primus.stock.master.dao.QuarterReportDAO;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.QuarterReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuarterReportService {

    @Autowired
    QuarterReportDAO quarterReportDAO;

    @Autowired
    FinancialService financialService ;

    public void saveQuarterData(long year, int quarter)
    {
        List<QuarterReport> reports =  quarterReportDAO.getQuarterResult(year,quarter-1);
        List<FinancialData> financialDataList = financialService.getAllFinancials() ;
        for (FinancialData financialData : financialDataList) {
            QuarterReport qrt = new QuarterReport(financialData,year,quarter);
            QuarterReport previousQuarterReport = reports.stream().filter( quarterReport ->  {
                return quarterReport.getBseCode().equalsIgnoreCase(financialData.getBseCode() )  &&
                        quarterReport.getYear().equals(year) && quarterReport.getQuarter().equals(quarter-1)?true : false;

            }).findFirst().orElse(null);
            if(previousQuarterReport != null && !previousQuarterReport.equals(qrt)) {
                quarterReportDAO.create(qrt);
            }
        }
    }

    public void appendQuarterData(long year, int quarter)
    {
        List<QuarterReport> reports =  quarterReportDAO.getQuarterResult(year,quarter);
        List<FinancialData> financialDataList = financialService.getAllFinancials() ;
        for (FinancialData financialData : financialDataList) {
            QuarterReport currentQuarterReport = reports.stream().filter( quarterReport ->  {
                return quarterReport.getBseCode().equalsIgnoreCase(financialData.getBseCode() )  &&
                        quarterReport.getYear().equals(year) && quarterReport.getQuarter().equals(quarter)?true : false;

            }).findFirst().orElse(null);
            if ( currentQuarterReport == null) {
                QuarterReport qrt = new QuarterReport(financialData, year, quarter);
                quarterReportDAO.create(qrt);
            }
        }
    }
    

    public List<QuarterReport> getQuarterReport(long year, int quarter)
    {
        return  quarterReportDAO.getQuarterResult(year,quarter);
    }


}
