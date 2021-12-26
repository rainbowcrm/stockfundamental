package com.primus.reports.service;

import com.primus.common.BusinessContext;
import com.primus.common.CommonErrorCodes;
import com.primus.common.PrimusError;
import com.primus.reports.data.TransReportData;
import com.primus.stock.master.dao.StockMasterDAO;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.utils.ExportService;
import com.primus.utils.MathUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterDAO stockMasterDAO ;


    private void savePDF(String xhtml,String pdfNAME) throws PrimusError
    {
        try (OutputStream outputStream = new FileOutputStream(pdfNAME)) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(xhtml);
            renderer.layout();
            renderer.createPDF(outputStream);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.PDF_COULDNOTBE_GEN, "Pdf could not be generated");
        }
    }

    private String  createHTML(List<TransReportData> transReportDataList)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML><HEAD><TITLE>Trade Details</TITLE></HEAD>");
        stringBuffer.append("<BODY>");
        stringBuffer.append("<TABLE WIDTH='90%'>");
        stringBuffer.append("<TR>");
      //  stringBuffer.append("<TH>Sector</TH>"  );
        stringBuffer.append("<TH>Security</TH>"  );
        stringBuffer.append("<TH>Industry</TH>"  );
        stringBuffer.append("<TH>Group</TH>"  );
        stringBuffer.append("<TH>Cap Size</TH>" );
        stringBuffer.append("<TH>Open Price</TH>");
        stringBuffer.append("<TH>Close Price</TH>");
        stringBuffer.append("<TH>Change%</TH>");
        stringBuffer.append("</TR>");

        String currSector = "";
        for (TransReportData transReportData : transReportDataList )
        {

            if (!currSector.equalsIgnoreCase(transReportData.getSector()))
            {
                stringBuffer.append("<TR>");
                stringBuffer.append("<TD colspan='4'> <h3> Sector : " +  transReportData.getSector() + " </h3></TD>");
                List<TransReportData> subList = transReportDataList.stream().filter( entry -> {
                   return entry.getSector().equalsIgnoreCase(transReportData.getSector() )?true:false;
                }).collect(Collectors.toList());
                stringBuffer.append("<TD colspan='3'> <h3> No Shares : " + subList.size()  + " </h3></TD>");

                stringBuffer.append("</TR>");
                currSector=transReportData.getSector();

            }
            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>" +  transReportData.getSecurity() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getIndustry() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getGroup() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getMarketCapGroup() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getOpeningPrice() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getFinalPricde() + "</TD>");
            stringBuffer.append("<TD><B>" +  transReportData.getChange() + "</B></TD>");
            stringBuffer.append("</TR>");

        }

        stringBuffer.append("</TABLE>");
        stringBuffer.append("</HTML>");
        return stringBuffer.toString();

    }


    private static void sort(List<TransReportData> list)
    {

        list.sort((o1, o2)
                -> o1.getSector().compareTo(
                o2.getSector()));
    }

    public Resource generateReport(String fromDateS, String toDateS,String groupBy,BusinessContext context) throws PrimusError
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = simpleDateFormat.parse(fromDateS);
            Date toDate = simpleDateFormat.parse(toDateS);
            List<TransReportData> transReportDataList = generateReport(fromDate, toDate);
            sort(transReportDataList);
            String xhtml = createHTML(transReportDataList);
            String unqValue = "reports/" + ExportService.randomStr()   +".pdf";
            String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
            String rootFolder = absPath.substring(0,absPath.length()-22);
            /*FileOutputStream op = new FileOutputStream(rootFolder + "/" + unqValue);
            op.write(xhtml.getBytes());
            op.close();*/
            Document document = Jsoup.parse(xhtml, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            savePDF(document.html(),rootFolder + "/" + unqValue);
            Resource resource = new ClassPathResource(unqValue);
            return  resource ;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }

    }

    private List<TransReportData> generateReport(Date fromDate, Date toDate) throws PrimusError
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = simpleDateFormat.parse("01-06-2021");
            if (fromDate.before(startDate)) {
                throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
            }
            if (toDate.after(new Date())) {
                throw new PrimusError(CommonErrorCodes.TO_DATE_WRONG, "Start Date cannot be after current day");
            }
            List<StockTransaction> stockTransactionList = stockTransactionDAO.getData(fromDate, toDate);
            List<TransReportData> transReportDataList = new ArrayList<>();
            List<StocksMaster> stocksMasterList = stockMasterDAO.listAllTrackedData();
            for (StocksMaster stocksMaster : stocksMasterList) {
                List<StockTransaction> indTransactionList = stockTransactionList.stream().filter( stockTransaction ->
                {  return stockTransaction.getStocksMaster().getBseCode().equalsIgnoreCase(stocksMaster.getBseCode())?true:false; })
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(indTransactionList)){
                    TransReportData transReportData = new TransReportData(stocksMaster.getSecurityName(),stocksMaster.getIndustry(),
                            stocksMaster.getGroupC(),stocksMaster.getMarketGroup(),stocksMaster.getSector());
                    transReportData.setOpeningPrice(indTransactionList.get(0).getOpenPrice());
                    transReportData.setFinalPricde(indTransactionList.get(indTransactionList.size()-1).getClosePrice());
                    double change = MathUtil.perChange(indTransactionList.get(0).getOpenPrice(),
                            indTransactionList.get(indTransactionList.size()-1).getClosePrice());
                    transReportData.setChange(change);
                    transReportDataList.add(transReportData);
                }

            }
            return transReportDataList;
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }



    }
}
