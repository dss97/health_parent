package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dou.constant.MessageConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.CheckGroup;
import com.dou.pojo.CheckItem;
import com.dou.service.CheckGroupService;
import com.sun.tools.doclint.Checker;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查组controller管理
 */
@RestController
@RequestMapping("checkgroup")
public class CheckGroupController {

    @Reference
private CheckGroupService checkGroupservice;
    //新增检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){//json数据加requestbody
        try {
            checkGroupservice.add(checkGroup,checkitemIds);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {//json数据加requestbody
        PageResult pageResult = checkGroupservice.pageQuery(queryPageBean);
        return pageResult;
    }

    //根据id查询检查组
    @RequestMapping("/findById")
    public Result findById(Integer id){//json数据加requestbody
        try {
            CheckGroup checkGroup=checkGroupservice.findById(id);
            //查询成功还需要使用成功后的数据
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //根据检查组id检查检查组包含的多个检查项id
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
            List<Integer> checkitemIds=checkGroupservice.findCheckItemIdsByCheckGroupId(id);
            //查询成功还需要使用成功后的数据
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //编辑检查组
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){//json数据加requestbody
        try {
            checkGroupservice.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //删除检查组
    @RequestMapping("/delete")
    public Result delete(Integer id){//json数据加requestbody
        try {
            checkGroupservice.delete(id);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询检查组
    @RequestMapping("/findAll")
    public Result findAll(){//json数据加requestbody

            List<CheckGroup> checkGroupList =checkGroupservice.findAll();
            //查询成功还需要使用成功后的数据
            if (checkGroupList !=null && checkGroupList.size()>0){
                return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
            }
            return  new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL,new ArrayList<CheckGroup>());
    }

}
