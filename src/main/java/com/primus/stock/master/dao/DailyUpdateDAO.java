package com.primus.stock.master.dao;

import com.primus.stock.master.model.DailyUpdate;
import com.primus.user.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class DailyUpdateDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void create(DailyUpdate model) {
        em.merge(model);
    }

    @Transactional
    public void update(DailyUpdate model) {
        em.merge(model);
    }

    public DailyUpdate getDailyUpdated(String groupX, Date date)
    {
        Query query = em.createQuery(" from DailyUpdate where groupX=?1 and updatedDate = ?2 ");
        query.setParameter(1,groupX);
        query.setParameter(2,date);
        List<DailyUpdate> retValue = query.getResultList();
        if (!CollectionUtils.isEmpty(retValue))
            return  retValue.get(0);
        else
            return null;
    }


}
