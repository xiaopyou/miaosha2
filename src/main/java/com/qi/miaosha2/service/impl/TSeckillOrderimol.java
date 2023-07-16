package com.qi.miaosha2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qi.miaosha2.entrty.TSeckillOrder;
import com.qi.miaosha2.mapper.TSeckillOrdermapper;
import com.qi.miaosha2.service.TSeckillOrderservice;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TSeckillOrderimol implements TSeckillOrderservice {
    @Autowired
    TSeckillOrdermapper tSeckillOrdermapper;
    @Resource
    RedisTemplate<String,Object> redis;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Override
    public Long getResult(String userid, String goodsid) {//为什么要做轮询因为可能有些订单还在抢商品
//       0 没抢到 -1 还在抢 订单ID 抢到了
        //其实redis里面已经有秒杀订单可以直接用
        TSeckillOrder tSeckillOrder=   tSeckillOrdermapper.selectOne(new QueryWrapper<TSeckillOrder>().eq("user_id",userid).eq("goods_id",goodsid));
        if (tSeckillOrder !=null){

            return tSeckillOrder.getOrderId();//生成了用户订单

        }else if (redis.hasKey("isStockEmpty:"+goodsid)){//（判断当前reids里面有没有这个key有就返回true） 这里是判断有没有生成当前用户的订单 没库存了

            return -1l;
        } else {

           return 0l;//当前用户没抢到
        }


    }

}
