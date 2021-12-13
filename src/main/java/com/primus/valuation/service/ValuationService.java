package com.primus.valuation.service;

import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.FinancialService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stock.master.service.StockMasterService;
import com.primus.ui.model.StockCompleteData;
import com.primus.ui.service.UIService;
import com.primus.utils.MathUtil;
import com.primus.valuation.data.IntrinsicData;
import com.primus.valuation.data.StockValuationData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValuationService {

    @Autowired
    UIService uiService;

    @Autowired
    FundamentalService fundamentalService;

    @Autowired
    FinancialService financialService;

    @Autowired
    StockMasterService stockMasterService;

    public List<StockValuationData> getUShares()
    {
        List<FundamentalData> fundamentalDataList = fundamentalService.listData(0,900,"","");
        List<FinancialData> financialDataList = financialService.getAllFinancials() ;
        List<StocksMaster> stocksMasterList = stockMasterService.getAllStocks();
        List<StockValuationData> stockCompleteDataList = new ArrayList<>();

        for (FundamentalData fundamentalData : fundamentalDataList) {
            FinancialData financialDataSel = financialDataList.stream().filter( financialData ->
            {  return financialData.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null) ;
            StocksMaster stocksMasterSel = stocksMasterList.stream().filter(stocksMaster ->
            { return  stocksMaster.getBseCode().equalsIgnoreCase(fundamentalData.getBseCode())?true:false; }).findFirst().orElse(null);
            StockValuationData stockCompleteData = new StockValuationData(fundamentalData,financialDataSel,stocksMasterSel);
            stockCompleteDataList.add(stockCompleteData);
        }

        Set<String> sectors = new HashSet<>();
        stocksMasterList.forEach( stocksMaster ->  {
            if (StringUtils.isNotEmpty(stocksMaster.getSector() ))
                sectors.add(stocksMaster.getSector());
        });

        List<StockValuationData> uvShares = new ArrayList<>();
        for (String sector : sectors) {
            List<StockValuationData> uvLShares = identifyUndervaluedShares(sector,"L",stockCompleteDataList);
            uvShares.addAll(uvLShares);

            List<StockValuationData> uvMShares = identifyUndervaluedShares(sector,"M",stockCompleteDataList);
            uvShares.addAll(uvMShares);

            List<StockValuationData> uvSShares = identifyUndervaluedShares(sector,"S",stockCompleteDataList);
            uvShares.addAll(uvSShares);


        }
        return uvShares;

    }

    private List<StockValuationData> identifyUndervaluedShares(String sector, String marketCap,List<StockValuationData> stockCompleteDataList)
    {
        List<StockValuationData> underValuedShares= new ArrayList<>();
        List<StockValuationData> filteredDataList = stockCompleteDataList.stream().filter(stockCompleteData ->  {
            return  stockCompleteData.getSector().equalsIgnoreCase(sector) && stockCompleteData.getGroupCap().equalsIgnoreCase(marketCap);
        }).collect(Collectors.toList());
        IntrinsicData intrinsicData = makeIntrinsicData(sector,marketCap,filteredDataList);
        for (StockValuationData stockCompleteData : filteredDataList)
        {
            if(intrinsicData.getPe()> MathUtil.getDoubleValue(stockCompleteData.getPe())
                    && intrinsicData.getPb() > MathUtil.getDoubleValue(stockCompleteData.getPb())
              && intrinsicData.getRoe() < stockCompleteData.getRoe() && intrinsicData.getDivident() < stockCompleteData.getDivident()
            && MathUtil.getDoubleValue(stockCompleteData.getPe()) >0 && MathUtil.getDoubleValue(intrinsicData.getPb()) > 0)
            {
                Double peDecr =  MathUtil.perChange(stockCompleteData.getPe(),intrinsicData.getPe());
                Double pbDecr =  MathUtil.perChange(stockCompleteData.getPb(),intrinsicData.getPb());
                Double roeIncr =  MathUtil.perChange(stockCompleteData.getRoe(),intrinsicData.getRoe());
                stockCompleteData.setOverValuedBy((float)MathUtil.getDoubleValue(Math.abs(peDecr)+ Math.abs(pbDecr) + Math.abs(roeIncr))/3);
                underValuedShares.add(stockCompleteData);
            }
        }
        return underValuedShares;
    }


    private IntrinsicData makeIntrinsicData(String sector, String marketCap, List<StockValuationData> filteredDataList)
    {



        List<Double> pbList =  new ArrayList<>();
        List<Double> peList = new ArrayList<>();
        List<Double> roeList =  new ArrayList<>();
        List<Double> dividentList = new ArrayList<>();

        filteredDataList.stream().forEach( filteredData -> {
              if (filteredData.getPb() != null)
                    pbList.add(filteredData.getPb());
              else {
                  System.out.println("break");
              }
              if (filteredData.getPe() != null)
                peList.add(filteredData.getPe());
              else {
                  System.out.println("break");
              }

              roeList.add(filteredData.getRoe());
              dividentList.add(filteredData.getDivident()==null?0.0:filteredData.getDivident());

        });
        try {
            IntrinsicData intrinsicData = new IntrinsicData();
            intrinsicData.setPb(MathUtil.getMedian(pbList));
            intrinsicData.setPe(MathUtil.getMedian(peList));
            intrinsicData.setRoe(MathUtil.getMedian(roeList));
            intrinsicData.setDivident(MathUtil.getMedian(dividentList));

            return intrinsicData;
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;


    }

}
