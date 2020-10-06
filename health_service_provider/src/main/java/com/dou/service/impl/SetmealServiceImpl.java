package com.dou.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dou.constant.RedisConstant;
import com.dou.dao.SetmealDao;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.pojo.Setmeal;
import com.dou.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检套餐服务
 * */
@Service(interfaceClass = SetmealService.class)
@Transactional
public  class SetmealServiceImpl implements SetmealService{

    @Autowired
    private SetmealDao setmealDao;
    //新增套餐信息,关联检查组
    @Autowired
    private JedisPool jedisPool;

    /*配置文件里配置的这个信息*/
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String outPutPath; //从属性文件中读取要生成的html对应的目录

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
     setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId, checkgroupIds);

        //将图片名称保存到redis集合中
        String fileName = setmeal.getImg();
        savePic2Redis(fileName);

        //当添加套餐后需要重新生成静态页面(套餐列表页面,套餐详情页面)
        //当有数据变化的时候,重新生成静态页面
        generateMobileStaticHtml();

    }
    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
        //1.在生成静态页面之前需要查询数据
        List<Setmeal> list = setmealDao.findAll();

        //2.需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);

        //3.需要生成套餐详情静态页面
        generateMobileSetmealDatailHtml(list);
    }

    //每个方法职责单一,解耦合
    //生成套餐列表静态页面
    public void  generateMobileSetmealListHtml(List<Setmeal> list){
        Map map = new HashMap();
        //为模板提供Map数据
        map.put("setmealList", list);
        //前为写好的模板名字,后面的页面名字自定义
        generateHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
}

    //生成套餐详细信息静态页面--可能有多个
    public void generateMobileSetmealDatailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map=new HashMap();
            map.put("setmeal", setmealDao.findByIdmobile(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_"+setmeal.getId()+".html", map);
        }
    }

    //通用的方法---这里生成静态页面
    //传入参数(模板名称--生成内容--页面所需数据)
    public void  generateHtml(String telateName, String htmlPageName, Map map){
        Writer writer=null;
        //获得配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            //添加模板名称
            Template template = configuration.getTemplate(telateName);
            //准备输出流,输出静态页面
            writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outPutPath+"/"+htmlPageName)),"utf-8"));  //在后面追加一个utf-8，指定下编码集
            //输出
            template.process(map, writer);
            //关闭流
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //套餐数据分页
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Setmeal> page =setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //更新套餐信息
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //先修改基础信息
     setmealDao.edit(setmeal);
     //根据id删除套餐和检查组链接
        setmealDao.deleteAssoication(setmeal.getId());
        //然后再根据id建立新的链接
        Integer setmealId = setmeal.getId();
        setSetmealAndCheckGroup(setmealId, checkgroupIds);
    }

    //根据id删除套餐组
    @Override
    public void delete(Integer id) {
        //先删关联组
        setmealDao.deleteAssoication(id);
        //后删数据
        setmealDao.delete(id);
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /*@Override
    public Setmeal findById2(Integer id) {

        return setmealDao.findById2(id);
       *//* //1.查询套餐信息
        Setmeal setmeal = setmealDao.findSetmealById(id);
        //2.查询套餐对应的检查组数据
        List<CheckGroup> checkGroups = checkGroupDao.findBySetmealId(id);
        setmeal.setCheckGroups(checkGroups);
        //3.根据检查组查询对应的检查项
        if(checkGroups != null && checkGroups.size() > 0){
            for (CheckGroup checkGroup : checkGroups) {
                //获取到每一个检查组对应的检查项，并且设置到检查组中
                List<CheckItem> checkitems = checkItemDao.findByCheckGroupId(checkGroup.getId());
                checkGroup.setCheckItems(checkitems);
            }
        }
        return setmeal;*//*
    }*/


    /*移动端查询全部套餐信息*/
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return setmealDao.findsetmealIdsByCheckGroupId(id);
    }

    /*移动端搜索*/
    @Override
    public Setmeal findByIdmobile(int id) {
        return setmealDao.findByIdmobile(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String fileName){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    //多表链接
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
        if (checkgroupIds !=null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmealId" , setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
