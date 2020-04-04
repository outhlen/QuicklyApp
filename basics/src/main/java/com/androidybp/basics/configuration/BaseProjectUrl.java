package com.androidybp.basics.configuration;

import com.androidybp.basics.utils.encryption.Base64Utils;

public class BaseProjectUrl {
    //用于判断当前 url是否加密
    private static final boolean isEncrypt = false;

    /****************************************************************************************
     ↓↓↓↓↓↓                   接口域名部份                   ↓↓↓↓↓↓
     ****************************************************************************************/

    /**
     * 获取当前环境地址 默认是正式环境
     * @return
     */
    public static String getRequestUrl(){
        String url = getUrl();
        if(isEncrypt){
            return decodeUrl(url);
        } else {
            return url;
        }
    }

    private static String getUrl(){
        return BaseProjectConfiguration.getMetaDataString("SERVER_HOST");
    }




    /******************↑↑ 接口域名部分   结束 ↑↑**********************/


    /****************************************************************************************
     ↓↓↓↓↓↓                   分享域名部分                  ↓↓↓↓↓↓
     ****************************************************************************************/


    /**
     * 获取当前分享环境地址 默认是正式环境
     * @return
     */
    public static String getWebUrl(){
        String url = getWebUrlHost();
        if(isEncrypt){
            return decodeUrl(url);
        } else {
            return url;
        }
    }

    private static String getWebUrlHost(){
        return BaseProjectConfiguration.getMetaDataString("WEB_HOST");
    }


    /******************↑↑ 分享域名部分   结束 ↑↑**********************/

    /**
     * 给制定的 URL 加密
     * @param url 加密的url
     * @return 打包使用的密文
     */
    public static String encodeUrl(String url){
        return Base64Utils.encode(url);
    }

    /**
     * 将打包项目的url密码 解密
     * @param url 加密的url
     * @return 打包使用的密文
     */
    public static String decodeUrl(String url){
        return Base64Utils.decode(url);
    }
}
