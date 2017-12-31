package com.example.mcdms.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.mcdms.entities.Users;

@Transactional
@Repository
public class UserDaloImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Users findById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Users findBySSO(String sso) {
        return null;
    }

    @Override
    public void save(Users user) {
        // TODO Auto-generated method stub
        entityManager.persist(user);

    }

    @Override
    public void deleteBySSO(String sso) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Users> findAllUsers() {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public boolean isUserSSOUnique(String sso) {
        String hql = "FROM Users as us WHERE us.email = ?";
        int count = entityManager.createQuery(hql).setParameter(1, sso)
                .getResultList().size();
        return count > 0 ? true : false;

    }

    @Override
    public boolean userValide(String email, String password) {
        // TODO Auto-generated method stub
        String hql = "FROM Users as us WHERE us.email = ? and us.password = ?";
        int count = entityManager.createQuery(hql).setParameter(1, email)
                .setParameter(2, password)
                .getResultList().size();
        return count > 0 ? true : false;
    }

    @Override
    public String findUsername(String email) {
        String hql = "select us.username FROM Users as us WHERE us.email = ?";
        String username = entityManager.createQuery(hql).setParameter(1, email).getResultList().get(0).toString();
        return username;
    }

    @Override
    public boolean isUserUsernameUnique(String email) {
        String hql = "FROM Users as us WHERE us.username = ?";
        int count = entityManager.createQuery(hql).setParameter(1, email)
                .getResultList().size();
        return count > 0 ? true : false;
    }

}
