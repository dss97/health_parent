package com.dou.service;


import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Menu;

import java.util.List;

public interface MenuService {

    //查询菜单信息
    public PageResult findPage(QueryPageBean queryPageBean);
    //查询主菜单信息
    public List<Menu> findmenu();
    //添加菜单
    public void add(Menu menu);
    //编辑菜单中回显信息
    public Menu findByid(Integer id);
    //编辑菜单
    public void edit(Menu menu);
    //删除菜单
    public void delete(Integer id);
}
