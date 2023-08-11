package com.qi.miaosha2.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5util {
    public static String md5(String src){//MD5加密
        //DigestUtils.md2Hex(src)
        return DigestUtils.md5Hex(src);
    }
    //加盐跟那个jwt差不多
    public static final String salt="qiyongle";//统一加盐的值
    public static String formPassToDBPass(String inputpass,String salt){//2次加密也就是后端加密

//    String str= salt.charAt(0)+salt.charAt(2)+inputpass+salt.charAt(4);
        String str= inputpass+salt;
        return md5(str);
    }
    //前端的加密传过来
    public static  String inputPassToDBPass(String inputpass,String salt){//第一个参数是接收前端加密的密码 第2个参数是加盐要统一

       String jiam= formPassToDBPass(inputpass,salt);
       System.out.println(jiam);

        return jiam;
    }

    public static void main(String[] args) {

        //模拟前端加密
//        String qd= salt.charAt(0)+salt.charAt(2)+"123456"+salt.charAt(4);
        String qd= "123456"+salt;
      String jiam1=  md5(qd);
      String ss="bb10dbc28b0c6dafe7408ada316bf79e";
      if (ss.equals(jiam1))
      {

          System.out.println("dui");
      }
      System.out.println(ss);//bb10dbc28b0c6dafe7408ada316bf79e
      System.out.println(inputPassToDBPass(jiam1,salt));//a1c1be6cd699aa1a29b98571f4fb5431
    }
}
