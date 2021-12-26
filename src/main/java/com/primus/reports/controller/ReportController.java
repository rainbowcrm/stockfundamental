package com.primus.reports.controller;

import com.primus.reports.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/getTransSummary", method = RequestMethod.GET)
    public void getTransSummary(@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String groupBy,
                                @RequestParam String repFormat,
                                HttpServletRequest request, HttpServletResponse response)
    {
        try {
           Resource resource = reportService.generateReport(fromDate, toDate, groupBy, repFormat,null);
           if("PDF".equalsIgnoreCase(repFormat)) {
               response.setContentType("application/pdf");
               response.setHeader("Content-Disposition", "attachment; filename=repNew.pdf");
           }else if ("CSV".equalsIgnoreCase(repFormat)){
               response.setContentType("application/CSV");
               response.setHeader("Content-Disposition", "attachment; filename=repNew.csv");
           }
            byte[] bytes = resource.getInputStream().readAllBytes();
            response.getOutputStream().write(bytes,0,(int)resource.getFile().length());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
