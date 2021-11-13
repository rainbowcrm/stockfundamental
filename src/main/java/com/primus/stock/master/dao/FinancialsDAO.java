package com.primus.stock.master.dao;

import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import org.springframework.stereotype.Component;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class FinancialsDAO {
    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void create(FinancialData model) {
        em.merge(model);
    }

    @Transactional
    public void update(FinancialData model) {
        em.merge(model);
    }

    public List<FinancialData> getAllFundamentals (  )
    {
        Query query =  em.createQuery("from FinancialData "  );
        List<FinancialData> ans = query.getResultList();
        return ans;
    }


}
