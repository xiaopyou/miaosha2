package com.qi.miaosha2.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 * @author pan_junbiao
 **/
@Configuration
public class MyRabbitMQConfig7 {

    public static final String QUEUE_NAME = "qiyongleACK"; //队列名称
    public static final String EXCHANGE_NAME = "qiyongleduilACK"; //交换器名称
    public static final String QUEUE_NAME2 = "qiyonglsxin"; // 死刑队列名称
    public static final String EXCHANGE_NAME2 = "qiyongleduilsxin";// 死刑交换机
    public static final String ROUTING_KEY = "qiACK"; //路由键
    /**
     * 队列
     */
    @Bean
    public Queue queue()
    {
        Map<String,Object> args=new HashMap<>();
        args.put("x-message-ttl",5000);//过期时间
//        args.put("x-max-length",1000);//队列最大个数
        args.put("x-dead-letter-exchange","qiyongleduilsxin");//绑定死行交换机
        args.put("x-dead-letter-routing-key","qisxduil2");// fanout模式不用配置 如果设置了队列key就要写
        return new Queue(QUEUE_NAME, true, false, false, args);
    }
    @Bean
    public Queue queue2(){ // 死刑队列名称

        return new Queue(QUEUE_NAME2,true,false,false);
    }

    /**
     * Direct交换器
     */
    @Bean
    public DirectExchange exchange()
    {
        /**
         * 创建交换器，参数说明：
         * String name：交换器名称
         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
         */
        return new DirectExchange(EXCHANGE_NAME);
    }
    @Bean
    public DirectExchange exchange2(){

        return new DirectExchange(EXCHANGE_NAME2);
    }

    /**
     * 绑定
     */
    @Bean
  public   Binding binding(DirectExchange exchange, Queue queue)
    {
        //将队列和交换机绑定, 并设置用于匹配键：routingKey
        return BindingBuilder.bind(queue).to(exchange).with("qiACK");
    }
    @Bean
    public   Binding binding2()
    {
        //将队列和交换机绑定, 并设置用于匹配键：routingKey
        return BindingBuilder.bind(queue2()).to(exchange2()).with("qisxduil2");
    }



//   @Bean
//    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory)//这个就是消息确认机制
//    {//这个就是告诉我们
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        rabbitTemplate.setConnectionFactory(connectionFactory);
//        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
//        rabbitTemplate.setMandatory(true);
//
//        //确认消息送到交换机(Exchange)回调
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback()
//        {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause)
//            {
//                System.out.println("\n确认消息送到交换机(Exchange)结果：");
//                System.out.println("相关数据：" + correlationData);
//                System.out.println("是否成功：" + ack);
//                System.out.println("错误原因：" + cause);
//            }
//        });
//
//        //确认消息送到队列(Queue)回调
//        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback()
//        {
//            @Override
//            public void returnedMessage(ReturnedMessage returnedMessage)
//            {
//                System.out.println("\n确认消息送到队列(Queue)结果：");
//                System.out.println("发生消息：" + returnedMessage.getMessage());
//                System.out.println("回应码：" + returnedMessage.getReplyCode());
//                System.out.println("回应信息：" + returnedMessage.getReplyText());
//                System.out.println("交换机：" + returnedMessage.getExchange());
//                System.out.println("路由键：" + returnedMessage.getRoutingKey());
//            }
//        });
//        return rabbitTemplate;
//    }
}
