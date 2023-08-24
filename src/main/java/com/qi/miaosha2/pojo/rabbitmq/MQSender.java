package com.qi.miaosha2.pojo.rabbitmq;

import com.qi.miaosha2.config.ConfirmCallbackService;
import com.qi.miaosha2.config.ReturnCallbackService;
import com.qi.miaosha2.pojo.DTO.SeckillMessage;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class MQSender {//发送者

    @Resource
    RabbitTemplate rabbitTemplate;
    @Autowired
    MQReceiver mqReceiver;
//    public void sendSeckillMessage(String message){
//
//        System.out.println("将用户商品消息存入MQ");//中通过MQ获取秒杀订单消息
//        rabbitTemplate.convertAndSend("qiyonle2.direct","seckillQueue",message);
//        System.out.println(message+"zhi");
////        mqReceiver.receive(message);
//
//    }


    public void sendSeckillMessage(Object message){

        System.out.println("将用户商品消息存入MQ"+message);//中通过MQ获取秒杀订单消息
        rabbitTemplate.convertAndSend("qiyonle2.direct","seckillQueue",message);
        System.out.println(message+"zhi");
//        mqReceiver.receive(message);

    }

    public void cs(Object message){

        System.out.println("将用户商品消息存入MQ");//中通过MQ获取秒杀订单消息
        rabbitTemplate.convertAndSend("qiyongle2.topic","s.mhucx1",message);//第2个参数可以是队列名字也可以是自己起的队列名字
        //topic 主题
        //direct 点对点
        //fanout 广播模式
        //Headers 基本不用
    }


    public void ycduil(Object message){//一遍不用过期消息
        //设置消息的过期时间 记得这不是队列 过期消息过期了就消失了 如果是过期队列就不会直接消失如果有死行队列的话就会到死行队列里
        MessagePostProcessor messagePostProcessor=new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("5000");
                message.getMessageProperties().setContentEncoding("UTF-8");
                return message;
            }
        };
        System.out.println("将用户商品消息存入MQ");
        rabbitTemplate.convertAndSend("confirm_exchange","qiyongle1",message,messagePostProcessor);
    }
    public void ycduil2(Object message){// 可以做15分钟支付
//        rabbitTemplate.setConfirmCallback(confirmCallbackService);
//        rabbitTemplate.setReturnsCallback(returnCallbackService);
        System.out.println("将用户商品消息存入MQ");
        rabbitTemplate.convertAndSend("confirm_exchange","qiyongle1",message);
    }

    @Autowired
    ConfirmCallbackService confirmCallbackService;

    @Autowired
    ReturnCallbackService returnCallbackService;
    public void ycduilqrxiaox (Object message) throws Exception{//解决消息不消失 一般和生产者无关和消费者有关   消息确认 ACK


//        //设置交换器的确认
        rabbitTemplate.setConfirmCallback(confirmCallbackService);
        rabbitTemplate.setReturnsCallback(returnCallbackService);
//        Thread.sleep(5000);
        //这里故意将routingKey参数写入错误，让其应发确认消息送到队列失败回调
        rabbitTemplate.convertAndSend("qiyongleduilACK", "qiACK", message);


    }


}
