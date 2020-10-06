package com.dou.dao;


import com.dou.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckItemDao {
    //添加
    public void add(CheckItem checkItem);

    //查询并分页
    public Page<CheckItem> selectByCondition(String queryString);

    public long findCountByCheckItemId(Integer id);

    public void deleteById(Integer id);

    public void edit(CheckItem checkItem);

    public  CheckItem findById(Integer id);

    public List<CheckItem> findAll();
}
