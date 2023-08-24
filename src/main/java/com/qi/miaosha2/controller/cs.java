package com.qi.miaosha2.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.qi.miaosha2.entrty.*;
import com.qi.miaosha2.mapper.TGoodsmapper;
import com.qi.miaosha2.mapper.TSeckillGoodsmapper;
import com.qi.miaosha2.mapper.TSeckillOrdermapper;
import com.qi.miaosha2.pojo.DTO.SeckillMessage;
import com.qi.miaosha2.pojo.DTO.diandancha;
import com.qi.miaosha2.pojo.rabbitmq.MQSender;
import com.qi.miaosha2.service.TSeckillGoodsservice;
import com.qi.miaosha2.service.TSeckillOrderservice;
import com.qi.miaosha2.service.tuserservice;
import com.qi.miaosha2.util.MD5util;
import com.qi.miaosha2.util.RespEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("cs")
public class cs   {
  @Autowired
com.qi.miaosha2.service.tuserservice tuserservice;
  @Autowired
  TGoodsmapper tGoodsmapper;//商品表
  @Autowired
  TSeckillGoodsservice tSeckillGoodsservice;//秒杀商品表 service
  @Autowired
  TSeckillGoodsmapper tSeckillGoodsmapper;//秒杀订单表
  @Autowired
  TSeckillOrdermapper tSeckillOrdermapper;//秒杀订单表
  @Autowired
  TSeckillOrderservice tSeckillOrderservice;//秒杀订单表 service
  @Resource
  RedisTemplate<String,Object> redis;
  @Autowired
  MQSender mqSender;
  private Map<Long,Boolean> EmptyStocMap =new HashMap<>();//标记reids库存是否还有 有库存 false 没有 true
  public static final ThreadLocal t= new ThreadLocal();
@PostMapping("dlu")
  public RespEntity cs(@RequestBody TUser tUser){
  System.out.println(tUser);

  return tuserservice.dlu(tUser);
  }

  @GetMapping("dlu2/{id}/{password}")
  public RespEntity cs2(@PathVariable  String id ,@PathVariable  String password){


    return tuserservice.dlucs(id,password);
  }
  //加载商品
  @GetMapping("zhi")
  public RespEntity zhi(){

  return new RespEntity(200,"cg",tGoodsmapper.splbiao());
  }
  //加载秒杀商品
  @PostMapping("miaoszhi/{id}")
  public RespEntity  miaozhi(@PathVariable  String id){
  System.out.println(id);
    tgoodss ss=tGoodsmapper.spmiaos(id);
    //判断时间 （秒杀未开始=0 秒杀开启=1 秒杀中=2 秒杀结束=3） 把他们的状态关给前端去判断
    System.out.println(ss.getStartDate()+"sjian");
    return new RespEntity(200,"cg",ss);
  }




  // Guava令牌桶：每秒放行10个请求
  RateLimiter rateLimiter = RateLimiter.create(100);

  // 延时时间：预估读数据库数据业务逻辑的耗时，用来做缓存再删除
  private static final int DELAY_MILLSECONDS = 1000;
  @PostMapping("/{path}/miaoshaxiadan")
  public RespEntity miaoshaxiadan (@PathVariable String path, @RequestBody diandancha diandanchaz)
  {
    //进行令牌桶限流
    // 1. 阻塞式获取令牌
//   rateLimiter.acquire();
    // 2. 非阻塞式获取令牌
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {

            return new RespEntity(4009,"你被限流了");
        }

    //验证接口也就是隐藏秒杀地址
////    String dz= (String) redis.opsForValue().get("seckillPath"+diandanchaz.getUserid()+":"+diandanchaz.getGoodsid());
//    System.out.println(path);
//    boolean dzpd=yanzdz(diandanchaz.getUserid(),diandanchaz.getGoodsid(),path);
//    if (dzpd){
//
//      return new RespEntity(4001,"地址不正确");
//    }

    System.out.println(diandanchaz.toString());
    //判断当前是否还有库存(这里判断的是商品表的库存)
    tgoodss kuc=tGoodsmapper.spmiaos(diandanchaz.getGoodsid());
    if (kuc.getGoodsStock()<=0)
    {
      return new RespEntity(4001,"没有库存");
    }
    //判断当前用户是否是第一次购买（判断秒杀订单表是否存在如果存在就说明当前用户已经买了一件商品）
//    TSeckillOrder tSeckillOrder=  tSeckillOrdermapper.selectOne(new QueryWrapper<TSeckillOrder>().eq("goods_id",diandanchaz.getGoodsid()).eq("user_id",diandanchaz.getUserid()));
//    if (tSeckillOrder!=null){
//
//      return new RespEntity(4002,"您已经购买一次了不可以再买");
//    }
    TSeckillOrder tSeckillOrder1=  (TSeckillOrder)redis.opsForValue().get("order"+diandanchaz.getUserid()+":"+diandanchaz.getGoodsid());
    if (tSeckillOrder1!=null){

      return new RespEntity(4002,"您已经购买一次了不可以再买");
    }
    //判断验证码（我不想写了）

    //接口隐藏

    //生成秒杀订单 并且去减库存
    System.out.println(diandanchaz.getGoodsid());
    TGoods tGoods=  tGoodsmapper.selectOne(new QueryWrapper<TGoods>().eq("id",diandanchaz.getGoodsid()));
  return new RespEntity(200,"成功",tSeckillGoodsservice.scdindan(diandanchaz.getUserid(),tGoods));


  }




//private static final int cs222=0;

