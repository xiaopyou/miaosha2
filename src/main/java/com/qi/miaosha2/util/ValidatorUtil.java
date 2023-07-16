package com.qi.miaosha2.util;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {//判断手机号是否符合要求的类
    public static final Pattern mobile_patren = Pattern.compile("[1]([3-9])[0-9]{9}$");
    public static  boolean isMobile(String mobile){

        if (StringUtils.isEmpty(mobile)){//StringUtils.isEmpty(）判断当前值是否为空

            return false;
        }
        Matcher matcher=mobile_patren.matcher(mobile);//判断是否符合规则
        return matcher.matches();


    }


}
