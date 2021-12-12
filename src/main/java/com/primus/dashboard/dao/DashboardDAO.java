package com.primus.dashboard.dao;


import com.primus.dashboard.model.DashBoardClobData;
import com.primus.dashboard.model.DashboardData;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class DashboardDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void update(DashBoardClobData model) {
        em.merge(model);
    }

    public DashBoardClobData getDashboardData ( long days )
    {
        Query query =  em.createQuery("from DashBoardClobData where days =  " + days  );
        List<DashBoardClobData> ans = query.getResultList();
        if (!CollectionUtils.isEmpty(ans))
            return ans.get(0);
        else
            return null;
    }
}
