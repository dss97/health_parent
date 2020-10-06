package com.dou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.dou.constant.MessageConstant;
import com.dou.constant.RedisConstant;
import com.dou.entity.PageResult;
import com.dou.entity.QueryPageBean;
import com.dou.entity.Result;
import com.dou.pojo.Setmeal;
import com.dou.service.SetmealService;
import com.dou.utils.QiniuUtils;
import com.dou.utils.SMSUtils;
import com.dou.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


    /**
     * @author 窦赛赛
     */
    @RestController
    @RequestMapping("/setmeal")
    public class SetmealController {

        @Reference
        private SetmealService setmealService;

        //使用JedisPool操作Redis服务(还没使用)
        @Autowired
        private JedisPool jedisPool;

        //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        System.out.println("获取的名字"+imgFile);
        String originalFilename = imgFile.getOriginalFilename();//原始文件格式
        int index = originalFilename.indexOf(".");
        String extention = originalFilename.substring(index-1);
        String fileName=UUID.randomUUID().toString()+extention;

        try {
            //将文件上传到七牛云
            System.out.println("上传后的文件名"+fileName);
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //sadd集合
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);

            jedisPool.getResource().hset("filename_hash", fileName, String.valueOf(System.currentTimeMillis()));
        } catch (IOException e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        //上传成功把文件名字带回去
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    //添加套餐信息数据
        @RequestMapping("/add")
        public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
            try {
                setmealService.add(setmeal,checkgroupIds);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
            }
            return  new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页显示查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
          return setmealService.pageQuery(queryPageBean);
    }

        //编辑套餐表单
        @RequestMapping("/findById")
        public Result update(Integer id){
            try {
                Setmeal setmeal=setmealService.findById(id);
                return  new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS,setmeal);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
            }
        }

    //确定之后套餐表单
    @RequestMapping("/edit")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.edit(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return  new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

        @RequestMapping("/findCheckItemIdsByCheckGroupId")
        public Result findCheckItemIdsByCheckGroupId(Integer id){
            try {
                List<Integer> checkGroupIds = setmealService.findCheckItemIdsByCheckGroupId(id);
                //获取到的关联表信息和自身信息都需要result带回去
                return  new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
            }
        }

        //根据id删除套餐表单
        @RequestMapping("/delete")
        public Result delete(Integer id){
            try {
               setmealService.delete(id);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
            }
            return  new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        }

}