  @PostMapping("/{path}/miaoshaxiadan2")
  @ResponseBody
  public RespEntity miaoshaxiadan2 (@PathVariable String path, @RequestBody diandancha diandanchaz)
  {
    //进行令牌桶限流
    // 1. 阻塞式获取令牌
//   rateLimiter.acquire();
    // 2. 非阻塞式获取令牌
    if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {

      return new RespEntity(4009,"你被限流了");
    }

    //验证接口也就是隐藏秒杀地址
    System.out.println(path);
    boolean dzpd=yanzdz(diandanchaz.getUserid(),diandanchaz.getGoodsid(),path);//有问题
    System.out.println(dzpd);
    if (dzpd){

      return new RespEntity(4001,"地址不正确");
    }
    //判断当前是否还有库存(这里判断的是商品表的库存)
    tgoodss kuc=tGoodsmapper.spmiaos(diandanchaz.getGoodsid());

    if (kuc.getStockCount()<1)
    {
      return new RespEntity(4001,"没有库存");
    }
    System.out.println(kuc.getStockCount()+"sdhasjkdashkdhaskdhkashda");
   //判断是否是重复购买  wt
    TSeckillOrder tSeckillOrder1=  (TSeckillOrder)redis.opsForValue().get("order"+diandanchaz.getUserid()+":"+diandanchaz.getGoodsid());
    if (tSeckillOrder1!=null){

      return new RespEntity(4002,"您已经购买一次了不可以再买");
    }
    System.out.println("没有重复购买");

//    seckillGoods.getId()
//    if (EmptyStocMap.get(diandanchaz.getGoodsid())){// 通过内存标记 减少reids的访问 （当redsi里面没库存时就不会去访问redis了） （好像加乐观锁）
//       return new RespEntity(4006,"减少reids的访问") ;
//    }
//    System.out.println("弗萨");

    ValueOperations valueOperations=  redis.opsForValue();
    //先判断扣减reids里库存是否还有 这里有问题扣减库存没加锁可能会出现redis超扣问题这样我的MQ会生成重复数据 加锁或者MQ判断生成的消息是否重复
   Long stock= valueOperations.decrement("seckillGoods:"+diandanchaz.getGoodsid());//扣减对应秒杀商品数量 根据前端传来的商品id来找
    System.out.println(stock+"扣减redis");
    System.out.println(diandanchaz.getGoodsid()+"sdsadasdsadasdasdasdasdas");
    if(stock<1){
      EmptyStocMap.put(kuc.getId(),true);
      valueOperations.increment("seckillGoods:"+diandanchaz.getGoodsid());
      return new RespEntity(4004,"预减库存没有库存");
  }

    SeckillMessage seckillMessage=  new SeckillMessage(diandanchaz.getUserid(),diandanchaz.getGoodsid());//
    Gson gson=new Gson();
//    mqSender.sendSeckillMessage(gson.toJson(seckillMessage));//发送消息 通过MQ生成订单
    mqSender.sendSeckillMessage(seckillMessage);
    return new RespEntity(200,"正在排队中");
  }



//  @Scheduled(cron = "59 1 * * * ?") //10.30
  @Scheduled(fixedRate = 120000)
  public void kucyel(){//初始话方法 也就是库存预热
    //把商品库存添加到缓存

    //查询出所有秒杀商品库存
  List<TSeckillGoods>  tSeckillGoods1 = tSeckillGoodsmapper.selectList(new QueryWrapper<>());
System.out.println("rle111111111111111");
    for (TSeckillGoods seckillGoods : tSeckillGoods1) {

      redis.opsForValue().set("seckillGoods:"+seckillGoods.getId(),seckillGoods.getStockCount());
      EmptyStocMap.put(seckillGoods.getId(),false);//生成
    }

  }

