package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.dao.OrderSettingDao;
import com.dou.pojo.OrderSetting;
import com.dou.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    //添加预约设置
    @Override
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());

                if (countByOrderDate > 0) {
                    //当前日期已经进行了预约设置，需要执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);

                } else {
                    //如果没有进行预约设置,进行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据月份查询对应预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {//格式:yyyy-MM
        String begin = date + "-1";
        String end = date + "-31";
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        //调用dao根据月份查询预约数据
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        //返回map集合形式
        List<Map> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("date", orderSetting.getOrderDate().getDate());
                maps.put("number", orderSetting.getNumber());
                maps.put("reservations", orderSetting.getReservations());
                result.add(maps);
            }
        }
        return result;
    }

    @Override
    public boolean editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        long count = orderSettingDao.findCountByOrderDate(orderDate);
            //判定已经预约的数量
            if (count > 0) {
                Integer reservations = orderSettingDao.findCountByOrderReservations(orderDate);
                if (orderSetting.getNumber() >= reservations) {
                    //已经进行了预约设置,执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    return false;
                }
            } else {
                //当前日期没有就那些预约设置，需要执行插入操作
                orderSettingDao.add(orderSetting);
            }
        return true;
    }
}