package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.RoleDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Menu;
import com.dou.pojo.Permission;
import com.dou.pojo.Role;
import com.dou.service.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Role> page=roleDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //菜单查询
    @Override
    public List<Menu> findmenuAll() {
        return roleDao.findmenuAll();
    }

    //权限查询
    @Override
    public List<Permission> findpermissionAll() {
        return roleDao.findpermissionAll();
    }

    @Override
    public boolean judgerole(Role role) {
        Role roleId = null;
        try {
            roleId = roleDao.findByroleId(role.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (roleId==null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void add(Role role, Integer[] menucheckitemIds, Integer[] permissioncheckitemIds) {
        roleDao.add(role);
        Integer roleId = role.getId();
        this.setRoleAndMenucheckitemIds(roleId, menucheckitemIds);
        this.setRoleAndPermissioncheckitemIds(roleId, permissioncheckitemIds);
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Integer> findMenuCheckItemIdsByRoleId(Integer id) {
        List<Integer> menuCheckItemIdsByRoleIds = roleDao.findMenuCheckItemIdsByRoleId(id);
        System.out.println(id);
        System.out.println(menuCheckItemIdsByRoleIds);
        return menuCheckItemIdsByRoleIds;
    }

    @Override
    public List<Integer> findPerssionCheckItemIdsByRoleId(Integer id) {
        List<Integer> perssionCheckItemIdsByRoleIds = roleDao.findPerssionCheckItemIdsByRoleId(id);
        System.out.println(id);
        System.out.println(perssionCheckItemIdsByRoleIds);
        return perssionCheckItemIdsByRoleIds;
    }

    //与菜单关联项
    public void setRoleAndMenucheckitemIds(Integer roleId,Integer[] menucheckitemIds){
        if (menucheckitemIds!=null&&menucheckitemIds.length>0){
            for (Integer menucheckitemId : menucheckitemIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("roleId", roleId);
                map.put("menucheckitemId", menucheckitemId);
                roleDao.setRoleAndMenucheckitemIds(map);
            }
        }
    }
//与权限关联项
    public void setRoleAndPermissioncheckitemIds(Integer roleId,Integer[] permissioncheckitemIds){
        if (permissioncheckitemIds!=null&&permissioncheckitemIds.length>0){
            for (Integer permissioncheckitemId : permissioncheckitemIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("roleId", roleId);
                map.put("permissioncheckitemId", permissioncheckitemId);
                roleDao.setRoleAndPermissioncheckitemIds(map);
            }
        }
    }
}
