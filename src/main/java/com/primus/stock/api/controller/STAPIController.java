package com.primus.stock.api.controller;


import com.primus.stock.api.service.APIService;
import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.service.FinancialService;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stock.master.service.QuarterReportService;
import com.primus.stock.master.service.StockMasterService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/stockapi/api")
@RestController
public class STAPIController {

    @Autowired
    APIService apiService;

    @Autowired
    FundamentalService fundamentalService ;

    @Autowired
    FinancialService financialService ;

    @Autowired
    StockMasterService stockMasterService;

    @Autowired
    QuarterReportService quarterReportService;


    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/justTry", method = RequestMethod.GET)
    public ResponseEntity<Map<String,String>> justTry()
    {
        Map<String,String> res = new HashMap();
        res.put("result","success");
        ResponseEntity entity =  new ResponseEntity<Map<String,String>>(res, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/getFundamentals", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> readFundamentals(@RequestParam String scripCode)
    {

        try {
            Map<String,Object> comHeaderData = apiService.getCompanyHeader(scripCode);
            Map<String,Object> scripHeaderData = apiService.getScripHeaderData(scripCode);
            Map<String,Object> fundamentalData = new HashMap<>();
            fundamentalData.put("CompHeaderData",comHeaderData) ;
            fundamentalData.put("ScripHeaderData",scripHeaderData);
            ResponseEntity entity =  new ResponseEntity<Map<String,Object>>(fundamentalData, HttpStatus.OK);
            return  entity;


        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }


    @RequestMapping(value = "/getFinancials", method = RequestMethod.GET)
    public ResponseEntity<String> readFinancials(@RequestParam String scripCode)
    {

        try {
            financialService.saveFinancialData(scripCode,1L);
            ResponseEntity entity =  new ResponseEntity<String>("Success", HttpStatus.OK);
            return  entity;


        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }

    @RequestMapping(value = "/saveFundamentals", method = RequestMethod.GET)
    public ResponseEntity<String> readAndSaveFundamentals(@RequestParam String scripCode)
    {

        try {
            FundamentalData fundamentalData = fundamentalService.saveFundamentals(scripCode);
            ResponseEntity entity =  new ResponseEntity<String>("Hello", HttpStatus.OK);
            return  entity;

        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }

    @RequestMapping(value = "/saveAllFundamentals", method = RequestMethod.GET)
    public ResponseEntity<String> saveAllFundamentals()
    {

        try {
            fundamentalService.saveAllFundamentals("X ");
            ResponseEntity entity =  new ResponseEntity<String>("Success", HttpStatus.OK);
            return  entity;

        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }

    @RequestMapping(value = "/saveAllMarketCap", method = RequestMethod.GET)
    public ResponseEntity<String> saveAllMarketCap()
    {

        try {
            //stockMasterService.updateMarketCap();
            fundamentalService.updateMarketCap();
            ResponseEntity entity =  new ResponseEntity<String>("Success", HttpStatus.OK);
            return  entity;

        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }

    @RequestMapping(value = "/saveAllFinancials", method = RequestMethod.GET)
    public ResponseEntity<String> saveAllFinancials()
    {

        try {
            financialService.saveAllFinancialData("X ");
            ResponseEntity entity =  new ResponseEntity<String>("Success", HttpStatus.OK);
            return  entity;

        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }

    @RequestMapping(value = "/saveQuarterly", method = RequestMethod.GET)
    public ResponseEntity<String> saveQuarterly(@RequestParam long year, @RequestParam int quarter )
    {

        try {
            quarterReportService.saveQuarterData(year,quarter);
            ResponseEntity entity =  new ResponseEntity<String>("Success", HttpStatus.OK);
            return  entity;

        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }

    @RequestMapping(value = "/exportAllFundamentals", method = RequestMethod.GET)
    public ResponseEntity<String> exportAllFundamentals()
    {

        try {
            fundamentalService.exportToXLS();
            ResponseEntity entity =  new ResponseEntity<String>("Success", HttpStatus.OK);
            return  entity;

        }catch (Exception ex){
            ex.printStackTrace();

        }
        ResponseEntity entity =  new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        return  entity;

    }
}
