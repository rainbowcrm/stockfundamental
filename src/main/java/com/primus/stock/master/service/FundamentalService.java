package com.primus.stock.master.service;

import com.primus.stock.api.service.APIService;
import com.primus.stock.master.dao.FundamentalsDAO;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FundamentalService {

    @Autowired
    FundamentalsDAO fundamentalsDAO ;

    @Autowired
    APIService apiService ;

    @Autowired
    StockMasterService stockMasterService ;

    public void createFundamentals(FundamentalData fundamentalData){
        fundamentalsDAO.create(fundamentalData);
    }

    public void updateFundamentals(FundamentalData fundamentalData){
        fundamentalsDAO.update(fundamentalData);
    }

    public void saveAllFundamentals(String groupC) throws Exception
    {
        List<StocksMaster> stocksMasterList =  stockMasterService.getAllTrackedStocks(groupC);
        for (StocksMaster stocksMaster : stocksMasterList) {
            Map<String, Object> comHeaderData = apiService.getCompanyHeader(stocksMaster.getBseCode());
            Map<String, Object> scripHeaderData = apiService.getScripHeaderData(stocksMaster.getBseCode());
            Map<String, Object> fdMAP = new HashMap<>();
            fdMAP.put("CompHeaderData", comHeaderData);
            fdMAP.put("ScripHeaderData", scripHeaderData);
            FundamentalData fundamentalData = createFullFDFromMap(fdMAP);
            System.out.println("id=" + stocksMaster.getId() + ":" + fundamentalData);
            createFundamentals(fundamentalData);
            Thread.sleep(500);
        }


    }


    public FundamentalData saveFundamentals(String scripCode) throws Exception
    {
        Map<String,Object> comHeaderData = apiService.getCompanyHeader(scripCode);
        Map<String,Object> scripHeaderData = apiService.getScripHeaderData(scripCode);
        Map<String,Object> fdMAP = new HashMap<>();
        fdMAP.put("CompHeaderData",comHeaderData) ;
        fdMAP.put("ScripHeaderData",scripHeaderData);
        FundamentalData fundamentalData = createFullFDFromMap(fdMAP);
        createFundamentals(fundamentalData);
        return fundamentalData ;

    }

    public FundamentalData createFullFDFromMap(Map<String,Object> fullData)
    {
        Map<String,String>  fdMap = (Map)fullData.get("CompHeaderData");
        Map<String,Object>  sHMap = (Map)fullData.get("ScripHeaderData");
        Map<String, String> curMap = (Map) sHMap.get("CurrRate");
        Map<String, String> compNamMap =  (Map) sHMap.get("Cmpname");
        FundamentalData fundamentalData = new FundamentalData();
        fundamentalData.setBseCode(fdMap.get("SecurityCode"));
        fundamentalData.setCompany(compNamMap.get("FullN"));
        fundamentalData.setIndustry(fdMap.get("Industry"));
        fundamentalData.setSector(fdMap.get("Sector"));
        fundamentalData.setCurPrice(Double.parseDouble(curMap.get("LTP")));
        if(NumberUtils.isNumber(fdMap.get("EPS")))
            fundamentalData.setEps(Double.parseDouble(fdMap.get("EPS")));
        else
            fundamentalData.setEps(0.0);
        if(NumberUtils.isNumber(fdMap.get("PB")))
            fundamentalData.setBookValue(Double.parseDouble(fdMap.get("PB")) * fundamentalData.getCurPrice());
        else
            fundamentalData.setBookValue(0.0);
        if(NumberUtils.isNumber(fdMap.get("ROE")))
            fundamentalData.setRoe(Double.parseDouble(fdMap.get("ROE")));
        else
            fundamentalData.setRoe(0.0);
        return fundamentalData;
    }
}
