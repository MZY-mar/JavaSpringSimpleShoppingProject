package com.beaconfire.project22.Dao;

import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Model.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class OrderDao extends AbstractHibernateDao<Order> {

    public OrderDao() {
        setClazz(Order.class);
    }

    public List<Order> findByUser(Users user) {
        Session session = getCurrentSession();
        String hql = "FROM Order o WHERE o.user = :user";
        return session.createQuery(hql, Order.class)
                .setParameter("user", user)
                .getResultList();
    }


    public List<Object[]> findTop3FrequentlyPurchasedItemsByUser(Long userId) {
        Session session = getCurrentSession();
        String hql = "SELECT oi.product.productId, COUNT(oi.product.productId) AS purchaseCount " +
                "FROM OrderItem oi " +
                "JOIN oi.order o " +
                "WHERE o.user.userId = :userId " +
                "GROUP BY oi.product.productId " +
                "ORDER BY purchaseCount DESC";

        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("userId", userId);
        query.setMaxResults(3);  // Limit to top 3 items

        return query.list();
    }

    public List<Object[]> findTop3RecentlyPurchasedItemsByUser(Long userId) {
        Session session = getCurrentSession();
        String hql = "SELECT oi.product.productId, max(o.datePlaced) " +
                "AS most_recent_date " +
                "FROM OrderItem oi " +
                "JOIN oi.order o " +
                "WHERE o.user.userId = :userId " +
                "GROUP BY oi.product.productId " +
                "ORDER BY most_recent_date DESC";

        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("userId", userId);
        query.setMaxResults(3);  // Limit to top 3 items

        return query.list();
    }


}
