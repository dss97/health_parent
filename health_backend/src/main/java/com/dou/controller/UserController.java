package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.dou.constant.MessageConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Menu;
import com.dou.pojo.Role;
import com.dou.service.UserService;
import com.dou.pojo.User;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

@Reference
private UserService userService;

//获得错误信息
    @RequestMapping("/loginerror")
    public String loginerror(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        AuthenticationException spring_security_last_exception = (AuthenticationException)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        /**
         * 用户名不存在:UsernameNotFoundException;
         * 密码错误:BadCredentialException;
         * 帐户被锁:LockedException;
         * 帐户未启动:DisabledException;
         * 密码过期:CredentialExpiredException;
         */
        if(spring_security_last_exception instanceof BadCredentialsException){
            return "密码错误";
        }else if(spring_security_last_exception instanceof UsernameNotFoundException){
            return "用户名不存在";
        }
        return spring_security_last_exception.getMessage();
    }

    //菜单权限控制
    @RequestMapping("/getlimit")
    public Result getlimit(String username){
        //返回数据类型参考
       /* {
            "path": "4",
                "title": "健康评估",
                "icon":"fa-stethoscope",
                "children":[
            {
                "path": "/4-1",
                    "title": "中医体质辨识",
                    "linkUrl":"all-medical-list.html",
                    "children":[]
            }
                    ]
        },*/

        LinkedHashSet<Map> list=new LinkedHashSet<>();
        LinkedHashSet<LinkedHashSet<Menu>> menus1=userService.getlimit(username);
        try {
            for (LinkedHashSet<Menu> menus : menus1) {
                Map<String,Object> title=new HashMap<>();
                LinkedHashSet<Map> list2=new LinkedHashSet<>();
                for (Menu menu : menus) {
                    Map<String,String> zishuju=new HashedMap();
                    //System.out.println("最终排序id:"+menu.getId());
                    if (menu.getParentMenuId()==null){
                        title.put("path", menu.getPath());
                        title.put("title", menu.getName());
                        title.put("icon", menu.getIcon());
                        title.put("children", list2);
                    }else {
                       zishuju.put("path", menu.getPath());
                       zishuju.put("title", menu.getName());
                       zishuju.put("linkUrl", menu.getLinkUrl());
                        list2.add(zishuju);
                    }
                    }
                list.add(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.GET_USERLIMIT_FAIL);
        }
        return new Result(true, MessageConstant.GET_USERLIMIT_SUCCESS,list);
        }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
    return  userService.findPage(queryPageBean);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Role> roles=userService.findAll();
            return new Result(true, MessageConstant.GET_ROLEINFO_SUCCESS,roles);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLEINFO_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            User user=userService.findById(id);
            return new Result(true, MessageConstant.GET_USERINFO_SUCCESS,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERINFO_FAIL);
        }
    }

    @RequestMapping("/findCheckItemIdsByUserId")
    public Result findCheckItemIdsByUserId(Integer id){
        try {
            List<Integer> checkitemIds=userService.findCheckItemIdsByUserId(id);
            return new Result(true, MessageConstant.GET_ROLEINFO_SUCCESS,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLEINFO_FAIL);
        }
    }

    //判定用户名是否存在
    @RequestMapping("/judgeuser")
    public Result judgeuser(@RequestBody User user,Integer[] checkitemIds){
        try {
            if (userService.judgeuser(user)){
                if (checkitemIds!=null&&checkitemIds.length>0){
                    return new Result(true, "用户信息存在+"+MessageConstant.ADD_ROLEINFO_SUCCESS);
                }else {
                    return new Result(false, MessageConstant.ADD_ROLEINFO_FAIL);
                }
            }else {
                return new Result(false, MessageConstant.JUDGEUSER_YES);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询出错了" );
        }
    }

    //添加用户
    @RequestMapping("/add")
    public Result add(@RequestBody User user,Integer[] checkitemIds){
        try {
            userService.add(user,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_USER_FAIL);
        }
        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }

    //编辑用户
    @RequestMapping("/edit")
    public Result edit(@RequestBody User user,Integer[] checkitemIds){
        try {
            userService.edit(user,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
    }

    //单个删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            userService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_USER_FAIL+"可能存在关联项");
        }
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }

    //批量删除
    @RequestMapping("/deleteAll")
    public Result deleteAll(@RequestBody HashSet<Integer> userIds){
        for (Integer userId : userIds) {
            try {
                userService.delete(userId);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.DELETE_USER_FAIL);
            }
        }
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }

    //批量删除--涉及的json数据转码
    @RequestMapping("/finduserAll")
    public Result finduserAll(@RequestBody String json){
        List<User> users = new ArrayList<User>(JSONArray.parseArray(json, User.class));
        HashSet<Integer> userIds=new HashSet<>();

        try {
            for (User user : users) {
                userIds.add(user.getId());
            }
            return new Result(true, MessageConstant.GET_USERINFO_SUCCESS,userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERINFO_FAIL);
        }
    }
}
