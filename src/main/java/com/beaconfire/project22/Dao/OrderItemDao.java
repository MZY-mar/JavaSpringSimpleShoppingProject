package com.beaconfire.project22.Dao;

import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Model.OrderItem;
import com.beaconfire.project22.Model.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemDao extends AbstractHibernateDao<OrderItem>{
    public OrderItemDao() {
        setClazz(OrderItem.class);
    }

    public List<OrderItem> getAllOrderItemsByOrderId(Long orderId) {
        Session session = getCurrentSession();
        String hql = "FROM OrderItem oi WHERE oi.order.orderId = :orderId";
        Query<OrderItem> query = session.createQuery(hql, OrderItem.class);
        query.setParameter("orderId", orderId);

        return query.getResultList();
    }


}
