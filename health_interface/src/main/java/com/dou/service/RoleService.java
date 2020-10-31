package com.dou.service;

import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Menu;
import com.dou.pojo.Permission;
import com.dou.pojo.Role;

import java.util.List;

public interface RoleService {
    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean);
    //查询菜单信息
    public List<Menu> findmenuAll();
    //查询权限信息
    public List<Permission> findpermissionAll();
    //判定新添加角色信息是否存在
    public boolean judgerole(Role role);
    //添加角色和关联项
    public void add(Role role, Integer[] menucheckitemIds, Integer[] permissioncheckitemIds);
    //回显角色信息
    public Role findById(Integer id);
    //回显菜单
    public List<Integer> findMenuCheckItemIdsByRoleId(Integer id);
    //回显权限
    public List<Integer> findPerssionCheckItemIdsByRoleId(Integer id);
    //单个删除角色
    public void delete(Integer id);
    //编辑角色和关联项
    public void edit(Role role, Integer[] menucheckitemIds, Integer[] permissioncheckitemIds);
}
