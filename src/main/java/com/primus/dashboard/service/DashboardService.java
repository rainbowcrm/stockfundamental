package com.primus.dashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.dashboard.model.DashboardData;
import com.primus.dashboard.model.GroupCardData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.StockMasterService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class DashboardService {

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterService stockMasterService ;

    private static final String TOTAL = "Total";
    private static final String GAINERS = "Gainers";
    private static final String AVGGAIN = "AvgGain";


    public Map<String,Object> getDashboardData(long prevDays)
    {
        long oneDayinMillis =  24l * 3600l * 1000l ;
        Date curDate = new java.util.Date();
        Date startDay = new java.util.Date( curDate.getTime() - (oneDayinMillis * prevDays));
        List<StockTransaction> stockTransactionList = stockTransactionDAO.getData(startDay,curDate);
        List<StocksMaster> stocksMasterList = stockMasterService.getAllTrackedStocks();
        DashboardData dashboardData = new DashboardData() ;
        setGroupCardData(stocksMasterList,stockTransactionList,dashboardData);
        ObjectMapper objectMapper =  new ObjectMapper();
        return objectMapper.convertValue(dashboardData,Map.class);

    }

    private void setGroupCardData(List<StocksMaster> stocksMasterList ,  List<StockTransaction> stockTransactionList,DashboardData dashboardData)
    {
        List<StocksMaster> groupAStocks = stocksMasterList.stream().filter( sm ->
        { return sm.getGroupC().equalsIgnoreCase("A ");
        }).collect(Collectors.toList());
        Map<String,Object> groupAResults=  getGroupResults(groupAStocks,stockTransactionList);
        GroupCardData groupCardData = new GroupCardData();
        groupCardData.setGroupANo((Integer) groupAResults.get(TOTAL));
        groupCardData.setGroupAGainer((Integer)groupAResults.get(GAINERS));
        groupCardData.setGroupAGainPerc((Double)groupAResults.get(AVGGAIN));

        List<StocksMaster> groupBStocks = stocksMasterList.stream().filter( sm ->
        { return sm.getGroupC().equalsIgnoreCase("B ");
        }).collect(Collectors.toList());
        Map<String,Object> groupBResults=  getGroupResults(groupBStocks,stockTransactionList);
        groupCardData.setGroupBNo((Integer) groupBResults.get(TOTAL));
        groupCardData.setGroupBGainer((Integer)groupBResults.get(GAINERS));
        groupCardData.setGroupBGainPerc((Double)groupBResults.get(AVGGAIN));

        List<StocksMaster> groupXStocks = stocksMasterList.stream().filter( sm ->
        { return sm.getGroupC().equalsIgnoreCase("X ");
        }).collect(Collectors.toList());
        Map<String,Object> groupXResults=  getGroupResults(groupXStocks,stockTransactionList);
        groupCardData.setGroupXNo((Integer) groupXResults.get(TOTAL));
        groupCardData.setGroupXGainer((Integer)groupXResults.get(GAINERS));
        groupCardData.setGroupXGainPerc((Double)groupXResults.get(AVGGAIN));

        groupCardData.setNoOfSecurities((Integer) groupBResults.get(TOTAL)  + (Integer) groupAResults.get(TOTAL) + (Integer) groupXResults.get(TOTAL));
        groupCardData.setGainers((Integer)groupBResults.get(GAINERS) + (Integer)groupXResults.get(GAINERS) + (Integer)groupXResults.get(GAINERS));
        dashboardData.setGroupCardData(groupCardData);


    }

    private Map<String,Object> getGroupResults(List<StocksMaster> groupStocks,  List<StockTransaction> stockTransactionList)
    {
        int groupTotalCount = groupStocks.size();
        Integer groupPositives = 0;
        Double groupAggrePerc = 0.0 ;
        for (StocksMaster groupStock : groupStocks) {
            List<StockTransaction> indStockRecords=  stockTransactionList.stream().filter( stockTransaction -> {
                return stockTransaction.getApi_code().equalsIgnoreCase(groupStock.getApiCode());
            } ).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(indStockRecords)) {
                    Double openPrice = indStockRecords.get(0).getOpenPrice();
                    Double closePrice = indStockRecords.get(indStockRecords.size() - 1).getClosePrice();
                    Double percChange = ((closePrice - openPrice) / openPrice) * 100;
                    if (percChange > 0.0) {
                        groupPositives += 1;
                    }
                    groupAggrePerc += percChange;
                }else
                {
                    System.out.println("remove "+ groupStock.getBseCode() + "  : " + groupStock.getGroupC());
                    groupTotalCount --;
                }
        }
        Map<String,Object> groupResults =  new HashMap<>();
        groupResults.put(TOTAL,groupTotalCount);
        groupResults.put(GAINERS,groupPositives);
        Double gainPercent = (groupAggrePerc/(double)groupTotalCount);
        Double gainPrec = Math.round(gainPercent * 100)/ 100.0;
        groupResults.put(AVGGAIN,gainPrec);
        return groupResults ;

    }
}
