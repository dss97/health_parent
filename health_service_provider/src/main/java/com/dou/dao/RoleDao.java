package com.dou.dao;

import com.dou.pojo.Menu;
import com.dou.pojo.Permission;
import com.dou.pojo.Role;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(Integer userId);
//查询全部角色信息
    public Page<Role> findByCondition(String queryString);
//查询菜单信息
    public List<Menu> findmenuAll();
//查询权限信息
    public List<Permission> findpermissionAll();
//判定添加信息
    public Role findByroleId(String name);
//添加角色
    public void add(Role role);
//关联菜单项
    public void setRoleAndMenucheckitemIds(Map<String, Integer> map);
//关联权限项
    public void setRoleAndPermissioncheckitemIds(Map<String, Integer> map);
//回显角色信息
    public Role findById(Integer id);
//回显菜单信息
    public List<Integer> findMenuCheckItemIdsByRoleId(Integer id);
//回显权限信息
    public List<Integer> findPerssionCheckItemIdsByRoleId(Integer id);
//删除菜单关联
    public void deleteMenuAssoication(Integer id);
//删除权限关联
    public void deletePermissionAssoication(Integer id);
//删除角色
    public void delete(Integer id);
//编辑角色
    public void edit(Role role);
}
