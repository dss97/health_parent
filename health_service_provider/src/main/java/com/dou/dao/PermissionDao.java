package com.dou.dao;

import com.dou.pojo.Permission;
import com.github.pagehelper.Page;

import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);

    public Page<Permission> findByCondition(String queryString);

    public Permission findById(Integer id);

    public void add(Permission permission);

    public void edit(Permission permission);

    public void delete(Integer id);
}
