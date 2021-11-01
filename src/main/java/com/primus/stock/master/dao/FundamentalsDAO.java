package com.primus.stock.master.dao;

import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class FundamentalsDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void create(FundamentalData model) {
        em.merge(model);
    }

    @Transactional
    public void update(FundamentalData model) {
        em.merge(model);
    }


    public List<FundamentalData> getAllFundamentals (  )
    {
        Query query =  em.createQuery("from FundamentalData "  );
        List<FundamentalData> ans = query.getResultList();
        return ans;
    }


}
