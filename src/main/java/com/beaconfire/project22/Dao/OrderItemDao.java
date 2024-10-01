package com.beaconfire.project22.Dao;

import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Model.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao extends AbstractHibernateDao<OrderItem>{
    public OrderItemDao() {
        setClazz(OrderItem.class);
    }
}
