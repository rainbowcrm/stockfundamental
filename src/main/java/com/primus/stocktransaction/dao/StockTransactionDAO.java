package com.primus.stocktransaction.dao;

import com.primus.stocktransaction.model.StockTransaction;
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
        Query query =  em.createQuery("from StockTransaction  where transDate>=?1 and transDate <= ?2 order by  transDate")  ;
        query.setParameter(1,from);
        query.setParameter(2,to);

        List<StockTransaction> ans = query.getResultList();
        return ans;
    }

    public List<StockTransaction> getDataForStock(Date from , Date to , String stock  ) {
        Query query =  em.createQuery("from StockTransaction  where transDate>=?1 and transDate <= ?2 and security_name =?3 order by  transDate")  ;
        query.setParameter(1,from);
        query.setParameter(2,to);
        query.setParameter(3,stock);

        List<StockTransaction> ans = query.getResultList();
        return ans;
    }

    @Transactional
    public void update(StockTransaction model) {
        em.merge(model);
    }
}
