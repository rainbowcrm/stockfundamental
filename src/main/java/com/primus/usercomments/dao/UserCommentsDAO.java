package com.primus.usercomments.dao;


import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import com.primus.usercomments.model.UserComments;
import java.util.List;

@Component
@Transactional
public class UserCommentsDAO {

    @PersistenceContext
    protected EntityManager em;

    @Transactional
    public void update(UserComments model) {
        em.merge(model);
    }


    public List<UserComments> getCommentsForStock(String bseCode)
    {
        Query query =  em.createQuery("from UserComments  where bseCode = ?1 order by commentedDate desc ");
        query.setParameter(1,bseCode);
        List<UserComments> ans = query.getResultList();
        return ans;


    }
}
