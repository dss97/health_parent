package com.dou.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.dou.constant.MessageConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Menu;
import com.dou.pojo.Permission;
import com.dou.pojo.Role;
import com.dou.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    //查询全部权限数据
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return  roleService.findPage(queryPageBean);
    }

    @RequestMapping("/findmenuAll")
    public Result findmenuAll(){
        try {
            List<Menu> menus=roleService.findmenuAll();
            return new Result(true, MessageConstant.GET_MENUINFO_SUCCESS,menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENUINFO_FAIL);
        }
    }

    @RequestMapping("/findpermissionAll")
    public Result findpermissionAll(){
        try {
            List<Permission> permissions=roleService.findpermissionAll();
            return new Result(true, MessageConstant.GET_MENUINFO_SUCCESS,permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENUINFO_FAIL);
        }
    }
    //判定角色名是否存在
    @RequestMapping("/judgerole")
    public Result judgerole(@RequestBody Role role, Integer[] menucheckitemIds,Integer[] permissioncheckitemIds){
        try {
            if (roleService.judgerole(role)){
                if (menucheckitemIds!=null&&menucheckitemIds.length>0&&permissioncheckitemIds!=null&&permissioncheckitemIds.length>0){
                    return new Result(true, "角色信息存在+"+MessageConstant.ADD_ROLEINFO_SUCCESS);
                }else {
                    return new Result(false, MessageConstant.ADD_ROLEINFO_FAIL);
                }
            }else {
                return new Result(false, MessageConstant.JUDGEROLE_YES);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询出错了" );
        }
    }

    //添加用户
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] menucheckitemIds,Integer[] permissioncheckitemIds){
        try {
            roleService.add(role,menucheckitemIds,permissioncheckitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ROLE_FAIL);
        }
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }
    //回显角色
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Role role=roleService.findById(id);
            return new Result(true, MessageConstant.GET_ROLEINFO_SUCCESS,role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLEINFO_FAIL);
        }
    }
    //回显菜单
    @RequestMapping("/findMenuCheckItemIdsByRoleId")
    public Result findMenuCheckItemIdsByRoleId(Integer id){
        try {
            List<Integer> menucheckitemIds=roleService.findMenuCheckItemIdsByRoleId(id);
            return new Result(true, MessageConstant.GET_MENUINFO_SUCCESS,menucheckitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENUINFO_FAIL);
        }
    }
    //回显菜单
    @RequestMapping("/findPerssionCheckItemIdsByUserId")
    public Result findPerssionCheckItemIdsByUserId(Integer id){
        try {
            List<Integer> permissioncheckitemIds=roleService.findPerssionCheckItemIdsByRoleId(id);
            return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS,permissioncheckitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_PERMISSION_FAIL);
        }
    }
}
