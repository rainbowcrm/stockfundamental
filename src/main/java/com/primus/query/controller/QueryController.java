package com.primus.query.controller;

import com.primus.common.BusinessContext;
import com.primus.query.model.QueryLine;
import com.primus.query.service.QueryService;
import com.primus.ui.model.StockCompleteData;
import com.primus.useractions.model.UserAction;
import com.primus.useractions.service.UserActionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/stockapi/query")
public class QueryController {

    @Autowired
    QueryService queryService ;

    @Autowired
    UserActionFacade userActionFacade;

    @RequestMapping(value = "/runQuery", method = RequestMethod.POST)
    public ResponseEntity<List<StockCompleteData>> applyQuery(@RequestBody List<QueryLine> queryLines )
    {
        List<StockCompleteData> stockCompleteDataList = queryService.applyFilter(queryLines);
        userActionFacade.saveUserAction(BusinessContext.getBusinessContent(),"Query",queryLines);
        ResponseEntity entity =  new ResponseEntity<List<StockCompleteData>>(stockCompleteDataList, HttpStatus.OK);
        return  entity;
    }

}
