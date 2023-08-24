package com.qi.miaosha2.config;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyRabbitMQConfig6 {
    // 交换机
    public static final String confirm_exchange_name = "confirm_exchange";
    // 队列
    public static final String confirm_queue_name="confirm_queue";
    // routingkey 自己定义的队列名字
    public static final String confirm_routing_key = "key1";
    //死信队列
    public static final String dead_queue_name = "dead_queue_springboot";
    // 死信交换机
    public static final String confirm_exchange_name2 = "sxinjiaohuanj";


    // 声明direct类型交换机交换机 FanoutExchange 声明广播类型交换机交换机
    @Bean
    public DirectExchange confirmExchange(){
        /**
         * 参数说明：
         * String name：交换器名称
         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
         */
        return new DirectExchange(confirm_exchange_name);
    }

    //死行交换机
    @Bean
    public DirectExchange confirmExchange2(){
        return new DirectExchange(confirm_exchange_name2);
    }
    // 声明队列
    @Bean
    public Queue confirmQueue() {//记得每次如果变更了队列的参数就要删除要不还是之前的
        Map<String,Object> args=new HashMap<>();
        args.put("x-message-ttl",5000);//过期时间
        args.put("x-max-length",1000);//队列最大个数
        args.put("x-dead-letter-exchange",confirm_exchange_name2);//绑定死行队列
        args.put("x-dead-letter-routing-key","qiyonglesxing2");// fanout模式不用配置 如果设置了队列key就要写
        return new Queue(confirm_queue_name,true,false,false,args);//第一个参数 队列名 第2个 持久化  第三个 是否是共享排他性 第4个 自动删除 false 第五个 参数设置
    }
     //死信队列
     @Bean
    public Queue sxduil()
    {
        Map<String,Object> args=new HashMap<>();
        args.put("x-message-ttl",1000);//过期时间
        return new Queue(dead_queue_name,true,false,false,args);
    }

    // 绑定队列到交换机
    @Bean
    public Binding queueBingExchange(){
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with("qiyongle1");
    }
    // 绑定队列到交换机 （死行绑定）
    @Bean
    public Binding sx(){
        return BindingBuilder.bind(sxduil()).to(confirmExchange2()).with("qiyonglesxing2");
    }

}
