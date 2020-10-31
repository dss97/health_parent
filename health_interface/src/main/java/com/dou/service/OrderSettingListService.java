package com.dou.service;

import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Member;
import com.dou.pojo.Order;
import com.dou.pojo.Setmeal;

import java.util.List;

public interface OrderSettingListService {
    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean);

    public List<Member> findmember();

    public List<Setmeal> findsetmeal();

    public Result add(Order order) throws Exception;

    public void edit(Order order);

    public Order findByid(Integer id);

    public void delete(Integer id);

    public String findmemberById(Integer id);

    public String findsetmealById(Integer id);
}
