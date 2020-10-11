package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.dou.constant.MessageConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Menu;
import com.dou.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return menuService.findPage(queryPageBean);
    }

    @RequestMapping("/findmenu")
    public Result findmenu(){
        try {
          List<Menu> menus=menuService.findmenu();
            return new Result(true, MessageConstant.GET_USERLIMIT_SUCCESS,menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERLIMIT_FAIL);
        }
    }

    //添加用户
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_MENU_FAIL);
        }
        return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
    }
    //发现菜单项
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Menu menu= menuService.findByid(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,menu);

        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
    //编辑菜单项
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MENU_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
    }

    //单个删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            menuService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.DELETE_MENU_FAIL+"可能存在关联项");
        }
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }

    //批量删除--涉及的json数据转码
    @RequestMapping("/findmenuAll")
    public Result findmenuAll(@RequestBody String json){
        List<Menu> menus = new ArrayList<Menu>(JSONArray.parseArray(json, Menu.class));
        HashSet<Integer> menuIds=new HashSet<>();
        try {
            for (Menu menu : menus) {
                menuIds.add(menu.getId());
            }
            return new Result(true, MessageConstant.GET_MENUINFO_SUCCESS,menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENUINFO_FAIL);
        }
    }
    //批量删除
    @RequestMapping("/deleteAll")
    public Result deleteAll(@RequestBody HashSet<Integer> userIds){
        for (Integer userId : userIds) {
            try {
                menuService.delete(userId);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(true, MessageConstant.DELETE_USER_FAIL);
            }
        }
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }
}
