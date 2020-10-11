package com.dou.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.dou.constant.MessageConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Permission;
import com.dou.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    //查询全部权限数据
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return  permissionService.findPage(queryPageBean);
    }
    //发现菜单项
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Permission permission= permissionService.findById(id);
            return new Result(true,MessageConstant.GET_PERMISSION_SUCCESS,permission);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_PERMISSION_FAIL);
        }
    }
    //添加用户
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }
    //编辑用户
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }
    //单个删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            permissionService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.DELETE_PERMISSION_FAIL+"可能存在关联项");
        }
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    //批量删除
    @RequestMapping("/deleteAll")
    public Result deleteAll(@RequestBody HashSet<Integer> permissionIds){
        for (Integer permissionId : permissionIds) {
            try {
                permissionService.delete(permissionId);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(true, MessageConstant.DELETE_PERMISSION_FAIL);
            }
        }
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    //批量删除--涉及的json数据转码
    @RequestMapping("/findpermissionAll")
    public Result findpermissionAll(@RequestBody String json){
        List<Permission> permissions = new ArrayList<Permission>(JSONArray.parseArray(json, Permission.class));
        HashSet<Integer> permissionIds=new HashSet<>();
        try {
            for (Permission permission : permissions) {
                permissionIds.add(permission.getId());
            }
            return new Result(true, MessageConstant.GET_USERINFO_SUCCESS,permissionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERINFO_FAIL);
        }
    }
}
