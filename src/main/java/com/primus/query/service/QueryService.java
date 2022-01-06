package com.primus.query.service;

import com.primus.query.model.QueryLine;
import com.primus.ui.controller.UIController;
import com.primus.ui.model.StockCompleteData;
import com.primus.ui.service.UIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QueryService {


    @Autowired
    UIService uiService;



    public List<StockCompleteData> applyFilter(List<QueryLine> queryLines)
    {
        List<StockCompleteData> fullStockData = uiService.getFullStocks();
        for (QueryLine queryLine : queryLines) {
            if(queryLine.getProperty().equals(QueryLine.PropertySet.INDS)){
                fullStockData = filterOnIndustry(fullStockData,queryLine.getValue());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.SCT)){
                fullStockData = filterOnSector(fullStockData,queryLine.getValue());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.CPSZ)){
                fullStockData = filterOnCapSize(fullStockData,queryLine.getValue());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.EPS)){
                fullStockData = filterOnEPS(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.PB)){
                fullStockData = filterOnPB(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.PE)){
                fullStockData = filterOnPE(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.BV)){
                fullStockData = filterOnBV(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.ROE)){
                fullStockData = filterOnROE(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.DIV)){
                fullStockData = filterOnDiv(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.RVN)){
                fullStockData = filterOnRev(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }

        }
        return fullStockData;

    }

    private List<StockCompleteData> filterOnIndustry(List<StockCompleteData> presentData , String industry)
    {
        return presentData.stream().filter( current -> { return current.getIndustry().equalsIgnoreCase(industry)?true:false; }).collect(Collectors.toList());
    }
    private List<StockCompleteData> filterOnSector(List<StockCompleteData> presentData , String sector)
    {
        return presentData.stream().filter( current -> { return current.getSector().equalsIgnoreCase(sector)?true:false; }).collect(Collectors.toList());
    }

    private List<StockCompleteData> filterOnCapSize(List<StockCompleteData> presentData , String capSize)
    {
        return presentData.stream().filter( current -> { return current.getGroupCap().equalsIgnoreCase(capSize)?true:false; }).collect(Collectors.toList());
    }

    private List<StockCompleteData> filterOnPrice(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnEPS(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getEps()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getEps()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getEps()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getEps()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getEps()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnBV(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getBookvalue()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getBookvalue()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getBookvalue()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getBookvalue()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getBookvalue()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnPE(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getPe()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getPe()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getPe()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getPe()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getPe()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnPB(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getPb()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getPb()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getPb()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getPb()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getPb()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnROE(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getRoe()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getRoe()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getRoe()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getRoe()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getRoe()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnDiv(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getDivident()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getDivident()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getDivident()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getDivident()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getDivident()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnRev(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getRevenue()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getRevenue()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getRevenue()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getRevenue()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getRevenue()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

}
