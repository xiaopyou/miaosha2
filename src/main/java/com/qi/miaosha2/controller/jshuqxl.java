package com.qi.miaosha2.controller;

import com.qi.miaosha2.entrty.TUser;
import com.qi.miaosha2.util.RespEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("cs2")
public class jshuqxl {
    @Autowired
    com.qi.miaosha2.service.tuserservice tuserservice;
    @PostMapping("dlu2")
    public RespEntity cs(@RequestBody TUser tUser, HttpServletRequest request){
        //隐藏接口 （不是去隐藏一个地址这是让每一个用拥有不同的秒杀地址）
        //验证验证码 （）
       //请求限流限制访问次数 5秒内访问5次
     String uri= request.getRequestURI();//获取请求路径

        return null;
    }
}
