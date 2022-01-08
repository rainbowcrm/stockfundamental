package com.primus.ui.service;

import com.primus.stock.master.dao.FundamentalsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import java.util.HashMap;


@Service
public class LookupService {

    @Autowired
    FundamentalsDAO fundamentalsDAO;

    public Map<String,String> lookupVals(String lookupType, String searchString) {
        if ("Industry".equalsIgnoreCase(lookupType))
            return lookupIndustry(searchString);
        else if("Sector".equalsIgnoreCase(lookupType))
            return lookupSector(searchString);
        else if("Stock".equalsIgnoreCase(lookupType))
            return lookupStock(searchString);
        return new HashMap<>();
    }

    public Map<String,String> lookupIndustry(String searchString) {
        Map<String,String> ret = new HashMap<>();
        List<String> vals = fundamentalsDAO.getDistinctIndustry(searchString);
        for (String val : vals) {
            ret.put(val,val);
        }
        return ret;
    }

    public Map<String,String> lookupSector(String searchString) {
        Map<String,String> ret = new HashMap<>();
        List<String> vals = fundamentalsDAO.getDistinctSector(searchString);
        for (String val : vals) {
            ret.put(val,val);
        }
        return ret;
    }


    public Map<String,String> lookupStock(String searchString)
    {
        Map<String,String> ret = new HashMap<>();
        List<String> vals = fundamentalsDAO.getDistinctStock(searchString);
        for (String val : vals) {
            ret.put(val,val);
        }
        return ret;
    }
}
