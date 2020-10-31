package com.dou.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.dou.constant.MessageConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Member;
import com.dou.pojo.Order;
import com.dou.pojo.Setmeal;
import com.dou.service.OrderSettingListService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/ordersettinglist")
public class OrderSettingListController {

    @Reference
    private OrderSettingListService orderSettingListService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return  orderSettingListService.findPage(queryPageBean);
    }

    @RequestMapping("/findmember")
    public Result findmenu(){
        try {
            List<Member> menus=orderSettingListService.findmember();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
    @RequestMapping("/findsetmeal")
    public Result findsetmeal(){
        try {
            List<Setmeal> setmeals=orderSettingListService.findsetmeal();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
    //添加用户
    @RequestMapping("/add")
    public Result add(@RequestBody Order order){
        Result result = new Result(false, MessageConstant.VALIDATECODE_ERROR);
        try {
            result =orderSettingListService.add(order);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return new Result(true, MessageConstant.ADD_ORDER_SUCCESS);
    }
    //添加用户
    @RequestMapping("/edit")
    public Result edit(@RequestBody Order order){
        try {
            orderSettingListService.edit(order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ORDER_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_ORDER_SUCCESS);
    }
    //发现菜单项
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Order order= orderSettingListService.findByid(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,order);

        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
    //批量删除--涉及的json数据转码
    @RequestMapping("/findOrderAll")
    public Result findmenuAll(@RequestBody String json){
        List<Order> orders = new ArrayList<Order>(JSONArray.parseArray(json, Order.class));
        HashSet<Integer> orderIds=new HashSet<>();
        try {
            for (Order order : orders) {
                orderIds.add(order.getId());
            }
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
    //批量删除
    @RequestMapping("/deleteAll")
    public Result deleteAll(@RequestBody HashSet<Integer> orderIds){
        for (Integer orderId : orderIds) {
            try {
                orderSettingListService.delete(orderId);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.DELETE_ORDER_FAIL);
            }
        }
        return new Result(true, MessageConstant.DELETE_ORDER_SUCCESS);
    }
    //单个删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            orderSettingListService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.DELETE_ORDER_FAIL+"可能存在关联项");
        }
        return new Result(true, MessageConstant.DELETE_ORDER_SUCCESS);
    }
    //发现菜单项
    @RequestMapping("/findmemberById")
    public Result findmemberById(Integer id){
        try {
            String memberName= orderSettingListService.findmemberById(id);
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,memberName);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
    //发现菜单项
    @RequestMapping("/findsetmealById")
    public Result findsetmealById(Integer id){
        try {
            String setmealName= orderSettingListService.findsetmealById(id);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,setmealName);
        }catch (Exception e){
            //logger.error(e);
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
}
