package com.beaconfire.project22.Dao;
import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Model.WatchList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class WatchListDao extends AbstractHibernateDao<WatchList> {

    public WatchListDao() {
        setClazz(WatchList.class);
    }
    public Optional<WatchList> findByUserIdAndProductId(Long userId,
                                                        Long productId) {
        String hql = "FROM WatchList w WHERE w.userId = :userId AND w.productId = :productId";
        WatchList watchList = getCurrentSession().createQuery(hql, WatchList.class)
                .setParameter("userId", userId)
                .setParameter("productId", productId)
                .uniqueResult();
        return Optional.ofNullable(watchList);
    }
    public List<WatchList> findByUserId(Long userId) {
        String hql = "FROM WatchList w WHERE w.userId = :userId";
        return getCurrentSession().createQuery(hql, WatchList.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public WatchList save(WatchList watchList){
        getCurrentSession().saveOrUpdate(watchList);
        return watchList;
    }
}
