package com.primus.stock.master.service;

import com.primus.stock.master.dao.DividentHistoryDAO;
import com.primus.stock.master.model.DividentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class DividentHistoryService {

    @Autowired
    DividentHistoryDAO dividentHistoryDAO;

    public void createDividentHistory(List<DividentHistory> dividentHistoryList)
    {
        for (DividentHistory dividentHistory : dividentHistoryList) {
            createDividentHistory(dividentHistory);
        }
    }

    public void createDividentHistory(DividentHistory dividentHistory) {
        DividentHistory existRecord = dividentHistoryDAO.getDividentsofCompany(dividentHistory.getBseCode(),dividentHistory.getExDate());
        if (existRecord == null)  {
            dividentHistoryDAO.create(dividentHistory);
        }else if (existRecord.getDivident() != dividentHistory.getDivident()) {
            existRecord.setDivident(dividentHistory.getDivident());
            dividentHistoryDAO.update(dividentHistory);
        }

    }

    public List<DividentHistory> getDividentHistory(Date fromDate,Date toDate)
    {
        return dividentHistoryDAO.getDividentHistory(fromDate,toDate);
    }



}
