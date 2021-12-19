package com.primus.user.dao;

import com.primus.user.model.User;
import com.primus.user.model.UserOTP;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class UserOTPDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void update(UserOTP model) {
        em.merge(model);
    }

    public UserOTP getByMobile(String phone)
    {
        Query query = em.createQuery(" from UserOTP where phoneNumber=?");
        query.setParameter(1,phone);
        List<UserOTP> retValue = query.getResultList();
        if (!CollectionUtils.isEmpty(retValue))
            return  retValue.get(0);
        else
            return null;

    }
}
