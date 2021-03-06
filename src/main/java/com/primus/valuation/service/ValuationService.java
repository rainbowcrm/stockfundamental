package com.primus.valuation.service;

import com.primus.common.LogWriter;
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
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    ValuationHelper valuationHelper;

    @Cacheable( value = "OVCache")
    public List<StockValuationData> getOverValuedShares()
    {
        List<StocksMaster> stocksMasterList = stockMasterService.getAllStocks();
        List<StockValuationData> stockCompleteDataList= valuationHelper.getStockCompleteDataList(stocksMasterList,false);
        Set<String> sectors = getSectors(stocksMasterList);
        List<StockValuationData> ovShares = getOVShares(sectors,stocksMasterList,stockCompleteDataList);
        return ovShares;
    }

    @Cacheable( value = "UVCache")
    public List<StockValuationData> getUnderValuedShares()
    {
        List<StocksMaster> stocksMasterList = stockMasterService.getAllStocks();
        List<StockValuationData> stockCompleteDataList= valuationHelper.getStockCompleteDataList(stocksMasterList,false);
        Set<String> sectors = getSectors(stocksMasterList);
        List<StockValuationData> uvShares = getUVShares(sectors,stocksMasterList,stockCompleteDataList);
        return uvShares;
    }

    private List<StockValuationData> getOVShares(Set<String> sectors,List<StocksMaster> stocksMasterList,List<StockValuationData> stockCompleteDataList)
    {
        List<StockValuationData> ovShares = new ArrayList<>();
        for (String sector : sectors) {
            Set<String> industries = uniqueIndustries(sector,stockCompleteDataList);
            for (String industry : industries) {
                List<StockValuationData> ovLShares = identifyOvervaluedShares(sector, "L", stockCompleteDataList,industry);
                ovShares.addAll(ovLShares);

                List<StockValuationData> ovMShares = identifyOvervaluedShares(sector, "M", stockCompleteDataList,industry);
                ovShares.addAll(ovMShares);

                List<StockValuationData> ovSShares = identifyOvervaluedShares(sector, "S", stockCompleteDataList,industry);
                ovShares.addAll(ovSShares);
            }

        }

        Collections.sort(
                ovShares,
                (i1,
                 i2) ->    i1.getOverValuedBy() < i2.getOverValuedBy()?1:-1);
        return ovShares;

    }

    private Set<String> uniqueIndustries (String sector,List<StockValuationData> stockCompleteDataList)
    {
        Set<String> industries = new LinkedHashSet<>();
        List<StockValuationData> sectorStocks = stockCompleteDataList.stream().filter( sectorStock ->
        { return sector.equalsIgnoreCase(sectorStock.getSector())?true:false; }).collect(Collectors.toList());
        for ( StockValuationData sectorStock : sectorStocks ) {
            industries.add(sectorStock.getIndustry()) ;
        }
        return industries ;

    }

        private List<StockValuationData> getUVShares(Set<String> sectors,List<StocksMaster> stocksMasterList,List<StockValuationData> stockCompleteDataList)
    {

        List<StockValuationData> uvShares = new ArrayList<>();
        for (String sector : sectors) {
            Set<String> industries = uniqueIndustries(sector,stockCompleteDataList);
            for (String industry : industries) {

                List<StockValuationData> uvLShares = identifyUndervaluedShares(sector, "L", stockCompleteDataList,industry);
                uvShares.addAll(uvLShares);

                List<StockValuationData> uvMShares = identifyUndervaluedShares(sector, "M", stockCompleteDataList,industry);
                uvShares.addAll(uvMShares);

                List<StockValuationData> uvSShares = identifyUndervaluedShares(sector, "S", stockCompleteDataList,industry);
                uvShares.addAll(uvSShares);
            }

        }
        Collections.sort(
                uvShares,
                (i1,
                 i2) ->    i1.getOverValuedBy() > i2.getOverValuedBy()?1:-1);
        return uvShares;
    }




    private Set<String> getSectors(List<StocksMaster> stocksMasterList)
    {
        Set<String> sectors = new HashSet<>();
        stocksMasterList.forEach( stocksMaster ->  {
            if (StringUtils.isNotEmpty(stocksMaster.getSector() ))
                sectors.add(stocksMaster.getSector());
        });
        return sectors;
    }
    public Map<String,List<StockValuationData>> getUOVShares( int noOV,int noUV)
    {
        List<StocksMaster> stocksMasterList = stockMasterService.getAllStocks();
        List<StockValuationData> stockCompleteDataList= valuationHelper.getStockCompleteDataList(stocksMasterList,false);
        Set<String> sectors = getSectors(stocksMasterList);
        List<StockValuationData> uvShares = getUVShares(sectors,stocksMasterList,stockCompleteDataList);
        if (noUV > 0 ) {
            while ( uvShares.size() > noUV){
                uvShares.remove( uvShares.size() -1 );
            }
        }
        List<StockValuationData> ovShares = getOVShares(sectors,stocksMasterList,stockCompleteDataList);
        if (noOV > 0 ) {
            while ( ovShares.size() > noOV){
                ovShares.remove( ovShares.size() -1 );
            }
        }
        Map<String,List<StockValuationData>> highVariantShares = new HashMap<>();
        highVariantShares.put("OV",ovShares);
        highVariantShares.put("UV",uvShares);
        return highVariantShares;
    }



    private List<StockValuationData> identifyUndervaluedShares(String sector, String marketCap,List<StockValuationData> stockCompleteDataList,
                                                               String industry)
    {
        List<StockValuationData> underValuedShares= new ArrayList<>();
        List<StockValuationData> filteredDataList = stockCompleteDataList.stream().filter(stockCompleteData ->  {
            return  sector.equalsIgnoreCase(stockCompleteData.getSector()) &&
                    industry.equalsIgnoreCase(stockCompleteData.getIndustry()) &&
                    marketCap.equalsIgnoreCase(stockCompleteData.getGroupCap());
        }).collect(Collectors.toList());
        IntrinsicData intrinsicData = makeIntrinsicData(sector,marketCap,filteredDataList);
        for (StockValuationData stockCompleteData : filteredDataList)
        {
            if(intrinsicData.getPe()> MathUtil.getDoubleValue(stockCompleteData.getPe())
                    && intrinsicData.getPb() > MathUtil.getDoubleValue(stockCompleteData.getPb())
              && intrinsicData.getRoe() < stockCompleteData.getRoe() &&  (stockCompleteData.getDivident() != null && intrinsicData.getDivident() < stockCompleteData.getDivident())
            && MathUtil.getDoubleValue(stockCompleteData.getPe()) >0 && MathUtil.getDoubleValue(intrinsicData.getPb()) > 0)
            {
                Double peDecr =  MathUtil.perChange(stockCompleteData.getPe(),intrinsicData.getPe());
                Double pbDecr =  MathUtil.perChange(stockCompleteData.getPb(),intrinsicData.getPb());
                Double roeIncr =  MathUtil.perChange(stockCompleteData.getRoe(),intrinsicData.getRoe());
                stockCompleteData.setOverValuedBy(-1 *  ((float)MathUtil.getDoubleValue(Math.abs(peDecr)+ Math.abs(pbDecr) + Math.abs(roeIncr))/3));
                stockCompleteData.setIntrinsicData(intrinsicData);
                Double closestFactor = (Math.abs(peDecr)<Math.abs(pbDecr) && Math.abs(peDecr)<Math.abs(roeIncr))?Math.abs(peDecr)
                        :((Math.abs(pbDecr)<Math.abs(roeIncr))?Math.abs(pbDecr):Math.abs(roeIncr));

                Double intrinsicValue = stockCompleteData.getCurrentPrice() + MathUtil.round( ((closestFactor * stockCompleteData.getCurrentPrice())/100) );
                stockCompleteData.setFairPrice(MathUtil.round(intrinsicValue));
                underValuedShares.add(stockCompleteData);
            }
        }
        return underValuedShares;
    }

    private List<StockValuationData> identifyOvervaluedShares(String sector, String marketCap,List<StockValuationData> stockCompleteDataList,
                                                              String industry)
    {
        List<StockValuationData> overValuedShares= new ArrayList<>();
        List<StockValuationData> filteredDataList = stockCompleteDataList.stream().filter(stockCompleteData ->  {
            return  sector.equalsIgnoreCase(stockCompleteData.getSector()) &&
                    industry.equalsIgnoreCase(stockCompleteData.getIndustry()) &&
                    marketCap.equalsIgnoreCase(stockCompleteData.getGroupCap());
        }).collect(Collectors.toList());
        IntrinsicData intrinsicData = makeIntrinsicData(sector,marketCap,filteredDataList);
        for (StockValuationData stockCompleteData : filteredDataList)
        {
            if(intrinsicData.getPe()< MathUtil.getDoubleValue(stockCompleteData.getPe())
                    && intrinsicData.getPb() < MathUtil.getDoubleValue(stockCompleteData.getPb())
                    && intrinsicData.getRoe() > stockCompleteData.getRoe() && (stockCompleteData.getDivident() == null || intrinsicData.getDivident() > stockCompleteData.getDivident())
                    && MathUtil.getDoubleValue(stockCompleteData.getPe()) >0 && MathUtil.getDoubleValue(intrinsicData.getPb()) > 0)
            {
                Double peDecr =  MathUtil.perChange(stockCompleteData.getPe(),intrinsicData.getPe());
                Double pbDecr =  MathUtil.perChange(stockCompleteData.getPb(),intrinsicData.getPb());
                Double roeIncr =  MathUtil.perChange(stockCompleteData.getRoe(),intrinsicData.getRoe());
                stockCompleteData.setOverValuedBy(( (float)MathUtil.getDoubleValue(Math.abs(peDecr)+ Math.abs(pbDecr) + Math.abs(roeIncr))/3));
                stockCompleteData.setIntrinsicData(intrinsicData);
                Double closestFactor = (Math.abs(peDecr)<Math.abs(pbDecr) && Math.abs(peDecr)<Math.abs(roeIncr))?Math.abs(peDecr)
                        :((Math.abs(pbDecr)<Math.abs(roeIncr))?Math.abs(pbDecr):Math.abs(roeIncr));

                Double intrinsicValue = stockCompleteData.getCurrentPrice() - MathUtil.round( ((closestFactor * stockCompleteData.getCurrentPrice())/100) );
                stockCompleteData.setFairPrice(MathUtil.round(intrinsicValue));
                overValuedShares.add(stockCompleteData);
            }
        }
        return overValuedShares;
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
            intrinsicData.setPb(MathUtil.round(MathUtil.getMedian(pbList)));
            intrinsicData.setPe(MathUtil.round(MathUtil.getMedian(peList)));
            intrinsicData.setRoe(MathUtil.round(MathUtil.getMedian(roeList)));
            intrinsicData.setDivident(MathUtil.round(MathUtil.getMedian(dividentList)));

            return intrinsicData;
        }catch (Exception ex)
        {

            LogWriter.logException("Ex in ValuationService" ,this.getClass(),ex);
        }
        return null;


    }

}
