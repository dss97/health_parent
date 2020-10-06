package com.dou.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.PermissionDao;
import com.dou.dao.RoleDao;
import com.dou.dao.UserDao;
import com.dou.pojo.Permission;
import com.dou.pojo.Role;
import com.dou.pojo.User;
import com.dou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

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
}
