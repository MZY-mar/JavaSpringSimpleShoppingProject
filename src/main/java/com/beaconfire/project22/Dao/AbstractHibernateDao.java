package com.beaconfire.project22.Dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractHibernateDao<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected Class<T> clazz;

    protected final void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public List<T> getAll() {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).getResultList();
    }

    public T findById(Long id) {
        return getCurrentSession().get(clazz, id);
    }

    public void add(T item) {
        getCurrentSession().save(item);
    }
    public T saveOrUpdate(T entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }
    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }
    public void deleteById(Long id) {
        T entity = findById(id);
        if (entity != null) {
            delete(entity);
        }
    }
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
