package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.CheckGroupDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.CheckGroup;
import com.dou.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
    //新增检查组，操作t_checkgroup表
     checkGroupDao.add(checkGroup);
        //设置检查组和检查项的多对多的关联关系，操作t_checkgroup_checkitem表
        Integer checkGroupId = checkGroup.getId();

        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }


    //检查组分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        //单个查询条件
        String queryString = queryPageBean.getQueryString();

        //完成分页查询,基于mybatis框架提供非分页助手插件完成(apl)
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_checkgroup limit 0,10
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);

        long total = page.getTotal();
        List<CheckGroup> rows = page.getResult();

        return new PageResult(total, rows);
    }

    //根据id查询检查组
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    //根据检查组id查询关联的检查项
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //编辑检查组信息,同时需要关联检查项
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组基本信息,操作检查组t_checkgroup表
        checkGroupDao.edit(checkGroup);
        //清理当前检查组关联的检查项,操作中间关系表t_checkgroup_checkitem表
        checkGroupDao.deleteAssoication(checkGroup.getId());
        //重新建立当前检查组和检查项的关联关系
        //设置检查组和检查项的多对多的关联关系，操作t_checkgroup_checkitem表
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);

    }

    //根据id删除检查组
    @Override
    public void delete(Integer id) {

        //先删除与外键的关联
        checkGroupDao.deleteAssoication(id);
        //然后删除检查组
        checkGroupDao.delete(id);
    }

    //查询全部检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


    //一对多关系建立--检查组和检查项id绑定
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}

