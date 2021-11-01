package com.primus.stock.master.service;

import com.primus.stock.api.service.APIService;
import com.primus.stock.master.dao.FundamentalsDAO;
import com.primus.stock.master.model.FundamentalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FundamentalService {

    @Autowired
    FundamentalsDAO fundamentalsDAO ;

    @Autowired
    APIService apiService ;

    public void createFundamentals(FundamentalData fundamentalData){
        fundamentalsDAO.create(fundamentalData);
    }

    public void updateFundamentals(FundamentalData fundamentalData){
        fundamentalsDAO.update(fundamentalData);
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
        fundamentalData.setEps(Double.parseDouble(fdMap.get("EPS")));
        fundamentalData.setBookValue(Double.parseDouble(fdMap.get("PB")) * fundamentalData.getCurPrice());
        fundamentalData.setRoe(Double.parseDouble(fdMap.get("ROE")));
        return fundamentalData;
    }
}
