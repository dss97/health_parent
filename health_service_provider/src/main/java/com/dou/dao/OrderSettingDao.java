package com.dou.dao;

import com.dou.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    public void add(OrderSetting orderSetting);
    public void editNumberByOrderDate(OrderSetting orderSetting);
    public long findCountByOrderDate(Date orderDate);
    public List<OrderSetting> getOrderSettingByMonth(Map<String,String> map);
    public Integer findCountByOrderReservations(Date orderDate);

    public OrderSetting findByOrderDate(Date orderdate);

    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
