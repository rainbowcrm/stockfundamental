package com.primus.stock.master.dao;

import com.primus.stock.master.model.QuarterReport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
@Transactional
public class QuarterReportDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void create(QuarterReport model) {
        em.merge(model);
    }

    @Transactional
    public void update(QuarterReport model) {
        em.merge(model);
    }


}
