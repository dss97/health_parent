package com.dou.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.CheckItemDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.CheckItem;
import com.dou.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */
//事务:明确实现的服务接口
@Service(interfaceClass = CheckItemService.class)
@Transactional  //事务注解
public class CheckItemServiceImpl implements CheckItemService {

    //注入dao对象
    @Autowired
    private  CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //检查性分页查询
     @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        //单个查询条件
        String queryString = queryPageBean.getQueryString();

        //完成分页查询,基于mybatis框架提供非分页助手插件完成(apl)
         PageHelper.startPage(currentPage,pageSize);
         //select * from t_checkitem limit 0,10
         Page<CheckItem> page = checkItemDao.selectByCondition(queryString);

         long total = page.getTotal();
         List<CheckItem> rows = page.getResult();

         return new PageResult(total, rows);
    }

    //根据id删除
    @Override
    public void deleteById(Integer id){
     //判断当前检查项是否关联检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count >0){
         //当前检查项已经关联到检查组
         throw  new  RuntimeException("我被关联了");
     }

     checkItemDao.deleteById(id);
    }

    //编辑检查项表单
    @Override
    public void edit(CheckItem checkItem) {
    checkItemDao.edit(checkItem);
    }

    @Override
    public CheckItem findByid(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

}
