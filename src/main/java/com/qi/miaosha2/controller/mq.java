package com.qi.miaosha2.controller;

import com.qi.miaosha2.entrty.TSeckillOrder;
import com.qi.miaosha2.pojo.rabbitmq.MQReceiver;
import com.qi.miaosha2.pojo.rabbitmq.MQSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("mq")
public class mq {

    @Autowired
    MQReceiver mqReceiver;
    @Autowired
    MQSender mqSender;

    @GetMapping("fs")
    public void mqfs(){
        TSeckillOrder lianxi = new TSeckillOrder(1l,18l,0l,11l);
        TSeckillOrder lianxi2 = new TSeckillOrder(1l,18l,0l,12l);
        TSeckillOrder lianxi3 = new TSeckillOrder(1l,18l,0l,13l);
        List<TSeckillOrder> list=new ArrayList<>();
        list.add(lianxi);
        list.add(lianxi2);
        list.add(lianxi3);
//        mqSender.ycduil2(list);
        try {
            mqSender.ycduilqrxiaox(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
