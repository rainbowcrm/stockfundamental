package com.primus.stock.master.dao;

import com.primus.stock.master.model.QuarterReport;
import com.primus.stock.master.model.StocksMaster;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class QuarterReportDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void create(QuarterReport model) {
        em.merge(model);
    }

    public List<QuarterReport> getQuarterResult(long year, int quarter) {
        Query query =  em.createQuery("  from QuarterReport where  year = :year  and quarter = :quarter " );
        query.setParameter("year",year);
        query.setParameter("quarter",quarter);
        List<QuarterReport> ans = query.getResultList();
        return ans;
    }

    @Transactional
    public void update(QuarterReport model) {
        em.merge(model);
    }


}
