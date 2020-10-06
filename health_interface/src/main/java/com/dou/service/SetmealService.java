package com.dou.service;

import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

   public void add(Setmeal setmeal, Integer[] checkgroupIds);

   public PageResult pageQuery(QueryPageBean queryPageBean);

   public void edit(Setmeal setmeal, Integer[] checkgroupIds);

   public void delete(Integer id);

   public Setmeal findById(Integer id);

   public List<Setmeal> findAll();

   public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

   public Setmeal findByIdmobile(int id);

    public List<Map<String,Object>> findSetmealCount();
}
