package com.qi.miaosha2.pojo.rabbitmq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;



public class RabbitMQTopicConfig {//自定义常见
//    private static final String QUEUE="seckillQueue";//队列
//    private static final String EXCHANGE="seckillExchange";//队列
//
//    public org.springframework.amqp.core.Queue queue(){//创建队列
//
//        return new Queue(QUEUE);
//
//    }
//
//    public TopicExchange topicExchange(){//创建交换机
//
//
//        return new TopicExchange(EXCHANGE);
//    }
//    @Bean
//    public Binding binding(){//RabbitMQ一般用主题模式
//
//        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
//    }


}
