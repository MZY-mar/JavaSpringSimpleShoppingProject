package com.beaconfire.project22.Dao;

import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Model.Users;
import org.hibernate.Session;
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
}
