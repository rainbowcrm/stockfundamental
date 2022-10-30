package com.primus.reports.service;

import com.opencsv.CSVWriter;
import com.primus.common.*;
import com.primus.common.datastructures.DataPair;
import com.primus.reports.data.TransReportData;
import com.primus.stock.master.dao.StockMasterDAO;
import com.primus.stock.master.model.DividentHistory;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.DividentHistoryService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import com.primus.utils.ExportService;
import com.primus.utils.MathUtil;
import org.apache.commons.collections.map.HashedMap;
import org.ehcache.xml.model.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
    Configuration configuration ;

    @Autowired
    StockTransactionDAO stockTransactionDAO;

    @Autowired
    StockMasterDAO stockMasterDAO ;

    @Autowired
    DividentHistoryService dividentHistoryService;


    protected void savePDF(String xhtml,String pdfNAME) throws PrimusError
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
            LogWriter.logException("Ex in ReportService" ,this.getClass(),ex);
            throw new PrimusError(CommonErrorCodes.PDF_COULDNOTBE_GEN, "Pdf could not be generated");
        }
    }

    private double findMedian(List<TransReportData> transReportDataList)
    {
        List<Double> doubleList = new ArrayList<Double>();
        for (TransReportData transReportData : transReportDataList) {
            doubleList.add(transReportData.getChange());
        }

        return MathUtil.getMedian(doubleList);
    }

    protected void createCSV(List<TransReportData> transReportDataList,String csvFileName) throws PrimusError
    {

        try {
            File file = new File(csvFileName);
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = { "Sector", "Industry", "Security" , "Group","Cap Size" ,"Open" ,"Close","Incr","Median","Rel Deviation"  };
            writer.writeNext(header);
            for (TransReportData transReportData : transReportDataList) {
                String[] row= {transReportData.getSector(),transReportData.getIndustry(),transReportData.getSecurity(),transReportData.getGroup(),
                        transReportData.getMarketCapGroup(),
                        String.valueOf(transReportData.getOpeningPrice()),String.valueOf(transReportData.getFinalPricde()),
                        String.valueOf(transReportData.getChange()),String.valueOf(transReportData.getMedian()) , String.valueOf(transReportData.getRelDeviation()) };
                writer.writeNext(row);
                }
            writer.close();
        }catch (Exception ex){
            LogWriter.logException("Ex in ReportService" ,this.getClass(),ex);
            throw new PrimusError(CommonErrorCodes.CSV_COULDNOTBE_GEN,"CSV file could not be generated");

        }
    }

    private List<TransReportData> getSubList(List<TransReportData> transReportDataList , TransReportData transReportData, String groupbBy)
    {
        List<TransReportData> subList = transReportDataList.stream().filter( entry -> {
            return entry.getSector().equalsIgnoreCase(transReportData.getSector() )?true:false;
        }).collect(Collectors.toList());
        return subList;
    }

    private boolean hasGroupFCChanged(String currGroupValue,TransReportData transReportData,String groupBy)
    {
        if("Sector".equalsIgnoreCase(groupBy) && !currGroupValue.equalsIgnoreCase(transReportData.getSector())) return true;
        if("Industry".equalsIgnoreCase(groupBy) && !currGroupValue.equalsIgnoreCase(transReportData.getIndustry())) return true;
        if("Group".equalsIgnoreCase(groupBy) && !currGroupValue.equalsIgnoreCase(transReportData.getGroup())) return true;
        if("marketCap".equalsIgnoreCase(groupBy) && !currGroupValue.equalsIgnoreCase(transReportData.getMarketCapGroup())) return true;
        return false;
    }

    private String getGroupHeader(TransReportData transReportData,String groupBy)
    {
        if("Sector".equalsIgnoreCase(groupBy) ) return "Sector : " + transReportData.getSector();
        if("Industry".equalsIgnoreCase(groupBy) )return "Industry : " +  transReportData.getIndustry();
        if("Group".equalsIgnoreCase(groupBy) ) return "Group : " + transReportData.getGroup();
        if("marketCap".equalsIgnoreCase(groupBy) )  return " MCap Size : " + transReportData.getMarketCapGroup();
        return "";
    }

    protected String getHeader(BusinessContext businessContext,String fromDate, String toDate,String title)
    {

        StringBuffer header = new StringBuffer("<H3>");
        header.append("Stock Sparrow</H3>");
        header.append("<H4>");
        header.append(title + "</H4>");
        header.append("<H4>");
        header.append("Trade From:  " + fromDate + " To: " + toDate + "</H4>");
        header.append("<br>Report run by " + businessContext.getUser().getFirstName() + " "+ businessContext.getUser().getLastName() +"" );
        return header.toString();

    }

    private String  createHTML(List<TransReportData> transReportDataList,String groupBy,BusinessContext businessContext,
                               String fromDate, String toDate)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<HTML><HEAD><TITLE>Trade Details</TITLE></HEAD>");
        stringBuffer.append("<BODY>");
        stringBuffer.append(getHeader(businessContext,fromDate,toDate,"Transaction Summary"));
        stringBuffer.append("<TABLE WIDTH='90%'>");
        stringBuffer.append("<TR>");
        if (!"Sector".equalsIgnoreCase(groupBy)) stringBuffer.append("<TH>Sector</TH>"  );
        stringBuffer.append("<TH>Security</TH>"  );
        if (!"Industry".equalsIgnoreCase(groupBy)) stringBuffer.append("<TH>Industry</TH>"  );
        if (!"Group".equalsIgnoreCase(groupBy)) stringBuffer.append("<TH>Group</TH>"  );
        if (!"marketCap".equalsIgnoreCase(groupBy)) stringBuffer.append("<TH>Cap Size</TH>" );
        stringBuffer.append("<TH>Open Price</TH>");
        stringBuffer.append("<TH>Close Price</TH>");
        stringBuffer.append("<TH>Change%</TH>");
        stringBuffer.append("</TR>");

        String currGroup = "";
        for (TransReportData transReportData : transReportDataList )
        {

            if (hasGroupFCChanged(currGroup,transReportData,groupBy))
            {
                stringBuffer.append("<TR>");
                stringBuffer.append("<TD border= '1px solid' colspan='4'> <h3>   " +  getGroupHeader(transReportData,groupBy) + " </h3></TD>");
                List<TransReportData> subList = getSubList(transReportDataList,transReportData,groupBy);
                double medianChange = findMedian(subList);
                stringBuffer.append("<TD border= '1px solid' colspan='3'> <h4> No Shares : " + subList.size()  + " </h4>");
                stringBuffer.append("<h4> Median Change : " + medianChange  + " </h4></TD>");
                stringBuffer.append("</TR>");
                if("Sector".equalsIgnoreCase(groupBy) ) currGroup= transReportData.getSector();
                if("Industry".equalsIgnoreCase(groupBy) ) currGroup= transReportData.getIndustry();
                if("Group".equalsIgnoreCase(groupBy) ) currGroup = transReportData.getGroup();
                if("marketCap".equalsIgnoreCase(groupBy) )  currGroup = transReportData.getMarketCapGroup();
            }
            stringBuffer.append("<TR>");
            if (!"Sector".equalsIgnoreCase(groupBy))  stringBuffer.append("<TD>" +  transReportData.getSector() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getSecurity().replace("&#160;"," ") + "</TD>");
            if (!"Industry".equalsIgnoreCase(groupBy))  stringBuffer.append("<TD>" +  transReportData.getIndustry() + "</TD>");
            if (!"Group".equalsIgnoreCase(groupBy)) stringBuffer.append("<TD>" +  transReportData.getGroup() + "</TD>");
            if (!"marketCap".equalsIgnoreCase(groupBy)) stringBuffer.append("<TD>" +  transReportData.getMarketCapGroup() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getOpeningPrice() + "</TD>");
            stringBuffer.append("<TD>" +  transReportData.getFinalPricde() + "</TD>");
            stringBuffer.append("<TD><B>" +  transReportData.getChange() + "</B></TD>");
            stringBuffer.append("</TR>");
        }
        stringBuffer.append("</TABLE>");
        stringBuffer.append("</HTML>");
        return stringBuffer.toString();

    }


    private static void sort(List<TransReportData> list,String groupField)
    {
        if("Sector".equalsIgnoreCase(groupField)) {
            list.sort((o1, o2)
                    -> o1.getSector().compareTo(
                    o2.getSector()));
        }else if("Industry".equalsIgnoreCase(groupField)) {
            list.sort((o1, o2)
                    -> o1.getIndustry().compareTo(
                    o2.getIndustry()));
        }else if("Group".equalsIgnoreCase(groupField)) {
            list.sort((o1, o2)
                    -> o1.getGroup().compareTo(
                    o2.getGroup()));
        }else if("marketCap".equalsIgnoreCase(groupField)) {
            list.sort((o1, o2)
                    ->  o1.getMarketCapGroup().compareTo(
                    o2.getMarketCapGroup()));
        }
    }

    public Resource generateReport(String fromDateS, String toDateS,String groupBy, String repFormat,BusinessContext context) throws PrimusError
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = simpleDateFormat.parse(fromDateS);
            Date toDate = simpleDateFormat.parse(toDateS);
            List<TransReportData> transReportDataList = generateReport(fromDate, toDate);
            sort(transReportDataList,groupBy);
            String unqValue ="";
            if("PDF".equalsIgnoreCase(repFormat)) {
                String xhtml = createHTML(transReportDataList,groupBy,context,fromDateS,toDateS);
                unqValue =  ExportService.randomStr()   +".pdf";
             //   String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
                String rootFolder = configuration.getReportFolder();
                Document document = Jsoup.parse(xhtml, "UTF-8");
                document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                savePDF(document.html(), rootFolder + "/" + unqValue);
            }else  if("CSV".equalsIgnoreCase(repFormat)){
                unqValue =  ExportService.randomStr()   +".csv";
             //   String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
                String rootFolder = configuration.getReportFolder();
                createCSV(transReportDataList,rootFolder + "/" + unqValue);

            }
            Resource resource = new FileSystemResource(configuration.getReportFolder()  + unqValue);
            return  resource ;

        }catch (Exception ex){
            LogWriter.logException("Ex in ReportService" ,this.getClass(),ex);
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }

    }

    private DataPair<Double,Double> findAdjacentPrices(Date exDate, List<StockTransaction> stockTransactionList)  {

      int dateDiffMin= Integer.MAX_VALUE ;
      Date closestDate = null ;
      int closestIndex = 0 ;
      Double beforePrice =0d;
      Double afterPrice = 0d ;

      for (int i  = 0 ; i <  stockTransactionList.size(); i  ++)
      {
          StockTransaction stockTransaction =  stockTransactionList.get(i);
          if (closestDate == null )
          {
              closestDate = stockTransaction.getTransDate();
          }
          int diff = (int) (Math.abs(exDate.getTime() - closestDate.getTime())/1000*24*3600);
          if ( diff < dateDiffMin)  {
              dateDiffMin = diff ;
              closestDate = stockTransaction.getTransDate() ;
              closestIndex = i;
          }
      }

      if ( closestIndex > 0 ) {
          beforePrice = stockTransactionList.get(closestIndex-1).getClosePrice();

      }
      if ( closestIndex < stockTransactionList.size()-1)
      {
          afterPrice = stockTransactionList.get(closestIndex+1).getClosePrice();
      }else
      {
          afterPrice = stockTransactionList.get(closestIndex).getClosePrice();
      }

    return new DataPair<Double, Double>(beforePrice,afterPrice) ;

    }

    protected List<TransReportData> generateDividentReport(Date fromDate, Date toDate) throws PrimusError
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
            List<DividentHistory> dividentHistoryList = dividentHistoryService.getDividentHistory(fromDate,toDate);
            for (DividentHistory dividentHistory : dividentHistoryList) {
                List<String> reportContent = new ArrayList<>();
                StocksMaster selStock = stocksMasterList.stream().filter( stocksMaster ->
                { return stocksMaster.getBseCode().equalsIgnoreCase(dividentHistory.getBseCode())?true:false;}).findFirst().orElse(null);
                reportContent.add(selStock.getSecurityName());
                reportContent.add(selStock.getSector());
                reportContent.add(selStock.getIndustry());
                reportContent.add(selStock.getMarketGroup());
                List<StockTransaction> selTransactions =  stockTransactionList.stream().filter( stockTransaction ->
                { return stockTransaction.getApi_code().equalsIgnoreCase(dividentHistory.getBseCode())?true:false; }).collect(Collectors.toList());
                DataPair<Double,Double> dataPair = findAdjacentPrices(dividentHistory.getExDate(),selTransactions);
                reportContent.add(simpleDateFormat.format(dividentHistory.getExDate()));
                reportContent.add(String.valueOf(dividentHistory.getDivident()));
                reportContent.add(String.valueOf(dataPair.getValue1()));
                reportContent.add(String.valueOf(dataPair.getValue2()));
            }
            return transReportDataList;
        }catch (Exception ex) {
            LogWriter.logException("Ex in ReportService" ,this.getClass(),ex);
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }



    }

    public Resource getDividentReport(String fromDateS, String toDateS) throws Exception
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = simpleDateFormat.parse(fromDateS);
        Date toDate = simpleDateFormat.parse(toDateS);
        List<TransReportData> transReportDataList = generateDividentReport(fromDate, toDate);
        String unqValue =  ExportService.randomStr()   +".csv";
        String rootFolder = configuration.getReportFolder();
        createCSV(transReportDataList,rootFolder + "/" + unqValue);
        Resource resource = new FileSystemResource(configuration.getReportFolder()  + unqValue);
        return  resource ;
    }
    protected List<TransReportData> generateReport(Date fromDate, Date toDate) throws PrimusError
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
                    TransReportData transReportData = new TransReportData(stocksMaster.getSecurityName().replace("-&#160;"," "),stocksMaster.getIndustry(),
                            stocksMaster.getGroupC(),stocksMaster.getMarketGroup(),stocksMaster.getSector());
                    transReportData.setOpeningPrice(indTransactionList.get(0).getOpenPrice());
                    transReportData.setFinalPricde(indTransactionList.get(indTransactionList.size()-1).getClosePrice());
                    double change = MathUtil.perChange(indTransactionList.get(0).getOpenPrice(),
                            indTransactionList.get(indTransactionList.size()-1).getClosePrice());
                    List<Double> priceList = getPricesInList(indTransactionList) ;
                    transReportData.setRelDeviation(MathUtil.getRelStandardDeviation(priceList));
                    transReportData.setMedian(MathUtil.getMedian(priceList));
                    transReportData.setChange(change);
                    transReportDataList.add(transReportData);
                }

            }
            return transReportDataList;
        }catch (Exception ex) {
            LogWriter.logException("Ex in ReportService" ,this.getClass(),ex);
            throw new PrimusError(CommonErrorCodes.FROM_DATE_WRONG, "Start Date cannot be post 01-06-2021");
        }



    }
    private List<Double> getPricesInList(List<StockTransaction> indTransactionList)
    {
        List<Double> prices = new ArrayList<Double> ();
        for (StockTransaction stockTransaction : indTransactionList) {
            prices.add(stockTransaction.getClosePrice());
        }
        return prices;

    }

}
