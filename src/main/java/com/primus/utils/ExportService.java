package com.primus.utils;

import com.primus.stock.master.model.ReportData;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExportService {

    private String randomStr()
    {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString ;

    }

    public Resource exportToExcel(List<Map> dataList,String sheetName,
                                  Map<String,String> titleKeyMap) throws  Exception
    {
        HSSFWorkbook workbook = new HSSFWorkbook ();
        HSSFSheet spreadsheet
                = workbook.createSheet(sheetName);
        HSSFRow rowhead = spreadsheet.createRow((short)0);
        int count=0;
        for (Map.Entry<String,String> entry : titleKeyMap.entrySet()) {
            rowhead.createCell(count++).setCellValue(entry.getKey());
        }
        AtomicInteger integer = new AtomicInteger(1) ;
        for (Map data : dataList) {
            HSSFRow row = spreadsheet.createRow((short)integer.getAndAdd(1));
            count=0;
            for (Map.Entry<String,String> entry : titleKeyMap.entrySet()) {
                if (data.get(entry.getValue()) instanceof  String && NumberUtils.isNumber((String)data.get(entry.getValue())))
                    row.createCell(count++).setCellValue(Long.parseLong(String.valueOf(data.get(entry.getValue()))));
                else if (data.get(entry.getValue()) instanceof  String)
                   row.createCell(count++).setCellValue(String.valueOf(data.get(entry.getValue())));
                else if (data.get(entry.getValue()) instanceof  Double)
                    row.createCell(count++).setCellValue((Double)data.get(entry.getValue()));
                else if (data.get(entry.getValue()) == null )
                    row.createCell(count++).setCellValue("");
            }
        }
        String unqValue = "reports/" + randomStr()   +".xls";
        String absPath = ResourceUtils.getFile("classpath:application.properties").getAbsolutePath();
        String rootFolder = absPath.substring(0,absPath.length()-22);
        FileOutputStream fileOut = new FileOutputStream(rootFolder
            +  "/" + unqValue );
        workbook.write(fileOut);
        fileOut.close();
        Resource resource = new ClassPathResource(unqValue);
        return  resource ;

    }

}
