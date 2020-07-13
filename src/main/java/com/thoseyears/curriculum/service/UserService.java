package com.thoseyears.curriculum.service;

import com.thoseyears.curriculum.entity.User;

import java.util.List;

public interface UserService {
    public List<User> findallUser();
    public String checkUser(String userid,String password) throws Exception;
    public User findlUserByid(String userid);
    public String addUser(User user);
    public User updateUser(String userid,User user);
    public User updateUserByUsername(String usernmae,User user);
    public boolean deleteUserByid(String userid);
}
