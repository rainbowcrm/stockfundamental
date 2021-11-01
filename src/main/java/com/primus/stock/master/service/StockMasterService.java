package com.primus.stock.master.service;

import com.primus.stock.master.dao.StockMasterDAO;
import com.primus.stock.master.model.StocksMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMasterService {

    @Autowired
    StockMasterDAO stockMasterDAO ;

    public List<StocksMaster>  getAllTrackedStocks(String groupC)
    {
        return stockMasterDAO.listAllTrackedData(groupC);
    }

}
