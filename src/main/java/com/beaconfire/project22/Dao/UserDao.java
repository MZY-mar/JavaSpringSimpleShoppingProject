package com.beaconfire.project22.Dao;

import com.beaconfire.project22.Model.Users;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public class UserDao extends AbstractHibernateDao<Users> {
    public UserDao() {
        setClazz(Users.class);
    }
    public boolean existsByUsername(String username) {
        String hql = "SELECT COUNT(u) FROM Users u WHERE u.username = :username";
        Long count = getCurrentSession().createQuery(hql, Long.class)
                .setParameter("username", username)
                .uniqueResult();
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        String hql = "SELECT COUNT(u) FROM Users u WHERE u.email = :email";
        Long count = getCurrentSession().createQuery(hql, Long.class)
                .setParameter("email", email)
                .uniqueResult();
        return count != null && count > 0;
    }

    public Users findByUsername(String username) {
        String hql = "FROM Users u WHERE u.username = :username";
        Users user = getCurrentSession().createQuery(hql, Users.class)
                .setParameter("username", username)
                .uniqueResult();
        System.out.println("get user from db in dao :" +user.getUsername() +
                ": " + user.getPassword());
        return user;
    }

    public Users findById(Long userId) {
        return getCurrentSession().get(Users.class, userId);
    }

}