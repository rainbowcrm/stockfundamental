package com.primus.reports.controller;

import com.primus.common.BusinessContext;
import com.primus.reports.service.DetailReportService;
import com.primus.reports.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/stockapi/reports")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    DetailReportService detailReportService ;

    @RequestMapping(value = "/getTransSummary", method = RequestMethod.GET)
    public void getTransSummary(@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String groupBy,
                                @RequestParam String repFormat,
                                HttpServletRequest request, HttpServletResponse response)
    {
        try {
            BusinessContext businessContext = BusinessContext.getBusinessContent();
           Resource resource = reportService.generateReport(fromDate, toDate, groupBy, repFormat,businessContext);
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
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,ex.getMessage(),ex.getMessage().getBytes(),null);
        }
    }

    @RequestMapping(value = "/getTransDetails", method = RequestMethod.GET)
    public void getTransDetails(@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String stock,
                                @RequestParam String repFormat,
                                HttpServletRequest request, HttpServletResponse response)
    {
        try {
            BusinessContext businessContext = BusinessContext.getBusinessContent();
            Resource resource = detailReportService.generateReport(fromDate, toDate, stock, repFormat,businessContext);
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
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST,ex.getMessage(),ex.getMessage().getBytes(),null);
        }
    }
}
