package com.primus.main;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.sound.midi.SysexMessage;

public class TestXMLParsing {

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.bseindia.com/stock-share-price/kriti-industries-(india)-ltd/kritiind/526423/financials-results/").get();
            System.out.println(doc);
           // doc.body().getElementById()

            //https://api.bseindia.com/BseIndiaAPI/api/getScripHeaderData/w?Debtflag=&scripcode=526423&seriesid=
            //https://api.bseindia.com/BseIndiaAPI/api/GetReportNewFor_Result/w?scripcode=526423
            //https://api.bseindia.com/BseIndiaAPI/api/ComHeader/w?quotetype=EQ&scripcode=526423&seriesid=
        }catch (Exception ex)
        {

        }
    }
}
