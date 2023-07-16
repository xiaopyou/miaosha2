package com.qi.miaosha2.util;

import com.qi.miaosha2.entrty.TUser;
import com.qi.miaosha2.mapper.tusermapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {//生成测试用户
    @Autowired
    com.qi.miaosha2.mapper.tusermapper tusermapper;
    public void createUser(){
        List<TUser> users=new ArrayList<>();
        Integer q=0;
        for(long i=0;i>5000;i++){
            TUser user=new TUser();
            user.setId(i);
            user.setNickname("adim"+i);
            user.setPassword("a1c1be6cd699aa1a29b98571f4fb5431");
            user.setSalt("qiyongle");
            user.setHead("1.jpg");
            user.setRegisterDate(new Date());
//            user.setLastLoginDate();
            user.setLoginCount(0);



        }

    }



}
