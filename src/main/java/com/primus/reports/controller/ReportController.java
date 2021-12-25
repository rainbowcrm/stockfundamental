package com.primus.reports.controller;

import com.primus.reports.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/getTransSummary", method = RequestMethod.GET)
    public void getTransSummary(@RequestParam String fromDate, @RequestParam String toDate,@RequestParam String groupBy)
    {
        try {
            reportService.generateReport(fromDate, toDate, groupBy, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
