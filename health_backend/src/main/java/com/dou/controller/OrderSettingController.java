package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dou.constant.MessageConstant;
import com.dou.entity.Result;
import com.dou.pojo.OrderSetting;
import com.dou.service.OrderSettingService;
import com.dou.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 * @author 窦赛赛
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;
//文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) throws ParseException {
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> data =new ArrayList<>();
            for (String[] strings : list) {
                String orderData = strings[0];
                String number = strings[1];
           //少了个M
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                Date  date = format.parse(orderData);

                OrderSetting orderSetting = new OrderSetting(date,Integer.parseInt(number));
                data.add(orderSetting);
            }
            //通过dubbo远程调用服务实现数据批量导入到数据库
            orderSettingService.add(data);
            return  new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
    //根据月份查询对应的预约设置数据(date:2020-0)
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        //要接收一下--并且,只要是有数据传递修改等行为,就必须要try catch判定一下是否成功
        try {
            List<Map> list=orderSettingService.getOrderSettingByMonth(date);
                return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //参数传递回去前端
            return  new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    //修改可预约人数
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        //要接收一下--并且,只要是有数据传递修改等行为,就必须要try catch判定一下是否成功
        try {
            boolean result = orderSettingService.editNumberByDate(orderSetting);
            if (result){
                return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
            }else {
                return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAILS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //参数传递回去前端
            return  new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
