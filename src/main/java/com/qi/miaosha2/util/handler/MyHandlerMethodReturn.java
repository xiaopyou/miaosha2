package com.qi.miaosha2.util.handler;

import com.qi.miaosha2.util.RespEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class MyHandlerMethodReturn {//全局异常
    private static final Logger logger = LoggerFactory.getLogger(MyHandlerMethodReturn.class);
    //数据库连接异常
    @ExceptionHandler({Exception.class})
    public RespEntity exception (Exception e){

        log.error("发生空指针异常！原因是:{}","系统错误");
        return new RespEntity("400",e.getMessage());


    }
    //业务错误异常
    @ExceptionHandler({RespEntity.class})
    public RespEntity exception (RespEntity e){

//        log.error("这个错误严重");
//        log.warn("这个错误 不影响程序");
        log.info("业务异常{}",e.getCode());//用的多
//        log.debug("如果只是想打印一个日志的话用这个2");//用的多
//        log.trace("");
        return new RespEntity(e.getMsg());


    }


    //redis 异常


    //MQ


    //空指针

    @ExceptionHandler(value =NullPointerException.class)
    public String exceptionHandler(NullPointerException e){//HttpServletRequest req,
        logger.error("发生空指针异常{}",e);
        return "空指针";
    }




}
