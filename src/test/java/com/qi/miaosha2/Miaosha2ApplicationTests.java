package com.qi.miaosha2;

import com.qi.miaosha2.controller.cs;
import com.qi.miaosha2.entrty.TUser;
import com.qi.miaosha2.entrty.tgoodss;
import com.qi.miaosha2.mapper.TGoodsmapper;
import com.qi.miaosha2.pojo.rabbitmq.MQReceiver;
import com.qi.miaosha2.service.TSeckillOrderservice;
import com.qi.miaosha2.service.impl.BatchInsertServiceImpl;
import com.qi.miaosha2.util.MD5util;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class Miaosha2ApplicationTests {

    @Autowired
    com.qi.miaosha2.controller.cs cs;
    @Autowired
    TGoodsmapper tGoodsmapper;
    @Resource
    RedisTemplate<Object,Object> redis;
    @Autowired
    TSeckillOrderservice tSeckillOrderservice;
    @Test
    void contextLoads() {
        TUser tUser=new TUser();
        tUser.setId(18017771667l);
        String qd= "123456"+"qiyongle";
        MD5util md51=new MD5util();
        String jiam1= md51.md5(qd);
        tUser.setPassword("bb10dbc28b0c6dafe7408ada316bf79e");
        cs.cs(tUser);
    }
    @Test
    void sss(){
//        List<tgoodss> ss=tGoodsmapper.splbiao();
//        System.out.println(ss);
        cs.kucyel();


    }
    @Test
    void redislock(){
       ValueOperations valueOperations= redis.opsForValue();
       Boolean islock= valueOperations.setIfAbsent("k1","v1");//如果当前key不存在才可以删除成功
        //如果占位成功那么删除锁
            if (islock){

             valueOperations.set("name","xxx");
             String name= (String)valueOperations.get("name");
             System.out.println(name);
             //删除锁 也就是释放锁 如果不去删除锁那么其他线程也就无法去获取当前锁
            redis.delete("k1");
         }else {

                System.out.println("已经被占用当前锁");

            }
    }
    @Test
    void redislock2(){//
        ValueOperations valueOperations= redis.opsForValue();
        //获取锁
        Boolean islock= valueOperations.setIfAbsent("k1","v1",5, TimeUnit.SECONDS);// 设置过期时间 (作用保证锁一定会被释放 但是要注意如果设置的过期时间不对那么可能会导致后面的锁会被前面的锁删除导致混乱)
        //前面的
        //如果占位成功那么删除锁 好像lock锁
        if (islock){//比较锁

            valueOperations.set("name","xxx");
            String name= (String)valueOperations.get("name");
            System.out.println(name);
            Integer.parseInt("xxxxx");//抛异常
            //删除锁 也就是释放锁 如果不去删除锁那么其他线程也就无法去获取当前锁
            redis.delete("k1");
        }else {

            System.out.println("已经被占用当前锁");

        }
    }

    @Test
    void redislock3(){//使用lue脚本
      String value= UUID.randomUUID().toString();//生成一个唯一的value key也行 主要看场景
        ValueOperations valueOperations= redis.opsForValue();
        //获取锁  设置过期时间 (作用保证锁一定会被释放 但是要注意如果设置的过期时间不对那么可能会导致前面一个锁释放锁的是第2个锁) 解决方法可以设置一个唯一的状态去判断是否改变如果改变了话就说明
        Boolean islock= valueOperations.setIfAbsent("k1",value,5, TimeUnit.SECONDS);//这个方法就像一个  synchronized (mykz.lock){} 一样
        //如果占位成功那么删除锁 好像lock锁
        if (islock){//比较锁

            valueOperations.set("name","xxx");
            String name= (String)valueOperations.get("name");
            System.out.println(name);
            System.out.println(valueOperations.get("k1"));
            Integer.parseInt("xxxxx");//抛异常
            //删除锁 也就是释放锁 如果不去删除锁那么其他线程也就无法去获取当前锁
            redis.delete("k1");
        }else {

            System.out.println("已经被占用当前锁");

        }
    }
    @Resource
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    com.qi.miaosha2.mapper.tusermapper tusermapper;
    @Autowired
    BatchInsertServiceImpl batchInsertService;
    @Test
    void eqwe(){
//        redisTemplate.opsForValue().set("hello","lllllll");

        String a=null;
        System.out.println(a);

    }
    @Test
    public void createUser(){//分批次插入数据
        List<TUser> users=new ArrayList<>();
        Integer q=0;
        for(long i=0;i<1000;i++){

            TUser user=new TUser();
            user.setId(1300000000l+i);
            user.setNickname("adim"+i);
            user.setPassword("1234");
            user.setSalt("qiyongle");
            user.setHead("1.jpg");
            user.setRegisterDate(new Date());
//            user.setLastLoginDate();
            user.setLoginCount(1);
            users.add(user);

//            tusermapper.insert(user);
        }
//        boolean b =  batchInsertService.saveBatch(users);

        tusermapper.pilianzhul(users);


    }
    @Resource
    RabbitTemplate rabbitTemplate;
    @Autowired
    MQReceiver mqReceiver;
//    @Test
//    public void testSendMessage() {
//        for (int i = 0; i < 5; i++) {
//            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
//            rabbitTemplate.convertAndSend("normal.exchange", "normal",i);
////                    new User("baobao" + i, 18, new Date()), correlationData);
//        }
//    }

    @Test
    public void  ss(){
        String asd=null;
        String aa=new String(asd);
        HashSet<String> list=new HashSet<>();
        Iterator<String> it=list.iterator(); //获取当前集合里面的值
        while (it.hasNext()){//it.hasNext() 迭代器 是否还有元素
         String ele= it.next(); //it.next() 获取当前集合值
            if (ele.contains("sb")){ // 判断当前值是否为 sb如果是就true没有false
it.remove();

            }

        }

    }


}
