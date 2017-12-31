package com.example.mcdms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mcdms.dao.UserDao;
import com.example.mcdms.entities.Users;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /*@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
     */
    @Override
    public void saveUser(Users user) {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public Users findBySSO(String username) {
        return userDao.findBySSO(username);
    }

    @Override
    public Users findById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateUser(Users user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUserBySSO(String sso) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Users> findAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUserSSOUnique(String sso) {
        // TODO Auto-generated method stub
        return userDao.isUserSSOUnique(sso);
    }

    @Override
    public boolean userValide(String email, String password) {
        // TODO Auto-generated method stub
        return userDao.userValide(email, password);
    }

    @Override
    public String findUsername(String email) {
        // TODO Auto-generated method stub
        return userDao.findUsername(email);
    }

    @Override
    public boolean isUserUsernameUnique(String email) {
        return userDao.isUserUsernameUnique(email);

    }
}
