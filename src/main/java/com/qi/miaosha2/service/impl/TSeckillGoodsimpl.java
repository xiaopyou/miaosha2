package com.qi.miaosha2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.qi.miaosha2.entrty.*;
import com.qi.miaosha2.mapper.TOrdermapper;
import com.qi.miaosha2.mapper.TSeckillGoodsmapper;
import com.qi.miaosha2.mapper.TSeckillOrdermapper;
import com.qi.miaosha2.service.TSeckillGoodsservice;
import com.qi.miaosha2.util.RespEntity;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TSeckillGoodsimpl implements TSeckillGoodsservice {
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    RedisTemplate<String,Object> redis;
    @Autowired
    TSeckillGoodsmapper tSeckillGoodsmapper;//秒杀商品表
    @Autowired
    TSeckillOrdermapper tSeckillOrdermapper;//秒杀订单表
    @Autowired
    TOrdermapper tOrdermapper;//订单表
    ReentrantLock lock = new ReentrantLock();

    @Transactional //事务
    @Override
    public RespEntity scdindan(String user_id, TGoods tGoods) {//后面进行升级 加乐观锁 做限流做重试机制

        synchronized (TSeckillGoodsimpl.class){
            ValueOperations valueOperations= redis.opsForValue();
            //查询秒杀商品表拿到当前的秒杀商品 (为什么把自己用前端传过来的库存 因为不安全数据不准确)
            TSeckillGoods tSeckillGoods = tSeckillGoodsmapper.selectOne(new QueryWrapper<TSeckillGoods>().eq("goods_id",tGoods.getId()));
            //扣减当前商品的库存并且去更新数据库 //加唯一索引防止一个用户抢多个商品 因为我们是高并发项目无法避免同一时间1个用户发送了多个请求导致生成多个订单 mst
            tSeckillGoods.setStockCount(tSeckillGoods.getStockCount()-1);
            //在删除库存之前在判断一次库存是否还有防止超卖 （这里看的库存是秒杀商品表的库存）sb库存都在商品表或者秒杀商品表 （根据商品表的商品id去找对应的秒杀商品并且去扣减库存 并且库存要大于0）
            TSeckillGoods tSeckillGoods1=   tSeckillGoodsmapper.selectOne(new QueryWrapper<TSeckillGoods>().eq("goods_id",tGoods.getId()).gt("stock_count",0));
            if (tSeckillGoods1.getStockCount()<1)//tSeckillGoods1.getStockCount()<=0
            {
                valueOperations.set("isStockEmpty:"+tGoods.getId(),"0");//判断是否有库存（）
                return new RespEntity(4001,"没有库存");
            }
            //修改库存
            tSeckillGoodsmapper.updateById(tSeckillGoods);
            //如果删除库存什么没有错误那么我们就生成订单 最后并返回订单消息给前端

            return new RespEntity(200,"成功",schandindan(tGoods,user_id,tSeckillGoods));

        }



    }

    //生产订单
    public TOrder schandindan(TGoods tOrder1 ,String user_id,TSeckillGoods tSeckillGoods){
        //生成订单表 在生成秒杀订单表因为他们之间有关联 订单表的订单id与秒杀表订单id字段是有关联的
        TOrder tOrder=new TOrder();
//        tOrder.setId(); 不需要自生成id 订单ID
        tOrder.setUserId(Long.valueOf(user_id));//用户ID
        tOrder.setGoodsId(tOrder1.getId());//商品ID
        tOrder.setDeliveryAddrId(0L);//收获地址ID
        tOrder.setGoodsName(tOrder1.getGoodsName());//商品名字
        tOrder.setGoodsCount(1);//购买的商品数量
        tOrder.setGoodsPrice(tSeckillGoods.getSeckillPrice());//商品价格  填秒杀商品价格 因为我们是秒杀订单
        tOrder.setOrderChannel(1);//1 pc,2 android, 3 ios
        tOrder.setStatus(0);//订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退货，5已完成
        tOrder.setCreateDate(new Date());//订单创建时间
//        tOrder.setPayDate(); 我们是创建未支付所以不用
        tOrdermapper.insert(tOrder);//添加订单表
        //生成秒杀订单表
        TSeckillOrder tSeckillOrder=new TSeckillOrder();
//        tSeckillOrder.setId();//秒杀订单ID  自动生成
        tSeckillOrder.setUserId(Long.valueOf(user_id));//用户ID
        tSeckillOrder.setOrderId(tOrder.getId());//  订单ID 就是指向订单表的订单ID
        tSeckillOrder.setGoodsId(tOrder1.getId());//商品ID
        tSeckillOrdermapper.insert(tSeckillOrder);

        redis.opsForValue().set("order"+user_id+":"+tOrder1.getId(),tSeckillOrder);//把秒杀订单的消息存进redis里面

        return tOrder;
    }


    public RespEntity scdindanrguanshu(String user_id, TGoods tGoods) {//后面进行升级 加分布式锁 还是有过期时间问题 看门狗解决或者设置一个唯一的id或者版本号
        String wyid= UUID.randomUUID().toString();

        try {
            boolean suo= redis.opsForValue().setIfAbsent("lockKey",lock,30, TimeUnit.SECONDS);//不能有重复的key  实现一把简单的分布式锁     redis.expire("lockKey",30);//过期时间
            if (suo){

                ValueOperations valueOperations= redis.opsForValue();
                //查询秒杀商品表拿到当前的秒杀商品 (为什么把自己用前端传过来的库存 因为不安全数据不准确)
                TSeckillGoods tSeckillGoods = tSeckillGoodsmapper.selectOne(new QueryWrapper<TSeckillGoods>().eq("goods_id",tGoods.getId()));

                //扣减当前商品的库存并且去更新数据库 //加唯一索引防止一个用户抢多个商品 因为我们是高并发项目无法避免同一时间1个用户发送了多个请求导致生成多个订单 mst
                tSeckillGoods.setStockCount(tSeckillGoods.getStockCount()-1);
                //在删除库存之前在判断一次库存是否还有防止超卖 （这里看的库存是秒杀商品表的库存）sb库存都在商品表或者秒杀商品表 （根据商品表的商品id去找对应的秒杀商品并且去扣减库存 并且库存要大于0）
                TSeckillGoods tSeckillGoods1=   tSeckillGoodsmapper.selectOne(new QueryWrapper<TSeckillGoods>().eq("goods_id",tGoods.getId()).gt("stock_count",0));

                if (tSeckillGoods1.getStockCount()<1)//tSeckillGoods1.getStockCount()<=0
                {
                    valueOperations.set("isStockEmpty:"+tGoods.getId(),"0");//判断是否有库存
                    return new RespEntity(4001,"没有库存");
                }


                //修改库存
//    boolean pd=  tSeckillGoodsmapper.update(new UpdateWrapper<TSeckillGoods>().setSql("stock_count="+"stock_count-1").eq("goods_id",tGoods.getId()).gt("stock_count",0));
                tSeckillGoodsmapper.updateById(tSeckillGoods);
                //如果删除库存什么没有错误那么我们就生成订单 最后并返回订单消息给前端
                System.out.println(schandindan(tGoods,user_id,tSeckillGoods));
                return new RespEntity(200,"成功",schandindan(tGoods,user_id,tSeckillGoods));
            }

        }finally {
            if (wyid.equals(redis.opsForValue().get("lockKey"))){//判断 如果相同说明是当前锁那就释放 如果不相同那就不是当前线程加的锁 (为了解决因为过期时间导致解开了别人的锁)
                redis.delete("lockKey");//最后一定要释放锁
            }

        }




    return null;


    }
    @Autowired
    Redisson redisson;

    public RespEntity redissonsuo(String user_id, TGoods tGoods) {//redisson 如果设置了主从复制还是会有问题如果 用红锁（一般不用）
        String wyid= UUID.randomUUID().toString();
        RLock rLock=redisson.getFairLock(wyid);
        try {
            rLock.lock();//加锁

                ValueOperations valueOperations= redis.opsForValue();
                //查询秒杀商品表拿到当前的秒杀商品 (为什么把自己用前端传过来的库存 因为不安全数据不准确)
                TSeckillGoods tSeckillGoods = tSeckillGoodsmapper.selectOne(new QueryWrapper<TSeckillGoods>().eq("goods_id",tGoods.getId()));

                //扣减当前商品的库存并且去更新数据库 //加唯一索引防止一个用户抢多个商品 因为我们是高并发项目无法避免同一时间1个用户发送了多个请求导致生成多个订单 mst
                tSeckillGoods.setStockCount(tSeckillGoods.getStockCount()-1);
                //在删除库存之前在判断一次库存是否还有防止超卖 （这里看的库存是秒杀商品表的库存）sb库存都在商品表或者秒杀商品表 （根据商品表的商品id去找对应的秒杀商品并且去扣减库存 并且库存要大于0）
                TSeckillGoods tSeckillGoods1=   tSeckillGoodsmapper.selectOne(new QueryWrapper<TSeckillGoods>().eq("goods_id",tGoods.getId()).gt("stock_count",0));

                if (tSeckillGoods1.getStockCount()<1)//tSeckillGoods1.getStockCount()<=0
                {
                    valueOperations.set("isStockEmpty:"+tGoods.getId(),"0");//判断是否有库存
                    return new RespEntity(4001,"没有库存");
                }


                //修改库存
//    boolean pd=  tSeckillGoodsmapper.update(new UpdateWrapper<TSeckillGoods>().setSql("stock_count="+"stock_count-1").eq("goods_id",tGoods.getId()).gt("stock_count",0));
                tSeckillGoodsmapper.updateById(tSeckillGoods);
                //如果删除库存什么没有错误那么我们就生成订单 最后并返回订单消息给前端
                System.out.println(schandindan(tGoods,user_id,tSeckillGoods));
                return new RespEntity(200,"成功",schandindan(tGoods,user_id,tSeckillGoods));


        }finally {
            rLock.unlock();//释放

        }



    }


}
