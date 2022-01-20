package com.primus.stock.master.service;

import com.primus.stock.master.dao.DailyUpdateDAO;
import com.primus.stock.master.model.DailyUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DailyUpdateService {
    @Autowired
    DailyUpdateDAO dailyUpdateDAO;

    public void updateDailyService(String groupX ,Date date)
    {
        dailyUpdateDAO.create(new DailyUpdate(groupX,date));
    }

    public void updateDailyService(DailyUpdate dailyUpdate)
    {
        dailyUpdateDAO.create(dailyUpdate);
    }

    public DailyUpdate getDailyService(String groupX, Date date)
    {
        return dailyUpdateDAO.getDailyUpdated(groupX,date);
    }

}


