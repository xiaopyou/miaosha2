package com.qi.miaosha2.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class Jwtgonj {
    private Integer id;
    private long id1;
    private String name;
    private String quanx;

    public String jiami(long id,String name,String quanx){//这个方法是用来发送数据的
      String token=  JWT.create().withClaim("id",id)
                .withClaim("name",name)
                .withClaim("quanx",quanx)
              .withExpiresAt(new Date(System.currentTimeMillis()+300000))//设置登录之后30秒过期
              .sign(Algorithm.HMAC256("qiyonglesb"));

        return token;
    }
    public String jiami(Integer id){//这个方法是用来发送数据的
        String token=  JWT.create().withClaim("id",id)
              .withExpiresAt(new Date(System.currentTimeMillis()+30000000))//设置登录之后30秒过期
                .sign(Algorithm.HMAC256("qiyonglesb"));

        return token;
    }
    public String jiami(Long id){//生成token的
        String token=  JWT.create().withClaim("id",id)
                .withExpiresAt(new Date(System.currentTimeMillis()+30000000))//设置登录之后30秒过期
                .sign(Algorithm.HMAC256("qiyonglesb"));

        return token;
    }
    public String jiex(HttpServletRequest req, HttpServletResponse resp,String token) throws IOException {//解析密钥  获取前端加密的token 在在这个方法里来判断是否有密钥
        System.out.println(token);
        JWTVerifier build = JWT.require(Algorithm.HMAC256("qiyonglesb")).build();
        try {
            DecodedJWT msg = build.verify(token);// //验证指定的token是否有效，如果此处没有报错，说明验证通过，token有效，如果报错，说明token无效

        }catch (SignatureVerificationException e){
//            ObjectMapper om=new ObjectMapper();
//            om.writeValue(resp.getWriter(),new RespEntity(5000,"无效的token:签名不一样"));
            return "无效的token:签名不一样";
        }catch (TokenExpiredException e){

            return "token过期";
        }catch (AlgorithmMismatchException e){

            return "算法不一致";
        }catch (InvalidClaimException e){

            return "载体异常";
        }
        return "登录成功";
    }

    public String jiex(String token) throws IOException {//解析密钥  获取前端加密的token 在在这个方法里来判断是否有密钥
        System.out.println(token);
        JWTVerifier build = JWT.require(Algorithm.HMAC256("qiyonglesb")).build();
        try {
            DecodedJWT msg = build.verify(token);// //验证指定的token是否有效，如果此处没有报错，说明验证通过，token有效，如果报错，说明token无效

        }catch (SignatureVerificationException e){
            return "无效的token:签名不一样";
        }catch (TokenExpiredException e){

            return "token过期";
        }catch (AlgorithmMismatchException e){

            return "算法不一致";
        }catch (InvalidClaimException e){

            return "载体异常";
        }
        System.out.println("成功");
        return "登录成功";
    }
    //根据key得到负载中的值
    public  String  getClaim(String token,String claimKey){
        return JWT.decode(token).getClaim(claimKey).asString();
    }

}
