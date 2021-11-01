package com.primus.stock.api.controller;


import com.primus.stock.api.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/api")
@RestController
public class STAPIController {

    @Autowired
    APIService apiService;


    @RequestMapping(value = "/justTry", method = RequestMethod.GET)
    public ResponseEntity<String> justTry()
    {
        ResponseEntity entity =  new ResponseEntity<String>("Hello", HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/getFundamentals", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> readFundamentals(@RequestParam String scripCode)
    {

        try {
            Map<String,Object> comHeaderData = apiService.getCompanyHeader(scripCode);
            Map<String,Object> scripHeaderData = apiService.getScripHeaderData(scripCode);
            Map<String,Object> fundamentalData = new HashMap<>();
            fundamentalData.put("Comp",comHeaderData) ;
            fundamentalData.put("Scrip",scripHeaderData);
            ResponseEntity entity =  new ResponseEntity<Map<String,Object>>(fundamentalData, HttpStatus.OK);
            return  entity;


        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }
}
