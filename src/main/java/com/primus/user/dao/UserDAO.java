package com.primus.user.dao;

import com.primus.user.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class UserDAO {
    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void update(User model) {
        em.merge(model);
    }

    public User getByEmail(String email)
    {
        Query query = em.createQuery(" from User where email=?1");
        query.setParameter(1,email);
        List<User> retValue = query.getResultList();
        if (!CollectionUtils.isEmpty(retValue))
             return  retValue.get(0);
        else
            return null;

    }

    public User getByPhone(String phone)
    {
        Query query = em.createQuery(" from User where phoneNumber=?1");
        query.setParameter(1,phone);
        List<User> retValue = query.getResultList();
        if (!CollectionUtils.isEmpty(retValue))
            return  retValue.get(0);
        else
            return null;

    }


    public User getByEmailAndPassword(String email,String password)
    {
        Query query = em.createQuery(" from User where email=?1 and password = ?2 ");
        query.setParameter(1,email);
        query.setParameter(2,password);
        List<User> retValue = query.getResultList();
        if (!CollectionUtils.isEmpty(retValue))
            return  retValue.get(0);
        else
            return null;

    }




}
