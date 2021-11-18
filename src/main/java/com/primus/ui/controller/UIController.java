package com.primus.ui.controller;

import com.primus.ui.service.UIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/uiapi")
@RestController
public class UIController {

    @Autowired
    UIService uiService;


    @RequestMapping(value = "/getDistinctSector", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getDistinctSector()
    {
        List<String> returnData = uiService.getDistinctSector();
        ResponseEntity entity =  new ResponseEntity<List<String>>(returnData, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/getDistinctIndustry", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getDistinctIndusty()
    {
        List<String> returnData = uiService.getDistinctIndustry();
        ResponseEntity entity =  new ResponseEntity<List<String>>(returnData, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/getAllStocks", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> getAllStocks(@RequestParam int from, @RequestParam int to )
    {
        List<Map> returnData = uiService.getAllStocks(from,to);
        ResponseEntity entity =  new ResponseEntity<List<Map>>(returnData, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/applyFilterStocks", method = RequestMethod.POST)
    public ResponseEntity<List<Map>> applyFilterStocks(@RequestParam int from, @RequestParam int to,
                                                       @RequestBody Map<String,Object> filterCriteria )
    {
        List<Map> returnData = uiService.applyFilterStocks(from,to,filterCriteria);
        ResponseEntity entity =  new ResponseEntity<List<Map>>(returnData, HttpStatus.OK);
        return  entity;

    }


    @RequestMapping(value = "/getAllStockCount", method = RequestMethod.GET)
    public ResponseEntity<Integer> getAllStockCount()
    {
        Integer returnData = uiService.getAllStockCount(null);
        ResponseEntity entity =  new ResponseEntity<Integer>(returnData, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/filteredStockCount", method = RequestMethod.POST)
    public ResponseEntity<Integer> filteredStockCount(@RequestBody Map<String,Object> criteriaMap)
    {
        Integer returnData = uiService.getAllStockCount(criteriaMap);
        ResponseEntity entity =  new ResponseEntity<Integer>(returnData, HttpStatus.OK);
        return  entity;

    }



}
