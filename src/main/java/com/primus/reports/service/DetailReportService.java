package com.primus.reports.service;

import com.opencsv.CSVWriter;
import com.primus.common.BusinessContext;
import com.primus.common.CommonErrorCodes;
import com.primus.common.PrimusError;
import com.primus.reports.data.TransDetailReport;
import com.primus.reports.data.TransLine;
import com.primus.reports.data.TransReportData;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.utils.ExportService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DetailReportService extends ReportService{

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    private  TransDetailReport generateReport(Date fromDate, Date toDate,String stock) throws PrimusError {
        TransDetailReport transDetailReport = new TransDetailReport();
        transDetailReport.setSecurity(stock);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = simpleDateFormat.parse("01-01-2021");
            if (fromDate.before(startDate)) {
                throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-01-2021");
            }
            if (toDate.after(new Date())) {
                throw new PrimusError(CommonErrorCodes.TO_DATE_WRONG, "Start Date cannot be after current day");
            }

            List<StockTransaction> stockTransactionList = stockTransactionDAO.getDataForStock(fromDate,toDate,stock);
            for (StockTransaction stockTransaction : stockTransactionList) {
                TransLine transLine = new TransLine(stockTransaction.getTransDate(),stockTransaction.getOpenPrice(),stockTransaction.getClosePrice(),
                        stockTransaction.getLowPrice(),stockTransaction.getHighPrice(),stockTransaction.getVolume());
                transDetailReport.addTransLine(transLine);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.REPORT_GEN_FAILED,"Report could not be generated");
        }
        return transDetailReport;
    }

    private String  createHTML(TransDetailReport transDetailReport,BusinessContext businessContext,
                               String fromDate, String toDate)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML><HEAD><TITLE>Trade Details</TITLE></HEAD>");
        stringBuffer.append("<BODY>");
        stringBuffer.append(getHeader(businessContext,fromDate,toDate));
        stringBuffer.append("<TABLE WIDTH='90%'>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TH>Security:</TH>" + transDetailReport.getSecurity()  );
        stringBuffer.append("</TR>");
        stringBuffer.append("<TR>");
        stringBuffer.append("<TH>Date</TH>"  );
        stringBuffer.append("<TH>Open Price</TH>");
        stringBuffer.append("<TH>Low Price</TH>");
        stringBuffer.append("<TH>High Price</TH>");
        stringBuffer.append("<TH>Close Price</TH>");
        stringBuffer.append("<TH>Volume</TH>");
        stringBuffer.append("</TR>");
        String currGroup = "";
        for (TransLine transLine : transDetailReport.getTransLineList() )
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            stringBuffer.append("<TR>");
            stringBuffer.append("<TD>" + simpleDateFormat.format(transLine.getDate())+ "</TD>");
            stringBuffer.append("<TD>" +  transLine.getOpening() + "</TD>");
            stringBuffer.append("<TD>" +  transLine.getLow() + "</TD>");
            stringBuffer.append("<TD>" +  transLine.getHigh() + "</TD>");
            stringBuffer.append("<TD>" +  transLine.getClosing() + "</TD>");
            stringBuffer.append("<TD>" +  transLine.getVolume() + "</TD>");
            stringBuffer.append("</TR>");
        }
        stringBuffer.append("</TABLE>");
        stringBuffer.append("</HTML>");
        return stringBuffer.toString();

    }

    private void createCSV(TransDetailReport transDetailReport,String csvFileName) throws PrimusError
    {

        try {
            File file = new File(csvFileName);
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = { "Stock", "Date", "Open" , "Low","High" ,"Close" ,"Volume"  };
            writer.writeNext(header);
            for (TransLine transLine : transDetailReport.getTransLineList()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String[] row= {transDetailReport.getSecurity(),simpleDateFormat.format(transLine.getDate()),String.valueOf(transLine.getOpening()),
                String.valueOf(transLine.getLow()),String.valueOf(transLine.getHigh()),String.valueOf(transLine.getVolume())};
                writer.writeNext(row);
            }
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.CSV_COULDNOTBE_GEN,"CSV file could not be generated");

        }
    }

    public Resource generateReport(String fromDateS, String toDateS, String stock, String repFormat, BusinessContext context) throws PrimusError
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = simpleDateFormat.parse(fromDateS);
            Date toDate = simpleDateFormat.parse(toDateS);
            TransDetailReport transDetailReport = generateReport(fromDate, toDate, stock);
            String unqValue ="";
            if("PDF".equalsIgnoreCase(repFormat)) {
                String xhtml = createHTML(transDetailReport,context,fromDateS,toDateS);
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
                createCSV(transDetailReport,rootFolder + "/" + unqValue);

            }
            Resource resource = new ClassPathResource(unqValue);
            return  resource ;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }

    }
}
