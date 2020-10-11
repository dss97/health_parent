package com.dou.service;


import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Permission;

public interface PermissionService {
    public PageResult findPage(QueryPageBean queryPageBean);

    public Permission findById(Integer id);

    public void add(Permission permission);

    public void edit(Permission permission);

    public void delete(Integer id);
}
