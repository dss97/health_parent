package com.dou.dao;

import com.dou.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

   public void add(CheckGroup checkGroup);

   public void setCheckGroupAndCheckItem(Map<String,Integer> map);

   public  Page<CheckGroup> findByCondition(String queryString);

   public CheckGroup findById(Integer id);

   public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

   public void edit(CheckGroup checkGroup);

   public void deleteAssoication(Integer id);

   public void delete(Integer id);

   public List<CheckGroup> findAll();


}
