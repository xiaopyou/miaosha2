package com.qi.miaosha2.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConfirmCallbackService implements RabbitTemplate.ConfirmCallback {
    /**
     * @param correlationData
     * @param ack true 表示消息成功发送到交换机，false 则发送失败
     * @param cause 消息发送失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息已经发送到交换机！");
        } else {
            System.out.println("消息发送到交换机失败：" + cause);
        }
    }
}
