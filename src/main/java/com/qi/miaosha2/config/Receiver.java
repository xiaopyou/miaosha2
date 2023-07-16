package com.qi.miaosha2.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 接收者
 * @author pan_junbiao
 **/
@Component
public class Receiver implements ChannelAwareMessageListener
{
//    @RabbitListener(queues = "qiyongleACK")
    @Override
    public void onMessage(Message message, Channel channel) throws Exception
    {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try
        {
            System.out.println("接收消息: " + new String(message.getBody(), "UTF-8"));

            /**
             * 确认消息，参数说明：
             * long deliveryTag：唯一标识 ID。
             * boolean multiple：是否批处理，当该参数为 true 时，
             * 则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
             */
            channel.basicAck(deliveryTag, true);

            /**
             * 否定消息，参数说明：
             * long deliveryTag：唯一标识 ID。
             * boolean multiple：是否批处理，当该参数为 true 时，
             * 则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
             * boolean requeue：如果 requeue 参数设置为 true，
             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
             * 而不会把它发送给新的消费者。
             */
            //channel.basicNack(deliveryTag, true, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            /**
             * 拒绝消息，参数说明：
             * long deliveryTag：唯一标识 ID。
             * boolean requeue：如果 requeue 参数设置为 true，
             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
             * 而不会把它发送给新的消费者。
             */
            channel.basicReject(deliveryTag, true);
        }
    }
}