package com.dou.dao;

import com.dou.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    public void add(Setmeal setmeal);

    public void setSetmealAndCheckGroup(Map map);

    public Page<Setmeal> findByCondition(String queryString);

    public void edit(Setmeal setmeal);

    public void deleteAssoication(Integer id);

    public void delete(Integer id);

    public List<Integer> findsetmealIdsByCheckGroupId(Integer id);

    public List<Setmeal> findAll();

    public Setmeal findById(Integer id);

    public Setmeal findByIdmobile(int id);

    public List<Map<String,Object>> findSetmealCount();
}
