package com.primus.stock.master.dao;

import com.primus.stock.master.model.FinancialData;
import com.primus.stock.master.model.FundamentalData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    public List<FinancialData> getAllFinancials (  )
    {
        Query query =  em.createQuery("from FinancialData "  );
        List<FinancialData> ans = query.getResultList();
        return ans;
    }

    public FinancialData getFinancialData ( String bseCode )
    {
        Query query =  em.createQuery("from FinancialData where bseCode ='" + bseCode +"'"  );
        List<FinancialData> ans = query.getResultList();
        if (CollectionUtils.isNotEmpty(ans))
            return ans.get(0);
        else
            return null;
    }

    public List<FinancialData> listData( int from , int to , String whereCondition, String orderby ) {


        Query query =  em.createQuery("from FinancialData "    +  ((StringUtils.isEmpty(whereCondition))?"":whereCondition) +
                " " + ((StringUtils.isEmpty((orderby))?"": (" order by " + orderby) ))) ;
        query.setFirstResult(from);
        query.setMaxResults(to-from);
        List<FinancialData> ans = query.getResultList();
        return ans;
    }


}
