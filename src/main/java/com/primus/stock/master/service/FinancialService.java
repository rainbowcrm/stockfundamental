package com.primus.stock.master.service;

import com.primus.common.LogWriter;
import com.primus.stock.api.service.APIService;
import com.primus.stock.master.dao.FinancialsDAO;
import com.primus.stock.master.model.DividentHistory;
import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FinancialService {

    @Autowired
    APIService apiService ;

    @Autowired
    StockMasterService stockMasterService ;

    @Autowired
    FinancialsDAO financialsDAO ;

    @Autowired
    DividentHistoryService dividentHistoryService;


    public void createFinancials(FinancialData financialData){
        financialsDAO.create(financialData);
    }

    private String formXML(String input)
    {
        System.out.println(input);
        if (StringUtils.isNotEmpty(input)) {
            String fPass = input.substring(0, input.indexOf("<a"));
            String sPass = fPass.replace("colspan=7", " ");
            String finalVa = "<root>" + sPass + "</td></tr> </tbody></root>";
            return finalVa;
        }else
        {
            return "";
        }
    }

    private String getValue(Node node,int row)
    {
        Node revenueNode =  node.getChildNodes().item(row);
        Node firstColNode =  revenueNode.getChildNodes().item(1);
        String value = firstColNode.getChildNodes().item(0).getNodeValue();
        return value ;
    }

    public  void saveAllFinancialData(String groupC)
    {
        List<StocksMaster> stocksMasterList = stockMasterService.getAllTrackedStocks(groupC) ;
        for (StocksMaster stocksMaster : stocksMasterList) {
            FinancialData financialData = saveFinancialData(stocksMaster.getBseCode(), stocksMaster.getId()) ;
            if (financialData != null) {
                financialData.setCompany(stocksMaster.getSecurityName());
                financialData.setIndustry(stocksMaster.getIndustry());
                createFinancials(financialData);
            }

        }

    }

    private Double getDivident(String scripCode,Date lastYearDate)
    {
        try {

            Map<String, Object> responseData = apiService.getDividentData(scripCode);
            Object finContent = responseData.get("Table");
            double yearDiv = 0.0;
            if (finContent != null && finContent instanceof List) {
                List<Map<String, Object>> divDataList = (List) finContent;
                for (Map<String, Object> map : divDataList) {
                    String divDateString = (String) map.get("BCRD_from");
                    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    Date divDate = dateFormat.parse(divDateString);
                    Double divAmount = (Double) map.get("Amount");
                    if (divDate.after(lastYearDate)) {
                        yearDiv += divAmount != null ? divAmount.doubleValue() : 0.0;
                    }
                    DividentHistory dividentHistory = new DividentHistory();
                    dividentHistory.setBseCode(scripCode);
                    dividentHistory.setExDate(divDate);
                    dividentHistory.setDivident(divAmount);
                    dividentHistoryService.createDividentHistory(dividentHistory);

                }
            }
            return yearDiv;
        }catch (Exception ex) {

            LogWriter.logException("Ex in FinancialService" ,this.getClass(),ex);
        }
        return 0.0;

    }

    public  FinancialData saveFinancialData(String scripCode,Long id)
    {
        try {
            Thread.sleep(100);
            Map<String,Object> responseData = apiService.getFinancialData(scripCode);
            Object finContent = responseData.get("QtlyinCr");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String xml = formXML((String) finContent);
            if (StringUtils.isNotEmpty(xml)) {
                Document doc = db.parse(new InputSource(new java.io.StringReader(xml)));
                NodeList nodeList = doc.getElementsByTagName("tbody");
                Node node = nodeList.item(0);
                //System.out.println(nodeValue);
                String revenue = getValue(node, 1);
                String expenditure = getValue(node, 4);
                String netProfit = getValue(node, 10);
                String equity = getValue(node, 11);
                long iyearDuration = 367l * 24l * 3600l * 1000l;
                Date lastYearDate= new Date (new Date().getTime() - iyearDuration );
                Double divident = getDivident(scripCode, lastYearDate);
                FinancialData financialData = new FinancialData();
                financialData.setBseCode(scripCode);
                if (NumberUtils.isNumber(equity))
                    financialData.setEquit(Double.parseDouble(equity));
                if (NumberUtils.isNumber(revenue.replace(",", "")))
                    financialData.setRevenue(Double.parseDouble(revenue.replace(",", "")));
                if (NumberUtils.isNumber(expenditure.replace(",", "")))
                    financialData.setExpenditure(Double.parseDouble(expenditure.replace(",", "")));
                if (NumberUtils.isNumber(netProfit.replace(",", "")))
                    financialData.setNetProfit(Double.parseDouble(netProfit.replace(",", "")));
                financialData.setDivident(divident);
                LogWriter.debug(id + ":values= " + revenue + " " + expenditure + " " + netProfit + " " + equity + " " + divident);
                return  financialData ;
            }else
                LogWriter.debug("Cannot get Financials for " + scripCode + " =" + id);

                return null;



        }catch (Exception ex)
        {
            LogWriter.logException("Ex in FinancialService" ,this.getClass(),ex);
        }
        return  null;

    }

    @Cacheable( value = "financialsCache")
    public List<FinancialData> getAllFinancials (  )
    {
        return financialsDAO.getAllFinancials();
    }

    public FinancialData getFinancialData ( String bseCode )
    {
        return financialsDAO.getFinancialData(bseCode);
    }
}
