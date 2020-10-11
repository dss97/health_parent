package com.dou.dao;

import com.dou.pojo.Role;
import com.dou.pojo.User;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface UserDao {
    public User findByUserId(String username);

//分页查询数据
    public Page<User> findByCondition(String queryString);
//查询用户信息
    public List<Role> findAll();
//根据id查询角色信息
    public User findById(Integer id);
//根据id查询用户信息
    public List<Integer> findCheckItemIdsByUserId(Integer id);
//添加用户
    public void add(User user);
//建立连接
    public void setUserAndCheckitemIds(Map<String, Integer> map);
//编辑用户
    public void edit(User user);

    public void deleteAssoication(Integer userId);

    public void delete(Integer id);
}
