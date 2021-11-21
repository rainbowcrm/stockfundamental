package com.primus.stocktransaction.dao;

import com.primus.stock.master.model.StocksMaster;
import com.primus.stocktransaction.model.StockTransaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class StockTransactionDAO {

    @PersistenceContext
    protected EntityManager em;

    public List<StockTransaction> getData(Date from , Date to  ) {
        Query query =  em.createQuery("from StockTransaction  where transDate>=? and transDate <= ? order by  transDate")  ;
        query.setParameter(1,from);
        query.setParameter(2,to);

        List<StockTransaction> ans = query.getResultList();
        return ans;
    }
}
