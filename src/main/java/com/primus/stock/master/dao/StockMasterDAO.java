package com.primus.stock.master.dao;

import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class StockMasterDAO {

    @PersistenceContext
    protected EntityManager em;


    public List<StocksMaster> listAllTrackedData(String groupC )
    {
        Query query =  em.createQuery("from StocksMaster where  useJavaAPI = true  and id > 0 and groupC='" + groupC + "'"  );
        List<StocksMaster> ans = query.getResultList();
        return ans;
    }

    public List<StocksMaster> listData(int from , int to , String whereCondition, String orderby ) {
        Query query =  em.createQuery("from StocksMaster "    +  ((StringUtils.isEmpty(whereCondition))?"":whereCondition) +
                " " + ((StringUtils.isEmpty((orderby))?"": (" order by " + orderby) ))) ;
        query.setFirstResult(from);
        query.setMaxResults(to-from);
        List<StocksMaster> ans = query.getResultList();
        return ans;
    }

    public List<StocksMaster> getAllStocks (  )
    {
        Query query =  em.createQuery("from StocksMaster "  );
        List<StocksMaster> ans = query.getResultList();
        return ans;
    }



}

