package com.primus.reports.service;

import com.primus.common.BusinessContext;
import com.primus.common.CommonErrorCodes;
import com.primus.common.PrimusError;
import com.primus.reports.data.TransReportData;
import com.primus.stock.master.dao.StockMasterDAO;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterDAO stockMasterDAO ;


    public static void sort(ArrayList<TransReportData> list)
    {

        list.sort((o1, o2)
                -> o1.getSector().compareTo(
                o2.getSector()));
    }

    public void generateReport(String fromDateS, String toDateS,String groupBy,BusinessContext context) throws PrimusError
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date fromDate = simpleDateFormat.parse(fromDateS);
            Date toDate = simpleDateFormat.parse(toDateS);
            List<TransReportData> transReportDataList = generateReport(fromDate, toDate);
            sort(transReportDataList);
            for (TransReportData transReportData : transReportDataList) {
                System.out.println(transReportData);

            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }

    }

    private List<TransReportData> generateReport(Date fromDate, Date toDate) throws PrimusError
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = simpleDateFormat.parse("01-06-2021");
            if (fromDate.before(startDate)) {
                throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
            }
            if (toDate.after(new Date())) {
                throw new PrimusError(CommonErrorCodes.TO_DATE_WRONG, "Start Date cannot be after current day");
            }
            List<StockTransaction> stockTransactionList = stockTransactionDAO.getData(fromDate, toDate);
            List<TransReportData> transReportDataList = new ArrayList<>();
            List<StocksMaster> stocksMasterList = stockMasterDAO.listAllTrackedData();
            for (StocksMaster stocksMaster : stocksMasterList) {
                List<StockTransaction> indTransactionList = stockTransactionList.stream().filter( stockTransaction ->
                {  return stockTransaction.getStocksMaster().getBseCode().equalsIgnoreCase(stocksMaster.getBseCode())?true:false; })
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(indTransactionList)){
                    TransReportData transReportData = new TransReportData(stocksMaster.getSecurityName(),stocksMaster.getIndustry(),
                            stocksMaster.getGroupC(),stocksMaster.getMarketGroup(),stocksMaster.getSector());
                    transReportData.setOpeningPrice(indTransactionList.get(0).getOpenPrice());
                    transReportData.setFinalPricde(indTransactionList.get(indTransactionList.size()-1).getClosePrice());
                    double change = MathUtil.perChange(indTransactionList.get(0).getOpenPrice(),
                            indTransactionList.get(indTransactionList.size()-1).getClosePrice());
                    transReportData.setChange(change);
                    transReportDataList.add(transReportData);
                }

            }
            return transReportDataList;
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }



    }
}
