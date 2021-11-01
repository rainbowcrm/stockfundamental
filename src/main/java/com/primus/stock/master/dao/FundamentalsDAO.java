package com.primus.stock.master.dao;

import com.primus.stock.master.model.FundamentalData;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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



}
