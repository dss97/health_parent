package com.dou.service;

import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    public void delete(Integer id);

    public List<CheckGroup> findAll();

}
