package com.primus.stock.master.dao;

import com.primus.stock.master.model.FundamentalData;
import com.primus.stock.master.model.StocksMaster;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
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

    public FundamentalData getFundamentalData (  String bseCode)
    {
        Query query =  em.createQuery("from FundamentalData where bseCode = '"  + bseCode + "'" );
        List<FundamentalData> ans = query.getResultList();
        if (!CollectionUtils.isEmpty(ans))
            return ans.get(0);
        else
            return null;

    }

    public List<FundamentalData> getAllFundamentals (  String whereCondition)
    {
        Query query =  em.createQuery("from FundamentalData " +  ((StringUtils.isEmpty(whereCondition))?"":whereCondition) );
        List<FundamentalData> ans = query.getResultList();
        return ans;
    }

    public List<FundamentalData> listData( int from , int to , String whereCondition, String orderby ) {


        Query query =  em.createQuery("from FundamentalData "    +  ((StringUtils.isEmpty(whereCondition))?"":whereCondition) +
                " " + ((StringUtils.isEmpty((orderby))?"": (" order by " + orderby) ))) ;
        query.setFirstResult(from);
        query.setMaxResults(to-from);
        List<FundamentalData> ans = query.getResultList();
        return ans;
    }


    public List<String> getDistinctIndustry()
    {
        Query query =  em.createQuery("Select distinct  industry from FundamentalData "  );
        List<String> ans = query.getResultList();
        return ans;

    }

    public List<String> getDistinctSector()
    {
        Query query =  em.createQuery("Select distinct  sector from FundamentalData "  );
        List<String> ans = query.getResultList();
        return ans;

    }

    public List<String> getDistinctIndustry(String searchStr)
    {
        Query query =  em.createQuery("Select distinct  industry from FundamentalData where industry like :searchKey "  );
        query.setParameter("searchKey",searchStr + "%");
        List<String> ans = query.getResultList();
        return ans;

    }

    public List<String> getDistinctSector(String searchStr)
    {
        Query query =  em.createQuery("Select distinct  sector from FundamentalData where sector like :searchKey  "  );
        query.setParameter("searchKey",searchStr + "%");
        List<String> ans = query.getResultList();
        return ans;
    }

    public List<String> getDistinctStock(String searchStr)
    {
        Query query =  em.createQuery("Select distinct  company from FundamentalData where company like :searchKey  "  );
        query.setParameter("searchKey",searchStr + "%");
        List<String> ans = query.getResultList();
        return ans;

    }

}
