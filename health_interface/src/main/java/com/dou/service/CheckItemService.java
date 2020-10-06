package com.dou.service;

import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.CheckItem;

import java.util.List;

//服务接口
public interface CheckItemService {
   //检查项
    public  void  add(CheckItem checkItem);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public void deleteById(Integer id);
    public  void edit(CheckItem checkItem);
    public CheckItem findByid(Integer id);

    //检查组
    public List<CheckItem> findAll();

}
