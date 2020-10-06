package com.dou.dao;

import com.dou.pojo.Role;
import com.dou.pojo.User;

import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(Integer userId);
}
