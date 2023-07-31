package com.primus.webscrap.fundamentals;

import com.primus.common.LogWriter;
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


public class BSEDailyTransactionSearch extends  Thread{





    public StockTransaction getDailyTransaction(List<StocksMaster> stocksMasterList, String chromeDriver, StockTransactionDAO stockTransactionDAO,
                                                FundamentalService fundamentalService)
    {

            System.setProperty("webdriver.chrome.driver", chromeDriver);
            ChromeOptions options = new ChromeOptions();
            //add the headless argument
            options.addArguments("headless");
            options.addArguments("--remote-allow-origins=*");
            WebDriver webDriver = new ChromeDriver(options);
        try {
            hitHomeScreen(webDriver);
            for (StocksMaster stocksMaster : stocksMasterList ) {
                String scripCode = stocksMaster.getApiCode();
                try {
                    StockTransaction stockTransaction = getStockTransactionData(scripCode, webDriver);
                    if (stockTransaction == null)
                        continue;
                    stockTransaction.setApi_code(stocksMaster.getApiCode());
                    stockTransaction.setSecurity_name(stocksMaster.getSecurityName());
                    stockTransaction.setStocksMaster(stocksMaster);
                    LogWriter.debug(stockTransaction.toString());
                    stockTransactionDAO.update(stockTransaction);
                    FundamentalData fundamentalData = fundamentalService.getFundamentalData(stocksMaster.getBseCode());
                    if (fundamentalData != null) {
                        fundamentalData.setCurPrice(stockTransaction.getClosePrice());
                        LogWriter.debug(fundamentalData.toString());
                        fundamentalService.updateFundamentals(fundamentalData);
                    }
                }catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
            return null;
        }catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            webDriver.quit();
        }
        return null;

    }

    private void hitHomeScreen(WebDriver webDriver )
    {
        webDriver.manage().window().maximize();
        webDriver.get("https://www.bseindia.com/");
        webDriver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);

        //getquotesearch
    }



    private StockTransaction getStockTransactionData(String scriptCode, WebDriver webDriver)
    {
        StockTransaction stockTransaction = new StockTransaction() ;
        String xPath = "//input[@id='getquotesearch']";   //name="q"
        webDriver.findElement(By.xpath(xPath)).sendKeys(scriptCode );
        webDriver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
        webDriver.findElement(By.xpath(xPath)).sendKeys("\n" );
        String xPathClosePrice = "//strong[@id='idcrval']";
        String closePrice = webDriver.findElement(By.xpath(xPathClosePrice)).getText();
        stockTransaction.setClosePrice(Double.parseDouble(closePrice));
        List<WebElement> webElementList = webDriver.findElements(By.xpath("//table/tbody/tr/td"));
        for (int i = 0 ; i <  webElementList.size() ; i ++) {
            WebElement currentElement = webElementList.get(i) ;
            String innerHTML = currentElement.getAttribute("innerHTML");
            if ( innerHTML != null && innerHTML.trim().equalsIgnoreCase("Open"))  {
                String openPrice = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                stockTransaction.setOpenPrice(Double.parseDouble(openPrice));
            }
            if ( innerHTML != null && innerHTML.trim().equalsIgnoreCase("High"))  {
                String price = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                stockTransaction.setHighPrice(Double.parseDouble(price));
            }

            if ( innerHTML != null && innerHTML.trim().equalsIgnoreCase("Low"))  {
                String price = webElementList.get(i+1).getAttribute(("innerHTML")) ;
                stockTransaction.setLowPrice(Double.parseDouble(price));
            }
        }
        stockTransaction.setTransDate(new Date());
        stockTransaction.setApi_code(scriptCode);

        return stockTransaction;


    }
}
