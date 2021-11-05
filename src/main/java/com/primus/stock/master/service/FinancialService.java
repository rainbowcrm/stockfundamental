package com.primus.stock.master.service;

import com.primus.stock.api.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class FinancialService {

    @Autowired
    APIService apiService ;


    private String formXML(String input)
    {
        String fPass = input.substring(0,input.indexOf("<a"));
        String sPass = fPass.replace("colspan=7"," ");
        String finalVa = "<root>" + sPass + "</td></tr> </tbody></root>";
        System.out.println(finalVa);
        return finalVa ;
    }

    private String getValue(Node node,int row)
    {
        Node revenueNode =  node.getChildNodes().item(row);
        Node firstColNode =  revenueNode.getChildNodes().item(1);
        String value = firstColNode.getChildNodes().item(0).getNodeValue();
        return value ;
    }

    public  void saveFinancialData(String scripCode)
    {
        try {
            Map<String,Object> responseData = apiService.getFinancialData(scripCode);
            Object finContent = responseData.get("QtlyinCr");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String xml = formXML((String) finContent);
            Document doc = db.parse(new InputSource(new java.io.StringReader(xml)));
            NodeList nodeList = doc.getElementsByTagName("tbody");
            Node node = nodeList.item(0);
            //System.out.println(nodeValue);
            String revenue = getValue(node,1);
            String expenditure = getValue(node,4);
            String netProfit= getValue(node,10);
            String equity = getValue(node,11);

            System.out.println(revenue +" " + expenditure + " "+ netProfit + " " + equity);


        }catch (Exception ex)
        {
                ex.printStackTrace();

        }

    }
}
