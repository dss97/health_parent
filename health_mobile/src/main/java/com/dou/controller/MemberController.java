package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dou.constant.MessageConstant;
import com.dou.constant.RedisMessageConstant;
import com.dou.entity.Result;
import com.dou.pojo.Member;
import com.dou.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 处理会员相关操作
 */
@RestController
@RequestMapping("member")
public class MemberController {
    //扫描service
    @Reference
    private MemberService memberService;

    //注入jedis
    @Autowired
    private JedisPool jedisPool;
    //别再忘扫描信息requestbody
    @RequestMapping("/check")
    public Result check(HttpServletResponse response,@RequestBody Map map){

        String telephone = (String) map.get("telephone");
        //从Redis中获取保存的验证码,null 009900
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        String validateCode = (String) map.get("validateCode");
        //将用户输入的验证码和Redis中保存的验证码进行比对validateCodeInRedis != null &&
        if( validateCode != null && validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)) {
            //如果比对成功，调用服务完成预约业务处理
            //判断用户是否为会员
             Member member=memberService.findByTelephone(telephone);
             if (member==null){
                 //不是会员.自动完成注册
                 //设置注册时间
                 member=new Member();
                 member.setRegTime(new Date());
                 member.setPhoneNumber(telephone);
                 memberService.add(member);
             }
             //向客户端浏览器写入cookie,内容为手机号
             Cookie cookie = new Cookie("login_member_telephone", telephone);
             cookie.setPath("/");
             cookie.setMaxAge(60*60*24*30);

             //把cookie返回至前端
             response.addCookie(cookie);
             //将会员信息保存到redis
            String json = JSON.toJSON(member).toString();
            //设置保存30分钟
            jedisPool.getResource().setex(telephone, 60*30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }else {
            //返回验证错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
