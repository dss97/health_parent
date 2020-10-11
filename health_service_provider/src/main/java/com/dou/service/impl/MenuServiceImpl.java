package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.MenuDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Menu;
import com.dou.service.MenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Menu> page=menuDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //主菜单信息查询
    @Override
    public List<Menu> findmenu() {
        List<Menu> menus=menuDao.findmenu();
        return menus;
    }
    //添加菜单
    @Override
    public void add(Menu menu) {
         menuDao.add(menu);
    }

    //编辑菜单
    @Override
    public Menu findByid(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

    @Override
    public void delete(Integer id) {
        menuDao.delete(id);
    }
}
