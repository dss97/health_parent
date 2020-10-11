package com.dou.dao;

import com.dou.pojo.Menu;
import com.dou.pojo.User;
import com.github.pagehelper.Page;

import java.util.LinkedHashSet;
import java.util.List;

public interface MenuDao {

    public LinkedHashSet<Menu> findByRoleId(Integer roleId);

    public Page<Menu> findByCondition(String queryString);

    public List<Menu> findmenu();

    public void add(Menu menu);

    public Menu findById(Integer id);

    public void edit(Menu menu);

    public void delete(Integer id);
}
