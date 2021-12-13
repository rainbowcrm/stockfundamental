package com.primus.dashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.dashboard.dao.DashboardDAO;
import com.primus.dashboard.model.*;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stock.master.service.StockMasterService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.ui.model.StockCompleteData;
import com.primus.utils.GeneralUtils;
import com.primus.utils.MathUtil;
import com.primus.valuation.data.StockValuationData;
import com.primus.valuation.service.ValuationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DashboardService {

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterService stockMasterService;

    @Autowired
    FundamentalService fundamentalService;

    @Autowired
    DashboardDAO dashboardDAO ;

    @Autowired
    ValuationService valuationService;

    private static final String TOTAL = "Total";
    private static final String GAINERS = "Gainers";
    private static final String AVGGAIN = "AvgGain";


    public Map<String, Object> getPersistedDashboardData(long prevDays)
    {
        DashBoardClobData dashBoardClobData =  dashboardDAO.getDashboardData(prevDays);
        DashboardData dashboardData ;
        ObjectMapper objectMapper =  new ObjectMapper();
        try {
            List<StockValuationData>  uvShares = valuationService.getUShares();
            for (StockCompleteData stockCompleteData :uvShares) {
                System.out.println(stockCompleteData);
            }
            if (dashBoardClobData == null) {
                dashboardData = getDashboardData(prevDays);
                dashBoardClobData = new DashBoardClobData();
                dashBoardClobData.setDays((int) prevDays);
                dashBoardClobData.setDashboardData(objectMapper.writeValueAsString(dashboardData));
                dashboardDAO.update(dashBoardClobData);
                return objectMapper.convertValue(dashboardData,Map.class) ;

            } else {
                String dashboardJson = dashBoardClobData.getDashboardData();
                return objectMapper.readValue(dashboardJson, Map.class);
            }
        }catch (Exception ex) {
            ex.printStackTrace();

        }
            return new HashMap<>();



    }

    private DashboardData getDashboardData(long prevDays)
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
        setSectorTrend(stocksMasterList,stockTransactionList,dashboardData);
        setHotStocks(stockTransactionList,stocksMasterList,dashboardData);
        return dashboardData ;

    }

    private void setHotStocks(List<StockTransaction> stockTransactionList,List<StocksMaster> stocksMasterList,DashboardData dashboardData)
    {
        Map<String,Double> hotStocks = new HashMap<>();
        for(StocksMaster stocksMaster : stocksMasterList) {
            String bseCode = stocksMaster.getBseCode();
            List<StockTransaction> indTransaction = stockTransactionList.stream().filter( stockTransaction ->  {
                return stockTransaction.getStocksMaster().getSecurityCode().equalsIgnoreCase(bseCode) ; } ).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(indTransaction)) {
                StockTransaction firstTrans = indTransaction.get(0);
                StockTransaction endTrans = indTransaction.get(indTransaction.size() - 1);
                Double openPrice = firstTrans.getOpenPrice();
                Double closePrice = firstTrans.getClosePrice() ;
                Double percChange =MathUtil.round (((closePrice-openPrice)/openPrice ) * 100);
                hotStocks.put(stocksMaster.getSecurityName(),percChange);
            }
        }

        Map<String,Double> reverseSortedMap = new LinkedHashMap<>();
        hotStocks.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        Map<String,Double> priorSortedMap = new LinkedHashMap<>();
        int j =0 ;
        for (Map.Entry<String,Double> entry: reverseSortedMap.entrySet()) {
            if ( j < 5 )
                priorSortedMap.put(entry.getKey(),entry.getValue());
            else
                break;
            j++;
        }
        dashboardData.setHikeStocks(priorSortedMap);
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
        MathContext mathContext = new  MathContext(3, RoundingMode.CEILING);
        Map<String,List<Double>> indusChange = new HashMap<>();
        Map<String,List<Double>> indusPBs = new HashMap<>();
        Map<String,List<Double>> indusROEs = new HashMap<>();
        fundamentalDataList.forEach( fundamentalData -> {
            if ( fundamentalData.getEps() != 0  && StringUtils.isNotEmpty(fundamentalData.getSector())) {
                if (indusChange.containsKey(fundamentalData.getSector())) {
                    BigDecimal pe = new BigDecimal(fundamentalData.getCurPrice() / fundamentalData.getEps());
                    pe.round(mathContext).floatValue();
                    indusChange.get(fundamentalData.getSector()).add(pe.round(mathContext).doubleValue());
                } else {
                    List ls = new ArrayList();
                    BigDecimal pe = new BigDecimal(fundamentalData.getCurPrice() / fundamentalData.getEps());
                    ls.add(pe.round(mathContext).doubleValue());
                    indusChange.put(fundamentalData.getSector(), ls);
                }
            }
            if ( fundamentalData.getBookValue() != 0  && StringUtils.isNotEmpty(fundamentalData.getSector())) {
                if (indusPBs.containsKey(fundamentalData.getSector())) {
                    BigDecimal pb = new BigDecimal(fundamentalData.getCurPrice() / fundamentalData.getBookValue());
                    pb.round(mathContext).floatValue();
                    indusPBs.get(fundamentalData.getSector()).add(pb.round(mathContext).doubleValue());
                } else {
                    List ls = new ArrayList();
                    BigDecimal pb = new BigDecimal(fundamentalData.getCurPrice() / fundamentalData.getBookValue());
                    ls.add(pb.round(mathContext).doubleValue());
                    indusPBs.put(fundamentalData.getSector(), ls);
                }
            }
            if ( StringUtils.isNotEmpty(fundamentalData.getSector())) {
                if (indusROEs.containsKey(fundamentalData.getSector())) {
                    BigDecimal roe = new BigDecimal(fundamentalData.getRoe());
                    roe.round(mathContext).floatValue();
                    indusROEs.get(fundamentalData.getSector()).add(roe.round(mathContext).doubleValue());
                } else {
                    List ls = new ArrayList();
                    BigDecimal roe = new BigDecimal(fundamentalData.getRoe());
                    ls.add(roe.round(mathContext).doubleValue());
                    indusROEs.put(fundamentalData.getSector(), ls);
                }
            }

        } );
        List<SectorDetails> sectorDetailsList = new ArrayList<>() ;
        for (Map.Entry<String,List<Double>> entry : indusChange.entrySet()) {
            String sec= entry.getKey();
            /*if (sec.startsWith("FMCG") || sec.startsWith("Telecom") || sec.startsWith("Finan") || sec.startsWith("Energy") || sec.startsWith("Healt") ||
                    sec.startsWith("Info") ) {*/
                List<Double> values = entry.getValue();
                BigDecimal medianPE = new BigDecimal(MathUtil.getMedian(values));

                SectorDetails sectorDetails = new SectorDetails();
                sectorDetails.setPe(medianPE.round(mathContext).doubleValue());

                List<Double> valuesPB = indusPBs.get(entry.getKey());
                BigDecimal medianPB = new BigDecimal(MathUtil.getMedian(valuesPB));

                List<Double> valuesROE = indusROEs.get(entry.getKey());
                BigDecimal medianROE = new BigDecimal(MathUtil.getMedian(valuesROE));

                sectorDetails.setPb(medianPB.round(mathContext).doubleValue());
                sectorDetails.setRoe(medianROE.round(mathContext).doubleValue());
                if (entry.getKey().startsWith("FMCG"))
                    sectorDetails.setSector("FMCG");
                else if (entry.getKey().startsWith("CDGS"))
                    sectorDetails.setSector("CDGS");
                else
                    sectorDetails.setSector(entry.getKey());
                sectorDetailsList.add(sectorDetails);
            //}
        }
        dashboardData.setSectorDetailsList(sectorDetailsList);
    }


    private void setSectorTrend(List<StocksMaster> stockMasterList ,  List<StockTransaction> stockTransactionList,DashboardData dashboardData)
    {
        MathContext mathContext = new MathContext(3,RoundingMode.CEILING);
        Set<String> sectors = new HashSet<>();
        stockMasterList.forEach( stocksMaster ->  {
            if (StringUtils.isNotEmpty(stocksMaster.getSector() ))
                sectors.add(stocksMaster.getSector());
        });
        Map<String,List<Double>> sectorValues = new HashMap<>();
        Map<String,List< Double>> sectorPrices= new HashMap<>();
        List<String> sectorList = new ArrayList<>();
        List<String> datesList = new ArrayList<>() ;
        SectorPriceHolder sectorPriceHolder = new SectorPriceHolder();
        boolean datePopulated=false;

        for (String sector : sectors) {
            sectorList.add(sector);
            List<StocksMaster> sectorMasters = stockMasterList.stream().filter( stocksMaster ->  {
                return stocksMaster.getSector().equalsIgnoreCase(sector);
            }).collect(Collectors.toList());

            Map<StocksMaster,List<Double>> stockriseList = new HashMap<>();
            for (StocksMaster sectMaster : sectorMasters) {
                List<StockTransaction> transForSector = stockTransactionList.stream().filter(stockTransaction -> {
                    return stockTransaction.getStocksMaster().getSecurityCode().equalsIgnoreCase(sectMaster.getSecurityCode());
                }).collect(Collectors.toList());
                if (transForSector.size() < 8)
                    continue;
                int size = transForSector.size();
                int index = size/8;
                Double openPrice = transForSector.get(0).getOpenPrice();
                List<Double> stockIncrs = new ArrayList<>();
                stockIncrs.add(0.0d);

                for (int i= 0 ;i <size-3 ; i += index)
                {
                  //  List<StockTransaction> segmentList = transForSector.subList(i,(i+index)>=size?size-1:i+index);
                    Double currPrice=transForSector.get(i).getClosePrice() ;
                    if (!datePopulated) {
                         Date endDate =transForSector.get(i).getTransDate();
                        datesList.add(GeneralUtils.getDayofMonth(endDate) + "-" + GeneralUtils.getMonth(endDate));

                    }BigDecimal perChange= new BigDecimal(((currPrice-openPrice)/openPrice) * 100).round(mathContext);
                    stockIncrs.add(perChange.doubleValue());
                }

                Double currPrice=transForSector.get(size-1).getClosePrice() ;
                BigDecimal perChange= new BigDecimal(((currPrice-openPrice)/openPrice) * 100).round(mathContext);
                stockIncrs.add(perChange.doubleValue());

                stockriseList.put(sectMaster,stockIncrs);
                datePopulated = true ;
            }

            sectorValues.put(sector,new ArrayList<>());
            for (int i = 0 ; i < 9 ;i ++) {
                List<Double> allIndexedVals = new ArrayList<>();
                for (Map.Entry<StocksMaster,List<Double>> entry :stockriseList.entrySet() ){
                    if(entry.getValue().size() > i)
                        allIndexedVals.add(entry.getValue().get(i));
                    else
                        allIndexedVals.add(0d);
                }
                sectorValues.get(sector).add( new BigDecimal(MathUtil.getMedian(allIndexedVals)).round(mathContext).doubleValue());
            }

        }
            for (String sector : sectors)
            {
                    List<Double> incrValues= sectorValues.get(sector);
                    for( int i =0; i < 8; i++) {
                        List<Double> sets = sectorPrices.get(datesList.get(i));
                        if (sets == null)
                            sets = new ArrayList<>();
                        sets.add(incrValues.get(i));
                        sectorPrices.put(datesList.get(i),sets);
                    }

            }
        sectorPriceHolder.setDateValues(sectorPrices);
        sectorPriceHolder.setSectors(sectorList);
        dashboardData.setSectorPriceHolder(sectorPriceHolder);
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
        List<Double> gainPerList = new ArrayList<>();
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
                    gainPerList.add(percChange) ;
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
       // Double gainPrec = Math.round(gainPercent * 100)/ 100.0;

        Double medGainPer = MathUtil.getMedian(gainPerList);
        Double gainPrec = Math.round(medGainPer * 100)/ 100.0;

        groupResults.put(AVGGAIN,gainPrec);
        return groupResults ;

    }


}
