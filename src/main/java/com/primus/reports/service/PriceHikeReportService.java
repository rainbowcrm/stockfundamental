package com.primus.reports.service;

import com.primus.common.*;
import com.primus.common.datastructures.DataPair;
import com.primus.reports.data.TransReportData;
import com.primus.stock.master.dao.StockMasterDAO;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.utils.ExportService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.swing.text.html.HTMLDocument;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PriceHikeReportService extends ReportService{

    @Autowired
    Configuration configuration ;



    private static void sort(List<TransReportData> list)
    {
            list.sort((o1, o2)
                    -> o2.getChange().compareTo(
                    o1.getChange()));
    }

    private DataPair getRangeforChange(List<TransReportData> transReportDataList)
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
         return  new DataPair<Double>(minHike,maxHike);

    }

    private List<DataPair<Double>> getRangeValues(DataPair<Double>pair) {
        List<DataPair<Double>> range = new ArrayList<>();
        Double sep = new Double(5) ;
        if ( pair.getValue2() - pair.getValue1() > 200) {
            sep = new Double(10);
        }else if(pair.getValue2() - pair.getValue1() < 10 ) {
            sep = new Double(1);
        }
        Double index = pair.getValue2();
        while(index > pair.getValue1()) {
            range.add(new DataPair<>(index - sep,index));
            index-=sep;
        }
        return range;
    }

    private String  createHTML(List<TransReportData> transReportDataList,BusinessContext businessContext,
                               String fromDate, String toDate)
    {
        StringBuffer stringBuffer = new StringBuffer();
        DataPair<Double> dataPair = getRangeforChange(transReportDataList);
        List<DataPair<Double>> valueRange = getRangeValues(dataPair);
        stringBuffer.append("<HTML><HEAD><TITLE>Trade Details</TITLE></HEAD>");
        stringBuffer.append("<BODY>");
        stringBuffer.append(getHeader(businessContext,fromDate,toDate,"Growth By Rate"));
        stringBuffer.append("<TABLE WIDTH='90%'>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TH>Security</TH>"  );
        stringBuffer.append("<TH>Open Price</TH>");
        stringBuffer.append("<TH>Close Price</TH>");
        stringBuffer.append("<TH>Change%</TH>");
        stringBuffer.append("</TR>");

        Iterator<DataPair<Double>> it = valueRange.iterator();
        DataPair<Double> curDP = it.next();
        stringBuffer.append("<TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TD border= '1px solid' colspan='5'> <h4>   " +  curDP.getValue1()  + "% To " + curDP.getValue2() + "% </h4></TD>");
        stringBuffer.append("</TR>");
        stringBuffer.append("</TR>");
        int i = 1;
        for (TransReportData transReportData : transReportDataList )
        {

            if( transReportData.getChange()  < curDP.getValue1() ) {
                curDP = it.next();
                while(it.hasNext() && !(curDP.inRange(transReportData.getChange()))) {
                    curDP = it.next();
                }
                final DataPair fnDP = curDP;
                List <TransReportData> groupRecs = transReportDataList.stream().filter( data ->{
                    return fnDP.inRange(data.getChange())?true:false;
                }).collect(Collectors.toList());
                stringBuffer.append("<TR>");
                stringBuffer.append("<TD border= '1px solid' colspan='4'> <h4>   " +  curDP.getValue1()  + "% To " + curDP.getValue2() + "% Count:" + groupRecs.size() + " </h4></TD>");
                stringBuffer.append("</TR>");

            }

            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>" +  (i++) + "</TD>");
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
                unqValue =  ExportService.randomStr()   +".pdf";
               // String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
                String rootFolder = configuration.getReportFolder();
                Document document = Jsoup.parse(xhtml, "UTF-8");
                document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                savePDF(document.html(), rootFolder + "/" + unqValue);
            }else  if("CSV".equalsIgnoreCase(repFormat)){
                unqValue =  ExportService.randomStr()   +".csv";
                //String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
                String rootFolder = configuration.getReportFolder();
                createCSV(transReportDataList,rootFolder + "/" + unqValue);

            }
            Resource resource = new FileSystemResource(configuration.getReportFolder()  + unqValue);
            return  resource ;
        }catch (Exception ex){
            LogWriter.logException("Ex in PriceeportService" ,this.getClass(),ex);
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }
    }


}
