package com.primus.dashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.dashboard.model.*;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stock.master.service.StockMasterService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.utils.MathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DashboardService {

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterService stockMasterService ;

    @Autowired
    FundamentalService fundamentalService;

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
        List<FundamentalData> fundamentals = fundamentalService.getAllFundamentals(" where marketGroup is not  null ");
        DashboardData dashboardData = new DashboardData() ;
        setGroupCardData(stocksMasterList,stockTransactionList,dashboardData);
        setCapCardData(stocksMasterList,stockTransactionList,dashboardData);
        setAverages(fundamentals,  dashboardData);
        setSectorDetails(fundamentals,dashboardData);
        ObjectMapper objectMapper =  new ObjectMapper();
        return objectMapper.convertValue(dashboardData,Map.class);

    }

    private Averages getSubAverage(List<FundamentalData> fundamentalDataList)
    {
        Double totalPE= 0.0;
        Double totalPB =0.0 ;
        Double totalROE = 0.0;
        Averages averages = new Averages();
        int totalPERecs = fundamentalDataList.size() ;
        int totalPBRecs = fundamentalDataList.size() ;
        int totalROERecs = fundamentalDataList.size() ;
        for ( FundamentalData fundamentalData : fundamentalDataList)
        {
            if ( fundamentalData.getEps() != null && fundamentalData.getEps() !=0.0  ){
                totalPE += (fundamentalData.getCurPrice() / fundamentalData.getEps() );
            }else {
                totalPERecs --;
            }
            if(fundamentalData.getBookValue() !=null  && fundamentalData.getBookValue() != 0.0){
                totalPB += (fundamentalData.getCurPrice() / fundamentalData.getBookValue() );
            }else{
                totalPBRecs -- ;
            }

            if(fundamentalData.getRoe()!=null  && fundamentalData.getRoe() != 0.0){
                totalROE += fundamentalData.getRoe() ;
            }else{
                totalROERecs -- ;
            }

        }
        Double avPE = totalPE/totalPERecs ;
        Double avPB = totalPB/totalPBRecs ;
        Double avROE = totalROE /totalROERecs;


        averages.setPbRatio(Math.round(avPB* 100.0 )/100.0);
        averages.setPeRatio(Math.round(avPE* 100.0 )/100.0);
        averages.setRoe((Math.round(avROE* 100.0 )/100.0));
        return  averages;

    }

    private Averages getSubMedian(List<FundamentalData> fundamentalDataList)
    {
        Averages averages = getSubAverage(fundamentalDataList);
        List<Double> peValues = new ArrayList<>();
        List<Double> pbValues = new ArrayList<>();
        List<Double> roeValues = new ArrayList<>();

        for ( FundamentalData fundamentalData : fundamentalDataList)
        {
            if ( fundamentalData.getEps() != null && fundamentalData.getEps() !=0.0  ){
                peValues.add(fundamentalData.getCurPrice() / fundamentalData.getEps() );
            }
            if(fundamentalData.getBookValue() !=null  && fundamentalData.getBookValue() != 0.0) {
                pbValues.add(fundamentalData.getCurPrice() / fundamentalData.getBookValue());
            }
            if(fundamentalData.getRoe()!=null  && fundamentalData.getRoe() != 0.0){
                roeValues.add(fundamentalData.getRoe()) ;
            }

        }
        Double avPE = MathUtil.getMedian(peValues);
        Double avPB = MathUtil.getMedian(pbValues);
        Double avROE = MathUtil.getMedian(roeValues);

        averages.setPbRatio(Math.round(avPB* 100.0 )/100.0);
        averages.setPeRatio(Math.round(avPE* 100.0 )/100.0);
        averages.setRoe((Math.round(avROE* 100.0 )/100.0));
        return  averages;

    }


    private void setAverages(List<FundamentalData> fundamentalDataList, DashboardData dashboardData)
    {
        Averages averages = getSubMedian(fundamentalDataList);

       // averages.setRateOfIncrease(Math.round(avgRateofIncr * 100.0)/100.0);
        dashboardData.setFullDataAvg(averages);

        List<FundamentalData> lCFundamentals = fundamentalDataList.stream().filter( fundamentalData -> {
            return fundamentalData.getMarketGroup().equalsIgnoreCase("L");
        }).collect(Collectors.toList());
        Averages lcAverages = getSubMedian(lCFundamentals);
        dashboardData.setlCDataAvg(lcAverages);

        List<FundamentalData> sCFundamentals = fundamentalDataList.stream().filter( fundamentalData -> {
            return fundamentalData.getMarketGroup().equalsIgnoreCase("S");
        }).collect(Collectors.toList());
        Averages scAverages = getSubMedian(sCFundamentals);
        dashboardData.setsCDataAvg(scAverages);

        List<FundamentalData> mCFundamentals = fundamentalDataList.stream().filter( fundamentalData -> {
            return fundamentalData.getMarketGroup().equalsIgnoreCase("M");
        }).collect(Collectors.toList());
        Averages mcAverages = getSubMedian(mCFundamentals);
        dashboardData.setmCDataAvg(mcAverages);




    }

    private void setSectorDetails( List<FundamentalData> fundamentalDataList,DashboardData dashboardData)
    {
        MathContext mathContext = new  MathContext(2, RoundingMode.CEILING);
        Map<String,List<Double>> indusChange = new HashMap<>();
        fundamentalDataList.forEach( fundamentalData -> {
            if ( fundamentalData.getEps() != 0  && StringUtils.isNotEmpty(fundamentalData.getSector())) {
                if (indusChange.containsKey(fundamentalData.getSector())) {

                    System.out.println(fundamentalData.getCompany() + "::" + fundamentalData.getEps());
                    BigDecimal pe = new BigDecimal(fundamentalData.getCurPrice() / fundamentalData.getEps());
                    pe.round(mathContext).floatValue();
                    System.out.println(fundamentalData.getCompany() + "::" + pe.round(mathContext).doubleValue());
                    indusChange.get(fundamentalData.getSector()).add(pe.round(mathContext).doubleValue());
                } else {
                    List ls = new ArrayList();

                    BigDecimal pe = new BigDecimal(fundamentalData.getCurPrice() / fundamentalData.getEps());
                    ls.add(pe.round(mathContext).doubleValue());
                    indusChange.put(fundamentalData.getSector(), ls);
                }
            }
        } );
        List<SectorDetails> sectorDetailsList = new ArrayList<>() ;
        for (Map.Entry<String,List<Double>> entry : indusChange.entrySet()) {
            List<Double> values = entry.getValue();
           BigDecimal median =  new BigDecimal(MathUtil.getMedian(values));
           SectorDetails sectorDetails =  new SectorDetails();
            sectorDetails.setPe(median.round(mathContext).doubleValue());
            sectorDetails.setSector(entry.getKey());
            sectorDetailsList.add(sectorDetails);
        }
        dashboardData.setSectorDetailsList(sectorDetailsList);
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
        groupCardData.setGainers((Integer)groupBResults.get(GAINERS) + (Integer)groupXResults.get(GAINERS) + (Integer)groupAResults.get(GAINERS));

        double totalRateofIncr = (groupCardData.getGroupAGainPerc() * groupCardData.getGroupANo())  +
                (groupCardData.getGroupBGainPerc() * groupCardData.getGroupBNo()) +
                (groupCardData.getGroupXGainPerc() * groupCardData.getGroupXNo());
        double avgRateofIncr= totalRateofIncr/groupCardData.getNoOfSecurities();

        groupCardData.setTotalGainPerc(Math.round(avgRateofIncr* 100.0 )/100.0);
        dashboardData.setGroupCardData(groupCardData);


    }

    private void setCapCardData(List<StocksMaster> stocksMasterList ,  List<StockTransaction> stockTransactionList,DashboardData dashboardData)
    {
        List<StocksMaster> capLStocks = stocksMasterList.stream().filter( sm ->
        { return sm.getMarketGroup().equalsIgnoreCase("L");
        }).collect(Collectors.toList());
        Map<String,Object> capLResults=  getGroupResults(capLStocks,stockTransactionList);
        CapCardData capData = new CapCardData();
        capData.setCapLNo((Integer) capLResults.get(TOTAL));
        capData.setCapLGainer((Integer)capLResults.get(GAINERS));
        capData.setCapLGainPerc((Double)capLResults.get(AVGGAIN));

        List<StocksMaster> capMStocks = stocksMasterList.stream().filter( sm ->
        { return sm.getMarketGroup().equalsIgnoreCase("M");
        }).collect(Collectors.toList());
        Map<String,Object> capMResults=  getGroupResults(capMStocks,stockTransactionList);
        capData.setCapMNo((Integer) capMResults.get(TOTAL));
        capData.setCapMGainer((Integer)capMResults.get(GAINERS));
        capData.setCapMGainPerc((Double)capMResults.get(AVGGAIN));

        List<StocksMaster> capSStocks = stocksMasterList.stream().filter( sm ->
        { return sm.getMarketGroup().equalsIgnoreCase("S");
        }).collect(Collectors.toList());
        Map<String,Object> capSResults=  getGroupResults(capSStocks,stockTransactionList);
        capData.setCapSNo((Integer) capSResults.get(TOTAL));
        capData.setCapSGainer((Integer)capSResults.get(GAINERS));
        capData.setCapSGainPerc((Double)capSResults.get(AVGGAIN));

        capData.setNoOfSecurities((Integer) capSResults.get(TOTAL)  + (Integer) capLResults.get(TOTAL) + (Integer) capMResults.get(TOTAL));
        capData.setGainers((Integer)capSResults.get(GAINERS) + (Integer)capLResults.get(GAINERS) + (Integer)capMResults.get(GAINERS));

        double totalRateofIncr = (capData.getCapLGainPerc() * capData.getCapLNo())  +
                (capData.getCapMGainPerc() * capData.getCapMNo()) +
                (capData.getCapSGainPerc() * capData.getCapSNo());
        double avgRateofIncr= totalRateofIncr/capData.getNoOfSecurities();
        capData.setTotalGainPerc(Math.round(avgRateofIncr* 100.0 )/100.0);

        dashboardData.setCapCardData(capData);


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
