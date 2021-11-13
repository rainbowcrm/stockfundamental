package com.primus.stock.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.stock.api.model.ComHeaderData;
import com.primus.stock.api.model.ScripHeaderData;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class APIService {

    public Map<String,Object> getCompanyHeader(String scripCode) throws  Exception
    {
        String api = "https://api.bseindia.com/BseIndiaAPI/api/ComHeader/w?quotetype=EQ&scripcode=" +  scripCode + "&seriesid="  ;
        URL obj = new URL(api);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        ObjectMapper objectMapper =  new ObjectMapper();
        return objectMapper.readValue(response.toString(), Map.class);

    }

    public Map<String,Object> getScripHeaderData (String scripCode) throws Exception
    {
        String api = "https://api.bseindia.com/BseIndiaAPI/api/getScripHeaderData/w?Debtflag=&scripcode=" +  scripCode + "&seriesid="  ;
        URL obj = new URL(api);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        ObjectMapper objectMapper =  new ObjectMapper();
        return objectMapper.readValue(response.toString(),Map.class);

    }

    public Map<String,Object> getFinancialData (String scripCode) throws Exception
    {
        String api = "https://api.bseindia.com/BseIndiaAPI/api/GetReportNewFor_Result/w?scripcode=" +  scripCode   ;
        URL obj = new URL(api);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        ObjectMapper objectMapper =  new ObjectMapper();
        return objectMapper.readValue(response.toString(),Map.class);

    }

    public Map<String,Object> getDividentData (String scripCode,Integer year) throws Exception
    {
        String api = "https://api.bseindia.com/BseIndiaAPI/api/CorporateAction/w?scripcode=" +  scripCode   ;
        URL obj = new URL(api);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        ObjectMapper objectMapper =  new ObjectMapper();
        return objectMapper.readValue(response.toString(),Map.class);

    }


}
