package com.primus.webscrap.fundamentals;

import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import com.primus.stock.master.service.FundamentalService;
import com.primus.stocktransaction.dao.StockTransactionDAO;
import com.primus.stocktransaction.model.StockTransaction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BSEFundamentalsSearch {

    private void hitHomeScreen(WebDriver webDriver )
    {
        webDriver.manage().window().maximize();
        webDriver.get("https://www.bseindia.com/");
        webDriver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);

        //getquotesearch
    }

    public void saveFundamentalData(List<StocksMaster> stocksMasterList, String chromeDriver)
    {

        System.setProperty("webdriver.chrome.driver", chromeDriver);
        ChromeOptions options = new ChromeOptions();
        //add the headless argument
       // options.addArguments("headless");
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        try {
            hitHomeScreen(webDriver);
            /*for (StocksMaster stocksMaster : stocksMasterList) {
                String scripCode = stocksMaster.getApiCode();
            }*/
            getFundamentalData("500110",webDriver);
        }catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            webDriver.quit();
        }
    }

    private FundamentalData getFundamentalData(String scriptCode, WebDriver webDriver)
    {
        FundamentalData fundamentalData = new FundamentalData() ;
        String xPath = "//input[@id='getquotesearch']";   //name="q"
        webDriver.findElement(By.xpath(xPath)).sendKeys(scriptCode );
        webDriver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
        webDriver.findElement(By.xpath(xPath)).sendKeys("\n" );
        String pathFinancialsLink = "//a[@id='afi']";
        webDriver.findElement(By.xpath(pathFinancialsLink)).click();
        webDriver.findElement(By.partialLinkText("Results")).click();
        List<WebElement> webElementList = webDriver.findElements(By.xpath("//table/tbody/tr/td"));
        for (int i = 0 ; i <  webElementList.size() ; i ++) {
            WebElement currentElement = webElementList.get(i);
            String innerHTML = currentElement.getAttribute("innerHTML");
            if (innerHTML != null && innerHTML.trim().equalsIgnoreCase("(in Cr.)")) {
                String month = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                System.out.println("month" + month);
            }
            if (innerHTML != null && innerHTML.trim().equalsIgnoreCase("EPS")) {
                String eps = webElementList.get(i+3).getAttribute(("innerHTML")) ;
                System.out.println("eps" + eps);
            }

            if (innerHTML != null && innerHTML.trim().equalsIgnoreCase("PB")) {
                String pb = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                System.out.println("PB" + pb);
            }
            if (innerHTML != null && innerHTML.trim().equalsIgnoreCase("Revenue")) {
                String revenue = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                System.out.println("revenue" + revenue);
            }
            if (innerHTML != null && innerHTML.trim().equalsIgnoreCase("Expenditure")) {
                String expenditure = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                System.out.println("expenditure" + expenditure);
            }

            if (innerHTML != null && innerHTML.trim().equalsIgnoreCase("Net Profit")) {
                String netprofit = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                System.out.println("netprofit" + netprofit);
            }

            if (innerHTML != null && innerHTML.trim().equalsIgnoreCase("Equity")) {
                String Equity = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                System.out.println("Equity" + Equity);
            }

        }

        return fundamentalData;


    }

}
