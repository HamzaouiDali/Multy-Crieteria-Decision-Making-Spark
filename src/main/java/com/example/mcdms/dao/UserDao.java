package com.example.mcdms.dao;

import java.util.List;

import com.example.mcdms.entities.Users;

public interface UserDao {

    Users findById(int id);

    Users findBySSO(String sso);

    void save(Users user);

    void deleteBySSO(String sso);

    List<Users> findAllUsers();

    boolean isUserSSOUnique(String sso);

    boolean userValide(String email, String password);

    String findUsername(String email);

    boolean isUserUsernameUnique(String email);

}
