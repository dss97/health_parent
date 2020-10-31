package com.dou.dao;

import com.dou.pojo.Member;
import com.dou.pojo.Order;
import com.dou.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;

public interface OrderSettingListDao {
    public Page<Order> findByCondition(String queryString);

    public List<Member> findmember();

    public List<Setmeal> findsetmeal();

    public void add(Order order);

    public void edit(Order order);

    public Order findById(Integer id);

    public void delete(Integer id);

    public String findmemberById(Integer id);

    public String findsetmealById(Integer id);
}
