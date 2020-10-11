package com.dou.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.*;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Menu;
import com.dou.pojo.Permission;
import com.dou.pojo.Role;
import com.dou.pojo.User;
import com.dou.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private MenuDao menuDao;

    //多表进行关联
    @Override
    public User findByUsername(String username) {
        User user=userDao.findByUserId(username);
        if (user==null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户id查询对应角色
        Set<Role> roles=roleDao.findByUserId(userId);
        //根据角色查询对应权限
        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> Permissions=permissionDao.findByRoleId(roleId);
             role.setPermissions(Permissions);
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public LinkedHashSet<LinkedHashSet<Menu>> getlimit(String username) {
        User user=userDao.findByUserId(username);
        if (user==null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户id查询对应角色
        Set<Role> roles=roleDao.findByUserId(userId);
        LinkedHashSet<Menu> menus=new LinkedHashSet<>();
        //LinkedHashSet<Menu> menus2=null;
        LinkedHashSet<Menu> menus2=new LinkedHashSet<>();

        LinkedHashSet<LinkedHashSet<Menu>> listmenus=new LinkedHashSet<>();
        for (Role role : roles) {
            Integer roleId = role.getId();
            menus=menuDao.findByRoleId(roleId);
            role.setMenus(menus);
        }
        user.setRoles(roles);

        for (Menu menu : menus) {
           if (menu.getParentMenuId()==null||menu.getParentMenuId().equals("")){
               menus2.add(menu);
             //  System.out.println("前置"+menu.getPath());
              // System.out.println("id"+menu.getId());
            }
        }

            for (Menu menu : menus2) {
                //System.out.println("输出id:"+menu.getId());
                LinkedHashSet<Menu> menus3=new LinkedHashSet<>();
                    menus3.add(menu);
                for (Menu menu1 : menus) {
                    if (menu.getId().equals(menu1.getParentMenuId())){
                        menus3.add(menu1);
                    }
                }
                listmenus.add(menus3);
            }
        return listmenus;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<User> page=userDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles=userDao.findAll();
        return roles;
    }

    @Override
    public User findById(Integer id) {
        User user=userDao.findById(id);
        return user;
    }

    @Override
    public List<Integer> findCheckItemIdsByUserId(Integer id) {
        List<Integer> checkitemIds=userDao.findCheckItemIdsByUserId(id);
        return checkitemIds;
    }

    @Override
    public void add(User user, Integer[] checkitemIds) {
            //用户密码加密
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);

            userDao.add(user);
            Integer userId = user.getId();
            this.setUserAndCheckitemIds(userId, checkitemIds);
        }


    @Override
    public void edit(User user, Integer[] checkitemIds) {
        //用户密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userDao.edit(user);

        Integer userId = user.getId();
        //删除连接
        userDao.deleteAssoication(userId);

        setUserAndCheckitemIds(userId, checkitemIds);
    }

    @Override
    public void delete(Integer id) {
        try {
            userDao.deleteAssoication(id);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        userDao.delete(id);

    }

    //判定用户名是否存在
    @Override
    public boolean judgeuser(User user) {
        User userId = null;
        try {
            userId = userDao.findByUserId(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userId==null){
            return true;
        }else {
            return false;
        }
    }

    public void setUserAndCheckitemIds(Integer userId,Integer[] checkitemIds){
        if (checkitemIds!=null&&checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("userId", userId);
                map.put("checkitemId", checkitemId);
                userDao.setUserAndCheckitemIds(map);
            }
        }
    }
}