  //生成秒杀接口
  @PostMapping("scdz")
  public RespEntity scdz (@RequestBody diandancha diandanchaz)
  {

    String str=MD5util.md5(UUID.randomUUID().toString()+"123456");//UUID.randomUUID()+123456
    redis.opsForValue().set("seckillPath"+diandanchaz.getUserid()+":"+diandanchaz.getGoodsid(),str,30,TimeUnit.SECONDS);//存进redis里面以后用来做判断

    return new RespEntity(200,"生成接口成功",str);
  }

  //验证秒杀地址
  public boolean yanzdz(String userid ,String goodsid,String reidsid){//reidsid
    System.out.println(userid+goodsid+reidsid+"sdasdasdasdekjlwlkejrwljrlkewjrlewjrjewioj");

    String pd=(String) redis.opsForValue().get("seckillPath"+userid+":"+goodsid);
    return reidsid.equals(pd);

  }

  //查询是否生成订单
  @GetMapping("cxddsc/{userid}/{goodsid}")
  public RespEntity cxddsc(@PathVariable String userid,@PathVariable String goodsid){
    System.out.println(userid+goodsid+"查询是否生成订单");
  if (userid==null && goodsid==null){

    return new RespEntity(4111,"出错") ;
  }
    System.out.println(tSeckillOrderservice.getResult(userid,goodsid));
   return  new RespEntity(200,"成功",tSeckillOrderservice.getResult(userid,goodsid));

  }



  @GetMapping("/csmiaos/{userid}/{goodsid}")  //测试秒杀接口
  public RespEntity csmiaos (@PathVariable String  userid ,@PathVariable String  goodsid)//(@RequestParam(value = "diandanchaz",required = false)
  {
    System.out.println(goodsid+userid);
    //进行令牌桶限流
    // 1. 阻塞式获取令牌
//   rateLimiter.acquire();
    // 2. 非阻塞式获取令牌
    if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {

      return new RespEntity(4009,"你被限流了");
    }

//    //验证接口也就是隐藏秒杀地址
//    System.out.println(path);
//    boolean dzpd=yanzdz(diandanchaz.getUserid(),diandanchaz.getGoodsid(),path);//有问题
//    System.out.println(dzpd);
//    if (dzpd){
//
//      return new RespEntity(4001,"地址不正确");
//    }
    //判断当前是否还有库存(这里判断的是商品表的库存)
    tgoodss kuc=tGoodsmapper.spmiaos(goodsid);

    if (kuc.getStockCount()<1)
    {
      return new RespEntity(4001,"没有库存");
    }
    System.out.println(kuc.getStockCount()+"sdhasjkdashkdhaskdhkashda");
    //判断是否是重复购买  wt
    TSeckillOrder tSeckillOrder1=  (TSeckillOrder)redis.opsForValue().get("order"+userid+":"+goodsid);
    if (tSeckillOrder1!=null){

      return new RespEntity(4002,"您已经购买一次了不可以再买");
    }
    System.out.println("没有重复购买");

    ValueOperations valueOperations=  redis.opsForValue();
    //先判断扣减reids里库存是否还有
    Long stock= valueOperations.decrement("seckillGoods:"+goodsid);//扣减  原则性
    System.out.println(stock);
    System.out.println(goodsid+"sdsadasdsadasdasdasdasdas");
    if(stock<1){
      EmptyStocMap.put(kuc.getId(),true);
      valueOperations.increment("seckillGoods:"+goodsid);
      return new RespEntity(4004,"预减库存没有库存");
    }

    SeckillMessage seckillMessage=  new SeckillMessage(userid,goodsid);//
    Gson gson=new Gson();
//    mqSender.sendSeckillMessage(gson.toJson(seckillMessage));//发送消息 通过MQ生成订单
    mqSender.sendSeckillMessage(seckillMessage);
    return new RespEntity(200,"正在排队中");
  }

  @PostMapping("miaoshaxiadan")
  public boolean ssdsds(@RequestParam("diandanchaz") diandancha diandanchaz){
//    diandancha a=diandanchaz;
//    System.out.println("sss"+a);
//    Integer.parseInt("abc123");
    String str = diandanchaz.getUserid();
    str.equals("111");
    return true;

  }


}
