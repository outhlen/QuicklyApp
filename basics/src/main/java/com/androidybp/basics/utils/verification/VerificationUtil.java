package com.androidybp.basics.utils.verification;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * 规则验证工具类
 *
 * 对于正则  可以找官方 辅助类 Patterns  里面有好多写好的正则 比如 email  phone  ip 等通用格式的验证规则
 */
public class VerificationUtil {

    public static final String TIME_DATA_SECOND_NEW= "([0-9]{4})-([0-9]{2})-([0-9]{2})\\s([0-9]{2}):([0-9]{2}):([0-9]{2})";
    //手机号
    public static final String MOBILE_NUM= "(\\+\\d+)?1[3456789]\\d{9}$";

    /**
     *
     * @param string 要检验的内容
     * @param regular 具体检验的正则
     * @return true 合法  false 不合法
     */
    public static boolean verificationText(String string, String regular){
        return Pattern.compile(regular).matcher(string).matches();
    }

    /**
     * 验证URL格式是否符合规则
     * @param url  需要检验的URL
     * @return true - 符合规则     false -- 不符合规则
     */
    public static boolean isWebUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches();
    }

    /**
     * 验证Email格式是否符合规则
     * @param email  email字符串
     * @return  true 合法  false 不合法
     */
    public static boolean isEmailAddress(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * 验证手机号是否符合规则
     * @param phoneName  手机号
     * @return  true 合法  false 不合法
     */
     public static boolean isPhoneName(String phoneName){
        return Patterns.PHONE.matcher(phoneName).matches();
    }

    /**
     *  验证IP地址是否符合规则
     * @param ipAddress  需要验证的IP 字符串
     * @return  true 合法  false 不合法
     */
     public static boolean isIpAddress(String ipAddress){
        return Patterns.IP_ADDRESS.matcher(ipAddress).matches();
    }




}
