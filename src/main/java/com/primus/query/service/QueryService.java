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


    private void addSubData(List<StockCompleteData> fullData, List<StockCompleteData> subData)
    {
        if (fullData != null && subData != null ) {
            for (StockCompleteData stockCompleteData : subData) {
                if (!fullData.contains(subData)){
                    fullData.add(stockCompleteData);
                }
            }
        }

    }

    public List<StockCompleteData> applyFilter(List<QueryLine> queryLines)
    {
        List<StockCompleteData> fullStockData = uiService.getFullStocks();
        List<StockCompleteData> backUpStockData = new ArrayList<>(fullStockData);
        for (QueryLine queryLine : queryLines) {
            if(queryLine.getProperty().equals(QueryLine.PropertySet.INDS)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnIndustry(backUpStockData,queryLine.getValue());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnIndustry(fullStockData,queryLine.getValue());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.SCT)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnSector(backUpStockData,queryLine.getValue());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnSector(fullStockData,queryLine.getValue());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.CPSZ)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnCapSize(backUpStockData,queryLine.getValue());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnCapSize(fullStockData,queryLine.getValue());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.CRPRC)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnPrice(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnPrice(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.EPS)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnEPS(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnEPS(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.PB)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnPB(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnPB(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.PE)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnPE(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnPE(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.BV)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnBV(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnBV(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.ROE)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnROE(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnROE(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.DIV)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnDiv(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnDiv(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }
            else if(queryLine.getProperty().equals(QueryLine.PropertySet.RVN)){
                if ("OR".equalsIgnoreCase(queryLine.getCondition())) {
                    List<StockCompleteData> subData = filterOnRev(backUpStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
                    addSubData(fullStockData,subData) ;
                }else
                    fullStockData = filterOnRev(fullStockData,Double.parseDouble(queryLine.getValue()),queryLine.getOperator());
            }

        }
        return fullStockData;

    }

    private List<StockCompleteData> filterOnIndustry(List<StockCompleteData> presentData , String industry)
    {
        return presentData.stream().filter( current -> { return current.getIndustry() !=null && current.getIndustry().equalsIgnoreCase(industry)?true:false; }).collect(Collectors.toList());
    }
    private List<StockCompleteData> filterOnSector(List<StockCompleteData> presentData , String sector)
    {
        return presentData.stream().filter( current -> { return current.getSector() != null && current.getSector().equalsIgnoreCase(sector)?true:false; }).collect(Collectors.toList());
    }

    private List<StockCompleteData> filterOnCapSize(List<StockCompleteData> presentData , String capSize)
    {
        return presentData.stream().filter( current -> { return current.getGroupCap() !=null && current.getGroupCap().equalsIgnoreCase(capSize)?true:false; }).collect(Collectors.toList());
    }

    private List<StockCompleteData> filterOnPrice(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()!=null &&  current.getCurrentPrice()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()!=null &&  current.getCurrentPrice()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()!=null &&  current.getCurrentPrice()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()!=null &&  current.getCurrentPrice()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getCurrentPrice()!=null &&  current.getCurrentPrice()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnEPS(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getEps()!=null && current.getEps()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getEps()!=null && current.getEps()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getEps()!=null && current.getEps()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getEps()!=null && current.getEps()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getEps()!=null && current.getEps()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnBV(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getBookvalue()!=null && current.getBookvalue()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getBookvalue()!=null && current.getBookvalue()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getBookvalue()!=null && current.getBookvalue()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getBookvalue()!=null && current.getBookvalue()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getBookvalue()!=null && current.getBookvalue()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnPE(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getPe()!=null &&  current.getPe()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getPe()!=null && current.getPe()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getPe()!=null && current.getPe()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getPe()!=null && current.getPe()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return  current.getPe()!=null && current.getPe()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnPB(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getPb()!=null && current.getPb()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getPb()!=null && current.getPb()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getPb()!=null && current.getPb()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getPb()!=null && current.getPb()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getPb()!=null && current.getPb()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnROE(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getRoe()!=null && current.getRoe()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getRoe()!=null && current.getRoe()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getRoe()!=null && current.getRoe()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getRoe()!=null && current.getRoe()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getRoe()!=null && current.getRoe()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnDiv(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getDivident()!=null && current.getDivident()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getDivident()!=null && current.getDivident()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getDivident()!=null && current.getDivident()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getDivident()!=null && current.getDivident()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getDivident()!=null && current.getDivident()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

    private List<StockCompleteData> filterOnRev(List<StockCompleteData> presentData , Double price, String operator)
    {
        if (operator.equalsIgnoreCase("=="))
            return presentData.stream().filter( current -> { return current.getRevenue()!=null && current.getRevenue()==price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">="))
            return presentData.stream().filter( current -> { return current.getRevenue()!=null &&  current.getRevenue()>=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase(">"))
            return presentData.stream().filter( current -> { return current.getRevenue()!=null && current.getRevenue()>price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<="))
            return presentData.stream().filter( current -> { return current.getRevenue()!=null && current.getRevenue()<=price?true:false; }).collect(Collectors.toList());
        else if (operator.equalsIgnoreCase("<"))
            return presentData.stream().filter( current -> { return current.getRevenue()!=null && current.getRevenue()<price?true:false; }).collect(Collectors.toList());
        return presentData;
    }

}
