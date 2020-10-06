package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.dou.constant.MessageConstant;
import com.dou.constant.RedisMessageConstant;
import com.dou.entity.Result;
import com.dou.pojo.Member;
import com.dou.pojo.Order;
import com.dou.service.OrderService;
import com.dou.utils.SMSUtils;
import com.dou.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/order")
public class OrderController {

@Reference
private OrderService orderService;

@Autowired
private JedisPool jedisPool;
    //发送验证码
    @RequestMapping("/sms")
    public Result sms(String telephone){
        String key = telephone + RedisMessageConstant.SENDTYPE_ORDER;
        String validatecode= ValidateCodeUtils.generateValidateCode(6).toString();
        //获取信息中的key对应时间,第一次发送不判定
        Long ttl = jedisPool.getResource().ttl(key);
        if(ttl >80){
            return new Result(false, "两分钟内不能重复点击");
        }
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validatecode);
        } catch (ClientException e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis上
        //setex(设置的值保存指定的时间)key上加验证
        jedisPool.getResource().setex(key, 200,validatecode);
        return  new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS,validatecode);
}

    //进行预约信息添加数据库
    @RequestMapping("/submit")
    public Result  orderinfo(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
   //从Redis中获取保存的验证码,null 009900
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");

        //将用户输入的验证码和Redis中保存的验证码进行比对validateCodeInRedis != null &&
        if( validateCode != null && validateCode.equals(validateCodeInRedis)){
            //如果比对成功，调用服务完成预约业务处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型，分为微信预约、电话预约
            Result result = new Result(false, MessageConstant.VALIDATECODE_ERROR);
            try{
                //调用方法
                result = orderService.order(map);//通过Dubbo远程调用服务实现在线预约业务处理
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()){
                //预约成功，可以为用户发送短信
                try{
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return result;
        }else {
            //如果比对不成功，返回结果给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        //页面要map信息
        try {
            Map map=orderService.findById(id);
            //把map信息带回去
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }


    //移动端用户快速登录发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        String key = telephone + RedisMessageConstant.SENDTYPE_LOGIN;
        String validatecode= ValidateCodeUtils.generateValidateCode(6).toString();
        //获取信息中的key对应时间,第一次发送不判定
        Long ttl = jedisPool.getResource().ttl(key);
        if(ttl >80){
            return new Result(false, "两分钟内不能重复点击");
        }
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validatecode);
        } catch (ClientException e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis上
        //setex(设置的值保存指定的时间)key上加验证
        jedisPool.getResource().setex(key, 200,validatecode);
        return  new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS,validatecode);
    }
}
