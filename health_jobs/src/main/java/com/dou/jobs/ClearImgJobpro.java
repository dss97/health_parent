package com.dou.jobs;

import com.dou.constant.RedisConstant;
import com.dou.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClearImgJobpro {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        //拿到hash中所有的数据
        Map<String, String> filename_hash = jedisPool.getResource().hgetAll("filename_hash");

        //获取当前时间
        Long current = System.currentTimeMillis();
//删除的图片
        List<String> delete = new ArrayList<>();
        //遍历map,拿到每一个数据，判断上传时间，如果上传时间超过1天，删除
        for(String filename : filename_hash.keySet()){
            String time = filename_hash.get(filename);
            Long l = Long.parseLong(time);
            if((current - l) >= 1000L * 60L * 60L * 24L){
                //超过1天
                delete.add(filename);
            }
        }

        for(String fileNameToDelete : delete){
            //1.把七牛云上的图片删除
            QiniuUtils.deleteFileFromQiniu(fileNameToDelete);
            //2.将hash中的数据删除
            jedisPool.getResource().hdel("filename_hash",fileNameToDelete);

            System.out.println("清理垃圾图片---"+fileNameToDelete);
        }
    }
}
