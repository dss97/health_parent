package com.dou.service;

import com.dou.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    public void  add(List<OrderSetting> list);

    public List<Map> getOrderSettingByMonth(String date);

    public boolean editNumberByDate(OrderSetting orderSetting);
}
