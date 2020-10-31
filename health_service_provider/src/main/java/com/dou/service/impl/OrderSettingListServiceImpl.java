package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.constant.MessageConstant;
import com.dou.dao.MemberDao;
import com.dou.dao.OrderDao;
import com.dou.dao.OrderSettingDao;
import com.dou.dao.OrderSettingListDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Member;
import com.dou.pojo.Order;
import com.dou.pojo.OrderSetting;
import com.dou.pojo.Setmeal;
import com.dou.service.OrderSettingListService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = OrderSettingListService.class)
@Transactional
public class OrderSettingListServiceImpl implements OrderSettingListService {

    @Autowired
    private OrderSettingListDao orderSettingListDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Order> page=orderSettingListDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Member> findmember() {
        return orderSettingListDao.findmember();
    }

    @Override
    public List<Setmeal> findsetmeal() {
        return orderSettingListDao.findsetmeal();
    }

    @Override
    public Result add(Order order){
        Date orderDate = order.getOrderDate();//预约日期
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        //看看日期是否进行预约设置
        if(orderSetting == null){
            //指定日期没有进行预约设置，无法完成体检预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if(reservations >= number){
            //已经约满，无法预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        Integer id = order.getMemberId();
        Integer setmealId= order.getSetmealId();
        Member member = memberDao.findById(id);
        if(member != null) {
            //判断是否在重复预约
            Integer memberId = member.getId();//会员ID
            Order order1 = new Order(memberId, orderDate,setmealId);
            //根据条件进行查询
            List<Order> list = orderDao.findByCondition(order1);
            if (list != null && list.size() > 0) {
                //说明用户在重复预约，无法完成再次预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }else {
            /*//4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            String UUIDName = UUID.randomUUID().toString().replace("-", "");
            member.setName("demo");
            member.setPhoneNumber(UUIDName);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//自动完成会员注册
            //新增会员之后还要再查一次
            member = memberDao.findByTelephone(telephone);*/
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS);
        }

//5、预约成功，更新当日的已预约人数
        orderSettingListDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        //更新预约人数
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS);
    }

    @Override
    public void edit(Order order) {
        orderSettingListDao.edit(order);
    }

    @Override
    public Order findByid(Integer id) {
        return orderSettingListDao.findById(id);
    }

    @Override
    public void delete(Integer id) {
        orderSettingListDao.delete(id);
    }

    @Override
    public String findmemberById(Integer id) {
        return orderSettingListDao.findmemberById(id);
    }

    @Override
    public String findsetmealById(Integer id) {
        return orderSettingListDao.findsetmealById(id);
    }
}
