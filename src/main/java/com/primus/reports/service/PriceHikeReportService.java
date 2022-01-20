package com.primus.reports.service;

import com.primus.common.BusinessContext;
import com.primus.common.CommonErrorCodes;
import com.primus.common.PrimusError;
import com.primus.reports.data.TransReportData;
import com.primus.stock.master.dao.StockMasterDAO;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.utils.ExportService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import javax.mail.Transport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PriceHikeReportService extends ReportService{

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterDAO stockMasterDAO ;

    private static void sort(List<TransReportData> list)
    {
            list.sort((o1, o2)
                    -> o1.getChange().compareTo(
                    o2.getChange()));
    }

    private void getRangeforChange(List<TransReportData> transReportDataList)
    {
         Double minHike = new Double(Double.MAX_VALUE);
         Double maxHike = new Double(Double.MIN_VALUE);
         for (TransReportData transReportData : transReportDataList) {
             if( transReportData.getChange() > maxHike)  {
                 maxHike = transReportData.getChange() ;
             }
             if( transReportData.getChange() < minHike)  {
                 minHike = transReportData.getChange() ;
             }
         }


    }

    private String  createHTML(List<TransReportData> transReportDataList,BusinessContext businessContext,
                               String fromDate, String toDate)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML><HEAD><TITLE>Trade Details</TITLE></HEAD>");
        stringBuffer.append("<BODY>");
        stringBuffer.append(getHeader(businessContext,fromDate,toDate));
        stringBuffer.append("<TABLE WIDTH='90%'>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TH>Security</TH>"  );
        stringBuffer.append("<TH>Open Price</TH>");
        stringBuffer.append("<TH>Close Price</TH>");
        stringBuffer.append("<TH>Change%</TH>");
        stringBuffer.append("</TR>");

        for (TransReportData transReportData : transReportDataList )
        {

            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>" +  transReportData.getSecurity().replace("&#160;"," ") + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getOpeningPrice() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getFinalPricde() + "</TD>");
            stringBuffer.append("<TD><B>" +  transReportData.getChange() + "</B></TD>");
            stringBuffer.append("</TR>");
        }
        stringBuffer.append("</TABLE>");
        stringBuffer.append("</HTML>");
        return stringBuffer.toString();

    }

    public Resource generateReport(String fromDateS, String toDateS,String repFormat, BusinessContext context) throws PrimusError
    {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = simpleDateFormat.parse(fromDateS);
            Date toDate = simpleDateFormat.parse(toDateS);
            List<TransReportData> transReportDataList = generateReport(fromDate, toDate);
            sort(transReportDataList);
            String unqValue ="";
            if("PDF".equalsIgnoreCase(repFormat)) {
                String xhtml = createHTML(transReportDataList,context,fromDateS,toDateS);
                unqValue = "reports/" + ExportService.randomStr()   +".pdf";
                String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
                String rootFolder = absPath.substring(0,absPath.length()-22);
                Document document = Jsoup.parse(xhtml, "UTF-8");
                document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                savePDF(document.html(), rootFolder + "/" + unqValue);
            }else  if("CSV".equalsIgnoreCase(repFormat)){
                unqValue = "reports/" + ExportService.randomStr()   +".csv";
                String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
                String rootFolder = absPath.substring(0,absPath.length()-22);
                createCSV(transReportDataList,rootFolder + "/" + unqValue);

            }
            Resource resource = new ClassPathResource(unqValue);
            return  resource ;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }
    }


}
