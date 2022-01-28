package com.primus.utils;

import com.primus.common.LogWriter;
import org.springframework.beans.factory.annotation.Autowired;
import com.primus.common.Configuration;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Service
public class SMSService {

    @Autowired
    Configuration configuration;



    public void sendSMS(String mobile, String otp){
        try {
            // Construct data
            String apiKey = "apikey=" + configuration.getSmsAPIKey();
            String message = "&message=" + "Your one time password is " + otp;
            String sender = "&sender=" + "Stock Sparrow";
            String numbers = "&numbers=" + mobile +"&template=1107160691313669232";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

           // return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            LogWriter.error(e);
        }
    }
}
