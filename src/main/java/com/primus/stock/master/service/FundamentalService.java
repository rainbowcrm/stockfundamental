package com.primus.stock.master.service;

import com.primus.stock.api.service.APIService;
import com.primus.stock.master.dao.FundamentalsDAO;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void exportToXLS() throws Exception
    {
        List<FundamentalData> fundamentalDataList = fundamentalsDAO.getAllFundamentals();
        HSSFWorkbook workbook = new HSSFWorkbook ();
        HSSFSheet spreadsheet
                = workbook.createSheet(" Stock Fundamentals ");
        HSSFRow rowhead = spreadsheet.createRow((short)0);
        rowhead.createCell(0).setCellValue("Scrip Code");
        rowhead.createCell(1).setCellValue("Company");
        rowhead.createCell(2).setCellValue("Industry");
        rowhead.createCell(3).setCellValue("Sector");
        rowhead.createCell(4).setCellValue("Price");
        rowhead.createCell(5).setCellValue("EPS");
        rowhead.createCell(6).setCellValue("Book Value");

        AtomicInteger integer = new AtomicInteger(1) ;
        for (FundamentalData fundamentalData : fundamentalDataList) {
            HSSFRow row = spreadsheet.createRow((short)integer.getAndAdd(1));
            row.createCell(0).setCellValue(fundamentalData.getBseCode());
            row.createCell(1).setCellValue(fundamentalData.getCompany());
            row.createCell(2).setCellValue(fundamentalData.getIndustry());
            row.createCell(3).setCellValue(fundamentalData.getSector());
            row.createCell(4).setCellValue(fundamentalData.getCurPrice());
            row.createCell(5).setCellValue(fundamentalData.getEps());
            row.createCell(6).setCellValue(fundamentalData.getBookValue());
        }
        FileOutputStream fileOut = new FileOutputStream("results.xls");
        workbook.write(fileOut);
//closing the Stream
        fileOut.close();
//closing the workbook



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
            fundamentalData.setBookValue( fundamentalData.getCurPrice()/ Double.parseDouble(fdMap.get("PB")) );
        else
            fundamentalData.setBookValue(0.0);
        if(NumberUtils.isNumber(fdMap.get("ROE")))
            fundamentalData.setRoe(Double.parseDouble(fdMap.get("ROE")));
        else
            fundamentalData.setRoe(0.0);
        return fundamentalData;
    }
}
