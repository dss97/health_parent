package com.dou.service;

import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Menu;
import com.dou.pojo.Role;
import com.dou.pojo.User;

import java.util.LinkedHashSet;
import java.util.List;

public interface UserService {
    public User findByUsername(String username);
    public LinkedHashSet<LinkedHashSet<Menu>> getlimit(String username);

    //分页查询数据
    public PageResult findPage(QueryPageBean queryPageBean);
    //查询全部角色信息
    public List<Role> findAll();
    //根据id查询用户
    public User findById(Integer id);
    //根据id查询角色
    public List<Integer> findCheckItemIdsByUserId(Integer id);
    //添加用户信息
    public void add(User user, Integer[] checkitemIds);
    //编辑用户信息
    public void edit(User user, Integer[] checkitemIds);
    //删除用户
    public void delete(Integer id);
    //判定用户是否存在
    public boolean judgeuser(User user);
}
