package com.dou.dao;

import com.dou.pojo.User;

public interface UserDao {
    public User findByUserId(String username);
}
