package com.primus.useractions.dao;

import com.primus.user.model.User;
import com.primus.useractions.model.UserAction;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
@Transactional
public class UserActionDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void update(UserAction model) {
        em.merge(model);
    }


}
