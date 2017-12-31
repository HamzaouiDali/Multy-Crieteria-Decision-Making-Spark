package com.example.mcdms.services;

import java.util.List;

import com.example.mcdms.entities.Users;

public interface UserService {

    Users findById(int id);

    Users findBySSO(String sso);

    void saveUser(Users user);

    void updateUser(Users user);

    void deleteUserBySSO(String sso);

    List<Users> findAllUsers();

    boolean isUserSSOUnique(String sso);

    boolean userValide(String email, String password);

    String findUsername(String email);

    boolean isUserUsernameUnique(String email);

}
