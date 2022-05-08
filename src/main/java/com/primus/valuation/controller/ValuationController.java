package com.primus.valuation.controller;

import com.primus.common.BusinessContext;
import com.primus.useractions.service.UserActionFacade;
import com.primus.valuation.data.StockValuationData;
import com.primus.valuation.service.SwingTradingService;
import com.primus.valuation.service.ValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/stockapi/valuation")
public class ValuationController{

    @Autowired
    ValuationService valuationService ;

    @Autowired
    SwingTradingService swingTradingService ;

    @Autowired
    UserActionFacade userActionFacade;

    @RequestMapping(value = "/getUnderValued", method = RequestMethod.GET)
    public ResponseEntity<List<StockValuationData>> getUnderValuedStocks()
    {
        List<StockValuationData> uvShares=  valuationService.getUnderValuedShares();
        userActionFacade.saveUserAction(BusinessContext.getBusinessContent(),"underValued Shares"," ");
        ResponseEntity<List<StockValuationData>> entity =  new ResponseEntity<List<StockValuationData>>(uvShares, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/getSwingRecommendations", method = RequestMethod.GET)
    public ResponseEntity<List<StockValuationData>> getSwingRecommendations()
    {
        BusinessContext businessContext = BusinessContext.getBusinessContent();
        List<StockValuationData> uvShares=  swingTradingService.giveRecommendations(businessContext.getUserPreferences().getTechDays());
        userActionFacade.saveUserAction(businessContext,"Swinging Shares"," ");
        ResponseEntity<List<StockValuationData>> entity =  new ResponseEntity<List<StockValuationData>>(uvShares, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/getOverValued", method = RequestMethod.GET)
    public ResponseEntity<List<StockValuationData>> getOverValuedStocks()
    {
        List<StockValuationData> uvShares=  valuationService.getOverValuedShares();
        userActionFacade.saveUserAction(BusinessContext.getBusinessContent(),"Overvalued Shares"," ");
        ResponseEntity<List<StockValuationData>> entity =  new ResponseEntity<List<StockValuationData>>(uvShares, HttpStatus.OK);
        return  entity;

    }

}