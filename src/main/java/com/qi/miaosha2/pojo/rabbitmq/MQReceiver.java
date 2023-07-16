package com.qi.miaosha2.pojo.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.qi.miaosha2.config.Receiver;
import com.qi.miaosha2.entrty.TGoods;
import com.qi.miaosha2.entrty.TSeckillOrder;
import com.qi.miaosha2.entrty.tgoodss;
import com.qi.miaosha2.mapper.TGoodsmapper;
import com.qi.miaosha2.pojo.DTO.SeckillMessage;
import com.qi.miaosha2.service.TSeckillGoodsservice;
import com.qi.miaosha2.util.RespEntity;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.apache.catalina.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.plugin2.message.Message;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class MQReceiver {//implements ChannelAwareMessageListener 消费者
    @Autowired
    TSeckillGoodsservice tSeckillGoodsservice;
    @Autowired
    TGoodsmapper tGoodsmapper;//商品表
    @Resource
    RabbitTemplate rabbitTemplate;
    @Autowired
    Receiver receiver;

    @Resource
    RedisTemplate<Object,Object> redis;
    @RabbitListener(queues = "seckillQueue") // @RabbitListener(queues = "seckillQueue") 监控seckillQueue这个队列 // 获取MQ里面的消息生成秒杀订单 前面还是要去判断库存是否足够
    public void receive(SeckillMessage seckillMessage){
        System.out.println("获取MQ");
//        Gson gson=new Gson();
//        SeckillMessage seckillMessage= gson.fromJson(messge,SeckillMessage.class);
        System.out.println(seckillMessage.toString());

        TGoods tGoods=  tGoodsmapper.selectOne(new QueryWrapper<TGoods>().eq("id",seckillMessage.getGoodId()));//you
        //判断当前是否还有库存(这里判断的是商品表的库存)
        tgoodss kuc=tGoodsmapper.spmiaos(seckillMessage.getGoodId());
        if (kuc.getGoodsStock()<1)
        {
            return ;
        }
        //判断是否是重复购买
        TSeckillOrder tSeckillOrder1=  (TSeckillOrder)redis.opsForValue().get("order"+seckillMessage.getUserid()+":"+seckillMessage.getGoodId());
        if (tSeckillOrder1!=null){

            return ;
        }
        //获取MQ里面的消息生成秒杀订单 (作用进行流量削峰)
        tSeckillGoodsservice.scdindan(seckillMessage.getUserid(),tGoods);
    }

//    @RabbitListener(queues = "miaosha2")//不要写交换机一定要写队列没有为什么
//    public void js(  List<TSeckillOrder> msg){//直接在参数那里设置对应的类型
//        System.out.println("消费者1");
//
//       System.out.println(msg.toString());
//
//    }
//    @RabbitListener(queues = "miaosha2s")
//    public void js2( List<TSeckillOrder> msg){
//        System.out.println("消费者2");
//        System.out.println(msg);
//
//    }


    @RabbitListener(queues = "miaosha3s")//不要写交换机一定要写队列没有为什么
    public void js(  List<TSeckillOrder> msg){//直接在参数那里设置对应的类型
        System.out.println("消费者1");

        System.out.println(msg.toString());

    }
    @RabbitListener(queues = "miaosha3")
    public void js2( List<TSeckillOrder> msg){
        System.out.println("消费者2");
        System.out.println(msg);

    }


    @RabbitListener(queues = "mhucx1")//不要写交换机一定要写队列没有为什么
    public void js3(  List<TSeckillOrder> msg){//直接在参数那里设置对应的类型
        System.out.println("消费者3");

        System.out.println(msg.toString());

    }
    @RabbitListener(queues = "mhucx2")
    public void js24( List<TSeckillOrder> msg){
        System.out.println("消费者4");
        System.out.println(msg);

    }

//    @RabbitListener(queues = "confirm_queue")
//    public void handleUserMessage(List<TSeckillOrder> msg) throws IOException {
//        System.out.println("过期消费者4");
//        System.out.println(msg.toString());
//
//    }


    @RabbitListener(queues = "dead_queue_springboot")//死刑队列
    public void handleUserMessage2(List<TSeckillOrder> msg) throws IOException {

        System.out.println("死行交换机费者4");
        System.out.println(msg.toString());

    }
//    @RabbitListener(queues = "qiyongleACK")
    @RabbitListener(queues = "qiyonglsxin")
    public void handleUserMessageACK3(List<TSeckillOrder> msg) throws IOException {

        System.out.println("ACK死行交换机费者5");
        System.out.println(msg.toString());

    }


//    @RabbitListener(queues = "dead_queue_springboot")//ACK确认
//    public void handleUserMessage4(Message message, Channel channel) throws IOException {
//        System.out.println("死行交换机费者4");
//
//
//    }
//@RabbitListener(queues = "qiyongleACK")//ACK
//    @Override
//    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        try
//        {
//            System.out.println("接收消息: " + new String(message.getBody(), "UTF-8"));
//
//            /**
//             * 确认消息，参数说明：
//             * long deliveryTag：唯一标识 ID。
//             * boolean multiple：是否批处理，当该参数为 true 时，
//             * 则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
//             */
//            channel.basicAck(deliveryTag, true);
//
//            /**
//             * 否定消息，参数说明：
//             * long deliveryTag：唯一标识 ID。
//             * boolean multiple：是否批处理，当该参数为 true 时，
//             * 则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
//             * boolean requeue：如果 requeue 参数设置为 true，
//             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
//             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
//             * 而不会把它发送给新的消费者。
//             */
////            channel.basicNack(deliveryTag, true, false);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//
//            /**
//             * 拒绝消息，参数说明：
//             * long deliveryTag：唯一标识 ID。
//             * boolean requeue：如果 requeue 参数设置为 true，
//             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
//             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
//             * 而不会把它发送给新的消费者。
//             */
//            channel.basicReject(deliveryTag, true);
//        }
//
//
//    }


//    public void receive(){
//        System.out.println("获取MQ");
//        String messge=  (String)rabbitTemplate.receiveAndConvert("qiyonle.hu");
//        Gson gson=new Gson();
//        SeckillMessage seckillMessage= gson.fromJson(messge,SeckillMessage.class);
//        System.out.println(seckillMessage.toString());
//
//        TGoods tGoods=  tGoodsmapper.selectOne(new QueryWrapper<TGoods>().eq("id",seckillMessage.getGoodId()));//you
//        //判断当前是否还有库存(这里判断的是商品表的库存)
//        tgoodss kuc=tGoodsmapper.spmiaos(seckillMessage.getGoodId());
//        if (kuc.getGoodsStock()<1)
//        {
//            return ;
//        }
//        //判断是否是重复购买
//        TSeckillOrder tSeckillOrder1=  (TSeckillOrder)redis.opsForValue().get("order"+seckillMessage.getUserid()+":"+seckillMessage.getGoodId());
//        if (tSeckillOrder1!=null){
//
//            return ;
//        }
//        //获取MQ里面的消息生成秒杀订单 (作用进行流量削峰)
//        tSeckillGoodsservice.scdindan(seckillMessage.getUserid(),tGoods);
//    }


}
