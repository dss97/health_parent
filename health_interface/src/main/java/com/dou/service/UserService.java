package com.dou.service;

import com.dou.pojo.User;

public interface UserService {
    public User findByUsername(String username);
}
