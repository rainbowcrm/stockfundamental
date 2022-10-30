package com.primus.stock.master.dao;



import com.primus.stock.master.model.DividentHistory;
import com.primus.stock.master.model.QuarterReport;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional
public class DividentHistoryDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void create(DividentHistory model) {
        em.merge(model);
    }

    @Transactional
    public void update(DividentHistory model) {
        em.merge(model);
    }

    public DividentHistory getDividentsofCompany(String bseCode, Date exDate)
    {
        Query query =  em.createQuery("  from DividentHistory where  bseCode = :bseCode and exDate =: exDate" );
        query.setParameter("bseCode",bseCode);
        query.setParameter("exDate",exDate);
        List<DividentHistory> ans = query.getResultList();
        if (CollectionUtils.isNotEmpty(ans))
            return ans.get(0);
        else
            return null;


    }

    public List<DividentHistory> getDividentHistory(Date fromDate, Date toDate)
    {
        Query query =  em.createQuery("  from DividentHistory where  exDate >= :fromDate and exDate<= :toDate " );
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        List<DividentHistory> ans = query.getResultList();
        return ans ;
    }

}
