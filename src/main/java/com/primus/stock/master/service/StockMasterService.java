package com.primus.stock.master.service;

import com.primus.common.LogWriter;
import com.primus.stock.api.service.APIService;
import com.primus.stock.master.dao.StockMasterDAO;
import com.primus.stock.master.model.StocksMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StockMasterService {

    @Autowired
    StockMasterDAO stockMasterDAO ;

    @Autowired
    APIService apiService;

    public List<StocksMaster>  getAllTrackedStocks(String groupC)
    {
        return stockMasterDAO.listAllTrackedData(groupC);
    }

    public List<StocksMaster> getAllStocks (  ){
        return  stockMasterDAO.getAllStocks() ;
    }

    public List<StocksMaster> getStocksForDashBoard (  ){
        return  stockMasterDAO.getStocksForDashboard() ;
    }


    public List<StocksMaster> getAllTrackedStocks (  ){
        return  stockMasterDAO.getAllTrackedStocks() ;
    }

    public void updateMarketCap()
    {
        List<StocksMaster> trackedStocks = stockMasterDAO.getAllTrackedStocks();
        for (StocksMaster stocksMaster : trackedStocks) {
            try {
                if(stocksMaster.getTracked() == true && stocksMaster.getMarketCap() == null ) {
                    Map<String, Object> map = apiService.getMarketCap(stocksMaster.getBseCode());
                    String fullMarketCap = (String) map.get("MktCapFull");
                    String ffMarketCap = (String) map.get("MktCapFF");
                    Double marketCap = Double.parseDouble(fullMarketCap.replace(",", ""));
                    Double marketCapff = Double.parseDouble(ffMarketCap.replace(",", ""));
                    String capGroup;
                    if (marketCap > 20000)
                        capGroup = "L";
                    else if (marketCap > 5000)
                        capGroup = "M";
                    else
                        capGroup = "S";
                    stocksMaster.setMarketCap(marketCap);
                    stocksMaster.setMarketCapFF(marketCapff);
                    stocksMaster.setMarketGroup(capGroup);
                    System.out.println(stocksMaster.getBseCode() + ":::::" + marketCap);
                    stockMasterDAO.update(stocksMaster);
                    Thread.sleep(100);
                }
            }catch (Exception ex)
            {
                LogWriter.logException("Ex in StockMasterService" ,this.getClass(),ex);
            }
        }
    }


}
