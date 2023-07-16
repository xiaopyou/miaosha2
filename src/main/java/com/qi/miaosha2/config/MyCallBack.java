package com.qi.miaosha2.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 向rabbitTemplate 注入回调失败的类
     * 后置处理器：其他注解都执行结束才执行。
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
    /**
     * 交换机确认回调方法
     *  发消息 交换机接收到了  回调
     * @param correlationData ：保存回调消息的 ID 及相关信息，交换机收到消息 ack=true代表成功；ack=false 代表失败
     * @param ack ：true 代表交换机收到了
     * @param cause : 原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
//            log.info("交换机已经收到 ID 为：{} 的消息",correlationData.getId());
            System.out.println("交换机已经收到 ID 为：{} 的消息"+correlationData.getId());
        }else{
//            log.info("交换机未收到 ID为 {} 的消息,原因是 {}",correlationData.getId(),cause);
            System.out.println("交换机未收到 ID为 {} 的消息,原因是 {}"+correlationData.getId()+cause);
        }
    }

    /**
     * 当消息传送到队列过程中不可抵达的时候 将消息返回给生产者
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//        log.error("消息 {} ,被交换机 {} 退回原因 {}",message,exchange,replyText);
        System.out.println("消息 {} ,被交换机 {} 退回原因 {}"+message+exchange+replyText);
    }
}


