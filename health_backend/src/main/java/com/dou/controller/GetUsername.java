package com.dou.controller;

import com.dou.constant.MessageConstant;
import com.dou.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/getuser")
public class GetUsername {

    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当spring security完成认证后,会将当前用户信息保存到框架提供的上下文对象中去

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if (user!=null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
