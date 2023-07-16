package com.qi.miaosha2.config;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitMQConfig5 {
//
//    // 交换机
//    public static final String dead_exchange_name = "dead_exchange_springboot";
//    // 普通交换机
//    public static final String normal_exchange_name = "normal_exchange_springboot";
//    // 普通队列
//    public static final String normal_queue_name="normal_queue_springboot";
//    // 死信队列
//    public static final String dead_queue_name = "dead_queue_springboot";
//
//    // 声明交换机
//    @Bean
//    public DirectExchange deadExchange(){
//        return new DirectExchange(dead_exchange_name);
//    }
//    // 声明交换机
//    @Bean
//    public DirectExchange normalExchange(){
//        return new DirectExchange(normal_exchange_name);
//    }
//    // 声明队列
//    @Bean
//    public Queue deadQueue() {
//        return QueueBuilder.durable(dead_queue_name).build();
//    }
//    // 声明队列
//    @Bean
//    public Queue normalQueue() {
//        return QueueBuilder.durable(normal_queue_name)
//                .withArgument("x-dead-letter-exchange",dead_exchange_name)
//                .withArgument("x-dead-letter-routing-key","dead")
//                .build();
//    }
//    // 绑定队列到交换机
//    @Bean
//    public Binding queueBingExchange3(){
//        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with("normal");
//    }
//    // 绑定队列到交换机
//    @Bean
//    public Binding queueBingExchange4(){
//        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("dead");
//    }





}
