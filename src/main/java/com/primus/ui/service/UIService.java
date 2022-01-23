package com.primus.ui.service;

import com.primus.common.BusinessContext;
import com.primus.common.datastructures.DataPair;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.FinancialService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stock.master.service.StockMasterService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.ui.model.DailyPrice;
import com.primus.ui.model.FullStockProfile;
import com.primus.ui.model.StockCompleteData;
import com.primus.utils.ExportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UIService {

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterService stockMasterService ;

    @Autowired
    FundamentalService fundamentalService ;

    @Autowired
    FinancialService financialService;

    @Autowired
    ExportService exportService ;



    public int getAllStockCount(Map<String,Object> criteriaMap)
    {
        StringBuffer ctSet =  new StringBuffer("");
        if (!CollectionUtils.isEmpty(criteriaMap)) {
            for (Map.Entry<String, Object> entryD : criteriaMap.entrySet()) {
                if (StringUtils.isNotEmpty(entryD.getKey())) {
                    if (ctSet.length() > 1)
                        ctSet.append(" and ");
                    else
                        ctSet.append(" where ");
                    ctSet.append(entryD.getKey() + " =  '" + entryD.getValue() + "'");
                }
            }
        }
        List<FundamentalData> fundamentalDataList = fundamentalService.getAllFundamentals(ctSet.toString());
        return fundamentalDataList.size();
    }

    public Resource exportDataAsFile( Map<String,Object> criteriaMap,String fileType) throws Exception
    {
        List<Map> mapList = applyFilterStocks(0,9000,criteriaMap);
        Map<String,String> titleKeys = new LinkedHashMap<>();
        mapList.get(0).keySet().forEach( key -> {
            titleKeys.put((String)key,(String)key) ;
        });

        return exportService.exportToExcel(mapList,"Fundamentals List",titleKeys);

    }

    public StockCompleteData getStockCompleteData(String bseCode, BusinessContext businessContext)
    {
        FundamentalData fundamentalData = fundamentalService.getFundamentalData(bseCode);
        FinancialData financialData= financialService.getFinancialData(bseCode);
        StocksMaster stocksMaster = stockMasterService.getStocksData(bseCode);
        FullStockProfile stockCompleteData = new FullStockProfile(fundamentalData,financialData,stocksMaster);

        Date toDate = new java.util.Date();
        Date fromDate = new Date(toDate.getTime() - 90l * 24l  * 3600l * 1000l ) ;
        List<StockTransaction> stockTransactionList = stockTransactionDAO.getDataForStock(fromDate,toDate,stocksMaster.getSecurityName());
        List<DailyPrice> dataPair = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        for (StockTransaction stockTransaction :stockTransactionList) {
            String str = sdf.format(stockTransaction.getTransDate());
            dataPair.add(new DailyPrice(str,stockTransaction.getOpenPrice(),stockTransaction.getLowPrice(),stockTransaction.getClosePrice(),
                    stockTransaction.getHighPrice()));

        }
        stockCompleteData.setPrices(dataPair);
        return stockCompleteData;

    }

    public List<StockCompleteData> getFullStocks()
    {
        List<FundamentalData> fundamentalDataList = fundamentalService.getAllFundamentals(null);
        List<FinancialData> financialDataList = financialService.getAllFinancials() ;
        List<StocksMaster> stocksMasterList = stockMasterService.getAllStocks();
        List<StockCompleteData> returnList = new ArrayList<>();
        for (FundamentalData fundamentalData : fundamentalDataList) {
            FinancialData financialDataSel = financialDataList.stream().filter( financialData ->
            {  return financialData.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null) ;
            StocksMaster stocksMasterSel = stocksMasterList.stream().filter(stocksMaster ->
            { return  stocksMaster.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null);
            StockCompleteData stockCompleteData = new StockCompleteData(fundamentalData,financialDataSel,stocksMasterSel);
            returnList.add(stockCompleteData) ;
        }
        return returnList ;

    }

    public List<Map> applyFilterStocks(int from, int to, Map<String,Object> criteriaMap)
    {

        StringBuffer ctSet =  new StringBuffer("");
        if (!CollectionUtils.isEmpty(criteriaMap)) {
            for (Map.Entry<String, Object> entryD : criteriaMap.entrySet()) {
                if (StringUtils.isNotEmpty(entryD.getKey())) {
                    if (ctSet.length() > 1)
                        ctSet.append(" and ");
                    else
                        ctSet.append(" where ");
                    ctSet.append(entryD.getKey() + " =  '" + entryD.getValue() + "'");
                }
            }
        }
        List<FundamentalData> fundamentalDataList = fundamentalService.listData(from,to,ctSet.toString(),"");
        List<FinancialData> financialDataList = financialService.getAllFinancials() ;
        List<StocksMaster> stocksMasterList = stockMasterService.getAllStocks();
        List<Map> returnList = new ArrayList<>();
        for (FundamentalData fundamentalData : fundamentalDataList) {
            FinancialData financialDataSel = financialDataList.stream().filter( financialData ->
            {  return financialData.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null) ;
            StocksMaster stocksMasterSel = stocksMasterList.stream().filter(stocksMaster ->
            { return  stocksMaster.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null);
            StockCompleteData stockCompleteData = new StockCompleteData(fundamentalData,financialDataSel,stocksMasterSel);
            Map stockCompleteMap = stockCompleteData.getMap();
            returnList.add(stockCompleteMap) ;
        }
        return returnList ;

    }


    public List<Map> getAllStocks(int from, int to ) {
        return applyFilterStocks(from,to,null);
    }

    public List<String> getDistinctIndustry()
    {
        return fundamentalService.getDistinctIndustry();
    }

    public List<String> getDistinctSector()
    {
        return fundamentalService.getDistinctSector();
    }
}
