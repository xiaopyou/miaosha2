package com.qi.miaosha2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qi.miaosha2.entrty.TUser;
import com.qi.miaosha2.mapper.tusermapper;
import com.qi.miaosha2.service.tuserservice;
import com.qi.miaosha2.util.Jwtgonj;
import com.qi.miaosha2.util.MD5util;
import com.qi.miaosha2.util.RespEntity;
import com.qi.miaosha2.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class tuserimpl implements tuserservice  {
    @Autowired
    tusermapper tusermapper;
    @Autowired
    Jwtgonj jwtgonj;
    @Resource
    RedisTemplate<Object,Object> redis;

        public RespEntity dlu(TUser tUser) {
            //先判断是否有值
            if (tUser==null){
                System.out.println("不能为空");
                return new RespEntity(4001,"不能为空");
            }
            //然后判断是否符合之前手机号
            if (!ValidatorUtil.isMobile(


                    String.valueOf(tUser.getId()))){//
                System.out.println("手机号不符合");
                return new RespEntity(4002,"手机号不符合");
            }

            //如果符合就去数据库去找到当前用户
            QueryWrapper<TUser> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("id",tUser.getId());
            TUser tuser=  tusermapper.selectOne(queryWrapper);
            if (tuser==null){//是否有当前用户

                return new RespEntity(4000,"没有当前用户");
            }
            System.out.println(tuser.getPassword());
            //然后判断传过来的密码是否和加密的密码相同
            if (!MD5util.inputPassToDBPass(tUser.getPassword(),tuser.getSalt()).equals(tuser.getPassword()))

            {
                //最后如果都成功生成token封装数据发送给前端

                System.out.println("失败");//a1c1be6cd699aa1a29b98571f4fb5431
                return new RespEntity(4000,"密码错误");
            }

            System.out.println(MD5util.inputPassToDBPass(tUser.getPassword(),tuser.getSalt()));//一般不这么写这里我是懒不想做前端
            System.out.println(tuser+"sadasdas");
            System.out.println("token:"+jwtgonj.jiami(tuser.getId()));
            redis.opsForValue().set("id",tuser);
            System.out.println(redis.opsForValue().get("id"));//当前用户存进redis
            return new RespEntity(200,"cg",tuser);
        }




    public RespEntity dlucs(String id,String pwd){
        //先判断是否有值
        if (id==null&&pwd==null){
            System.out.println("不能为空");
            return new RespEntity(4001,"不能为空");
        }
        //然后判断是否符合之前手机号
        if (!ValidatorUtil.isMobile(String.valueOf(id))){//
            System.out.println("手机号不符合");
            return new RespEntity(4002,"手机号不符合");
        }

        //如果符合就去数据库去找到当前用户
        QueryWrapper<TUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        TUser tuser=  tusermapper.selectOne(queryWrapper);
        if (tuser==null){//是否有当前用户

            return new RespEntity(4000,"没有当前用户");
        }
        //然后判断传过来的密码是否和加密的密码相同
        if (!MD5util.inputPassToDBPass(pwd,tuser.getSalt()).equals(tuser.getPassword()))

        {
            //最后如果都成功生成token封装数据发送给前端

            System.out.println("失败");//a1c1be6cd699aa1a29b98571f4fb5431
            return new RespEntity(4000,"密码错误");
        }

        System.out.println(MD5util.inputPassToDBPass(pwd,tuser.getSalt()));//一般不这么写这里我是懒不想做前端
        System.out.println(tuser+"sadasdas");
        System.out.println("token:"+jwtgonj.jiami(tuser.getId()));
        redis.opsForValue().set("id",tuser);
        System.out.println(redis.opsForValue().get("id"));//当前用户存进redis
        return new RespEntity(200,"cg",tuser);
    }
}
