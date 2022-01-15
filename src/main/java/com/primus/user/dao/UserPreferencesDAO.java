package com.primus.user.dao;


import com.primus.user.model.UserPreferences;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class UserPreferencesDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void update(UserPreferences model) {
        em.merge(model);
    }

    public UserPreferences getByEmail(String email)
    {
        Query query = em.createQuery(" from UserPreferences where emailId=?");
        query.setParameter(1,email);
        List<UserPreferences> retValue = query.getResultList();
        if (!CollectionUtils.isEmpty(retValue))
            return  retValue.get(0);
        else
            return null;

    }
}
