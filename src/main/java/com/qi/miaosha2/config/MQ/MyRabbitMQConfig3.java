//package com.qi.miaosha2.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration  qiyongle.topic
//public class MyRabbitMQConfig3 {//配置队列和交换器 如果自己已经配置了消息就不要在写了
//    private static final String QUEUE ="seckillQueue";
//    private static final String EXCHANGE ="qiyonle2.direct";
//    private static final String ROUTINGKEY ="seckillQueue";
//    @Bean
//    public Queue miaosha()
//    {
//        //队列第一个是队列名字 第2个是是否开启持久化
//        return new Queue(QUEUE,true);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange(){//交换机
//
//
//        return new FanoutExchange(EXCHANGE);
//    }
//    @Bean
//    public Binding binding1(){//绑定队列到交换机上面
//
//        return BindingBuilder.bind(miaosha()).to(fanoutExchange());
//    }
//
//
//}
