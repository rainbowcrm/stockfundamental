package com.primus.stock.master.service;

import com.primus.stock.api.service.APIService;
import com.primus.stock.master.dao.FundamentalsDAO;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.ReportData;
import com.primus.stock.master.model.StocksMaster;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.*;
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

    public List<FundamentalData> listData( int from , int to , String whereCondition, String orderby )
    {
        return fundamentalsDAO.listData(from,to,whereCondition,orderby);
    }

    public List<FundamentalData> getAllFundamentals (String whereCondition )
    {
        return fundamentalsDAO.getAllFundamentals(whereCondition);
    }

    public void updateMarketCap()
    {
        List<StocksMaster> trackedStocks = stockMasterService.getAllTrackedStocks();
        List<FundamentalData> fundamentalDataList = fundamentalsDAO.getAllFundamentals(null);
        for (StocksMaster stocksMaster : trackedStocks) {
            if( stocksMaster.getMarketCap() != null )
            {
                FundamentalData fundamentalData = fundamentalDataList.stream().filter( fundamentalData1 -> {
                   return (fundamentalData1.getBseCode().equalsIgnoreCase(stocksMaster.getBseCode())?true:false);
                }).findFirst().orElse(null);
                if(fundamentalData != null) {
                    fundamentalData.setMarketCap(stocksMaster.getMarketCap());
                    fundamentalData.setMarketCapFF(stocksMaster.getMarketCapFF());
                    fundamentalData.setMarketGroup(stocksMaster.getMarketGroup());
                    fundamentalsDAO.update(fundamentalData);
                    System.out.println(stocksMaster.getBseCode() + ":::::" + stocksMaster.getMarketCap());
                }
            }
        }
    }

    private List<ReportData> makeReportDataList(List<FundamentalData> fundamentalDataList)
    {
        Map<String,List<FundamentalData>> map = new HashMap<>() ;
        Map<String,List<Double>> mpAverages = new HashMap<>() ;
        for (FundamentalData fundamentalData : fundamentalDataList)
        {
            if (map.containsKey(fundamentalData.getIndustry())) {
                ((List)map.get(fundamentalData.getIndustry())).add(fundamentalData);
            }else
            {
                map.put(fundamentalData.getIndustry(), new ArrayList<>()) ;
                ((List)map.get(fundamentalData.getIndustry())).add(fundamentalData);
            }
        }

        for (Map.Entry<String,List<FundamentalData>> entry : map.entrySet()) {
            List<FundamentalData> industryList = entry.getValue() ;
            Double sumPE = 0.0 ;
            Double sumPB = 0.0 ;
            for (  FundamentalData fdData : industryList) {
                if (fdData.getEps() != 0 ) {
                    Double pe = fdData.getCurPrice() / fdData.getEps();
                    sumPE += pe;
                }
                 if (fdData.getBookValue() != 0) {
                     Double pb = fdData.getCurPrice() / fdData.getBookValue();
                     sumPB += pb;
                 }


            }
            Double avgPE = sumPE / industryList.size() ;
            Double avgPB = sumPB / industryList.size() ;
            mpAverages.put(entry.getKey(), Arrays.asList(avgPE,avgPB)) ;
        }
        List<ReportData> reportDataList  = new ArrayList<>() ;
        for (FundamentalData fundamentalData : fundamentalDataList)
        {
            ReportData reportData  = new ReportData(fundamentalData);
            reportData.setIndustryAvgPE(mpAverages.get(reportData.getIndustry()).get(0));
            reportData.setIndustryAvgPB(mpAverages.get(reportData.getIndustry()).get(1));
            reportData.setIntrinsicValPE(reportData.getIndustryAvgPE() * reportData.getEps());
            reportData.setIntrinsicValPB(reportData.getIndustryAvgPB() * reportData.getBookValue());
            reportDataList.add(reportData);
        }

        return reportDataList ;

    }

    public void exportToXLS() throws Exception
    {
        List<FundamentalData> fundamentalDataList = fundamentalsDAO.getAllFundamentals("");
        HSSFWorkbook workbook = new HSSFWorkbook ();
        HSSFSheet spreadsheet
                = workbook.createSheet(" Stock Fundamentals ");
        HSSFRow rowhead = spreadsheet.createRow((short)0);
        rowhead.createCell(0).setCellValue("Scrip Code");
        rowhead.createCell(1).setCellValue("Company");
        rowhead.createCell(2).setCellValue("Industry");
        rowhead.createCell(3).setCellValue("Sector");
        rowhead.createCell(4).setCellValue("Price");
        rowhead.createCell(5).setCellValue("Intrinsic Value(PE)");
        rowhead.createCell(6).setCellValue("Intrinsic Value(PB)");
        rowhead.createCell(7).setCellValue("EPS");
        rowhead.createCell(8).setCellValue("PE");
        rowhead.createCell(9).setCellValue("Industry avg PE");
        rowhead.createCell(10).setCellValue("Book Value");
        rowhead.createCell(11).setCellValue("PB");
        rowhead.createCell(12).setCellValue("Industry Avg PB");
        AtomicInteger integer = new AtomicInteger(1) ;
        List<ReportData> reportDataList = makeReportDataList(fundamentalDataList);
        for (ReportData reportData : reportDataList) {
            HSSFRow row = spreadsheet.createRow((short)integer.getAndAdd(1));
            row.createCell(0).setCellValue(reportData.getBseCode());
            row.createCell(1).setCellValue(reportData.getCompany());
            row.createCell(2).setCellValue(reportData.getIndustry());
            row.createCell(3).setCellValue(reportData.getSector());
            row.createCell(4).setCellValue(reportData.getCurPrice());
            row.createCell(5).setCellValue(reportData.getIntrinsicValPE());
            row.createCell(6).setCellValue(reportData.getIntrinsicValPB());
            row.createCell(7).setCellValue(reportData.getEps());
            row.createCell(8).setCellValue(reportData.getCurPrice()/ reportData.getEps());
            row.createCell(9).setCellValue(reportData.getIndustryAvgPE());
            row.createCell(10).setCellValue(reportData.getBookValue());
            row.createCell(11).setCellValue(reportData.getCurPrice()/ reportData.getBookValue());
            row.createCell(12).setCellValue(reportData.getIndustryAvgPB());
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
            if( fundamentalData.getCurPrice() >0.0) {
                createFundamentals(fundamentalData);
            }else {
                System.out.println("Skipping =  " + fundamentalData );
            }

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
        if (fundamentalData.getCurPrice() >0.0) {
            createFundamentals(fundamentalData);
        }
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
        if (compNamMap.get("FullN").length() > 99 )
        {
            fundamentalData.setCompany( fdMap.get("SecurityCode") + compNamMap.get("FullN").substring(0,70));

        }else {
            fundamentalData.setCompany(compNamMap.get("FullN"));
        }
        fundamentalData.setIndustry(fdMap.get("Industry"));
        fundamentalData.setSector(fdMap.get("Sector"));
        if(NumberUtils.isNumber(curMap.get("LTP")))
            fundamentalData.setCurPrice(Double.parseDouble(curMap.get("LTP")));
        else
            fundamentalData.setCurPrice(-1.0);
        if(NumberUtils.isNumber(fdMap.get("EPS")))
            fundamentalData.setEps(Double.parseDouble(fdMap.get("EPS")));
        else
            fundamentalData.setEps(0.0);
        if(NumberUtils.isNumber(fdMap.get("PB")) && Double.parseDouble(fdMap.get("PB") ) !=0.0 )
            fundamentalData.setBookValue( fundamentalData.getCurPrice()/ Double.parseDouble(fdMap.get("PB")) );
        else
            fundamentalData.setBookValue(0.0);
        if(NumberUtils.isNumber(fdMap.get("ROE")))
            fundamentalData.setRoe(Double.parseDouble(fdMap.get("ROE")));
        else
            fundamentalData.setRoe(0.0);
        fundamentalData.setFundaLastUpdated( new java.util.Date());
        fundamentalData.setPriceLastUpdated(new java.util.Date());
        return fundamentalData;
    }

    public List<String> getDistinctIndustry()
    {
        return fundamentalsDAO.getDistinctIndustry();
    }

    public List<String> getDistinctSector()
    {
        return fundamentalsDAO.getDistinctSector();
    }
}
