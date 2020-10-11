package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.PermissionDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Permission;
import com.dou.pojo.User;
import com.dou.service.PermissionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Permission> page=permissionDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public void delete(Integer id) {
        permissionDao.delete(id);
    }
}
