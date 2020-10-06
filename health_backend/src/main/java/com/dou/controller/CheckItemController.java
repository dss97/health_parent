package com.dou.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.dou.constant.MessageConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.CheckItem;
import com.dou.service.CheckItemService;
import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 检查项管理
 */

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    private static Logger logger = Logger.getLogger(CheckItemController.class);

    @Reference
    private CheckItemService checkItemService;
    //新增
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            //logger.error(e);
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return  pageResult;
    }

    //删除检查项
    //使用注解形式添加权限
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
          checkItemService.deleteById(id);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //编辑检查项
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //编辑检查项
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
           CheckItem checkItem= checkItemService.findByid(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);

        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //查询所有,供检查组使用
    @RequestMapping("/findAll")
    public Result findAll(){
            List<CheckItem> checkItemList=checkItemService.findAll();
            if (checkItemList!=null && checkItemList.size()>0){
                return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
            }
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL,new ArrayList<>());
    }

}