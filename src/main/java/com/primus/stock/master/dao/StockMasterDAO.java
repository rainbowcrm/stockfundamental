package com.primus.stock.master.dao;

import com.primus.stock.master.model.StocksMaster;
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
        Query query =  em.createQuery("from StocksMaster where  useJavaAPI = true and groupC='" + groupC + "'"  );
        List<StocksMaster> ans = query.getResultList();
        return ans;
    }

}

