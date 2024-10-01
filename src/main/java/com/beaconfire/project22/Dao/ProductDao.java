package com.beaconfire.project22.Dao;

import com.beaconfire.project22.Model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductDao extends AbstractHibernateDao<Product> {
    public ProductDao() {
        setClazz(Product.class);
    }
    public List<Product> findByQuantityGreaterThan(int quantity){
        String hql = "From Product p where p.quantity > :quantity";
        return getCurrentSession().createQuery(hql,Product.class).setParameter("quantity",quantity).getResultList();

    }

    public boolean existsByProductIdAndQuantityGreaterThan(Long productId, int quantity) {
        String hql = "SELECT COUNT(p) FROM Product p WHERE p.productId = :productId AND p.quantity > :quantity";
        Long count = getCurrentSession().createQuery(hql, Long.class)
                .setParameter("productId", productId)
                .setParameter("quantity", quantity)
                .uniqueResult();
        return count != null && count > 0;
    }




}
