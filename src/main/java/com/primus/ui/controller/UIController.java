package com.primus.ui.controller;

import com.primus.common.BusinessContext;
import com.primus.dashboard.service.DashboardService;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.StockMasterService;
import com.primus.ui.model.CompetitorData;
import com.primus.ui.model.StockCompleteData;
import com.primus.ui.service.LookupService;
import com.primus.ui.service.UIService;
import com.primus.user.model.UserPreferences;
import com.primus.user.service.UserService;
import com.primus.useractions.service.UserActionFacade;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/stockapi/uiapi")
@RestController
public class UIController {

    @Autowired
    UIService uiService;

    @Autowired
    DashboardService dashboardService;

    @Autowired
    LookupService lookupService;

    @Autowired
    UserService userService;

    @Autowired
    StockMasterService stockMasterService;

    @Autowired
    UserActionFacade userActionFacade ;


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
        userActionFacade.saveUserAction(BusinessContext.getBusinessContent(),"View Stocks",filterCriteria);
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


    @RequestMapping(value = "/lookup", method = RequestMethod.GET)
    public ResponseEntity<Map<String,String>> lookupValues(@RequestParam String lookupType, @RequestParam String searchStr,
                                                           @RequestParam(required = false) String additionalInput)
    {
        Map<String,String> returnData = lookupService.lookupVals(lookupType,searchStr);
        ResponseEntity entity =  new ResponseEntity<Map<String,String>>(returnData, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping( value = "/downloadFile/{fileType}", method = RequestMethod.POST)
    public void getHttpResponse(@PathVariable String fileType, HttpServletRequest request, HttpServletResponse response,
                                        @RequestBody Map<String,Object> criteriaMap      ) {
        String contentType = null;
        Resource resource = null;
        try {

            resource = uiService.exportDataAsFile(criteriaMap,fileType);
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if(contentType == null) {
                contentType = "application/vnd.ms-excel";
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=repNew.xlsx");
            byte[] bytes = resource.getInputStream().readAllBytes();

            response.getOutputStream().write(bytes,0,(int)resource.getFile().length());
            userActionFacade.saveUserAction(BusinessContext.getBusinessContent(),"Download Stock",criteriaMap);

                          /*  byte[] bytes=  ResponseEntity<byte[]>.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.getFile().length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename='" + resource.getFilename() + "'")
                    .body(IOUtils.toByteArray(resource.getInputStream())); */

            } catch (Exception ex) {
            ex.printStackTrace();
            }
     //   ResponseEntity entity =  new ResponseEntity<b>(resource, HttpStatus.BAD_REQUEST);
        return  ;
    }

    @RequestMapping(value = "/getDashBoardData", method = RequestMethod.GET)
    public ResponseEntity<Map> getDashBoardData()
    {
        BusinessContext businessContext= BusinessContext.getBusinessContent() ;
        UserPreferences userPreferences = userService.getPreferences(businessContext);
        int days = 30;
        if ( userPreferences !=null ) {
            days = userPreferences.getDashboardDays();
        }
        Map returnData = dashboardService.getPersistedDashboardData(days);
        userActionFacade.saveUserAction(businessContext,"Dashboard",days);
        ResponseEntity<Map> entity =  new ResponseEntity<Map>(returnData, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/getStockCompleteData", method = RequestMethod.GET)
    public ResponseEntity<StockCompleteData> getStockCompleteData(@RequestParam String bseCode)
    {
        BusinessContext businessContext= BusinessContext.getBusinessContent() ;
        StockCompleteData stockCompleteData = uiService.getStockCompleteData(bseCode,businessContext);
        userActionFacade.saveUserAction(businessContext,"View Details",bseCode);
        ResponseEntity<StockCompleteData> entity =  new ResponseEntity<StockCompleteData>(stockCompleteData, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/getStockFromSecurity", method = RequestMethod.GET)
    public ResponseEntity<StocksMaster> getStockFromSecurity(@RequestParam String securityName)
    {
        BusinessContext businessContext= BusinessContext.getBusinessContent() ;
        StocksMaster stocksMaster = stockMasterService.getStocksDataFromName(securityName);
        ResponseEntity<StocksMaster> entity =  new ResponseEntity<StocksMaster>(stocksMaster, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/getCompetitorData", method = RequestMethod.GET)
    public ResponseEntity<CompetitorData> getCompetitorData(@RequestParam String bseCode)
    {
        BusinessContext businessContext= BusinessContext.getBusinessContent() ;
        CompetitorData stockCompleteData = uiService.getCompetitorData(bseCode,businessContext);
        ResponseEntity<CompetitorData> entity =  new ResponseEntity<CompetitorData>(stockCompleteData, HttpStatus.OK);
        return  entity;

    }




}
