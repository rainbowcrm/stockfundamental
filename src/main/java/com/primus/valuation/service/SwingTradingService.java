package com.primus.valuation.service;

import com.primus.common.BusinessContext;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.StockMasterService;
import com.primus.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.primus.valuation.data.StockValuationData;
import java.util.*;

@Service
public class SwingTradingService  {

    @Autowired
    StockMasterService stockMasterService ;

    @Autowired
    ValuationHelper valuationHelper;

    @Autowired
    ValuationService valuationService;




    @Cacheable( key="#p0", value = "SwingTradeCache")
    public List<StockValuationData> giveRecommendations(Integer days)
    {
        List<StockValuationData> uvShares = valuationService.getUnderValuedShares();
        List<Double> stdValues = new ArrayList<>();
        List<StockValuationData> swingShares = new ArrayList<>();
        for (StockValuationData stockValuationData : uvShares) {
            valuationHelper.setTechnicals(stockValuationData, days);
            if (stockValuationData.getMedianPrice() != null && stockValuationData.getMedianPrice() > 0)
                stdValues.add(stockValuationData.getRelStdDeviation());
        }
        Double medianDev= MathUtil.getMedian(stdValues);
        for (StockValuationData stockValuationData : uvShares) {
            if ( stockValuationData.getRelStdDeviation() !=null && stockValuationData.getMedianPrice() != null &&
                    stockValuationData.getRelStdDeviation() > (medianDev/2)
                    && stockValuationData.getCurrentPrice() < stockValuationData.getMedianPrice()) {
                swingShares.add(stockValuationData);
            }
        }
        return swingShares;
    }





}
