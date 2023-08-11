package com.qi.miaosha2.util.handler;

import com.qi.miaosha2.util.RespEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyHandlerMethodReturn {//全局异常

    //数据库连接异常
    @ExceptionHandler({Exception.class})
    public RespEntity exception (Exception e){

        System.out.println("异常");

        return new RespEntity("400",e.getMessage());


    }
    //业务错误异常
//    @ExceptionHandler({})
//    public RespEntity exception (Exception e){
//
//        System.out.println("异常");
//
//        return new RespEntity("400","错误");
//
//
//    }


    //redis 异常


    //MQ


    //空指针




}
