package com.dou.jobs;

import com.dou.constant.RedisConstant;
import com.dou.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义Job
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        System.out.println("基础版运行");
        //根据redis中保存的两个set集合进行差值计算,获得垃圾图片名称集合
        //Redis Sdiff 命令返回给定集合之间的差集,不存在的集合 key 将视为空集,差集的结果来自前面的 FIRST_KEY
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set != null) {
            for (String picName : set) {
                //删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //从redis集合中删除图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
                System.out.println("删除图片:--"+picName);
            }
        }
        }

}
