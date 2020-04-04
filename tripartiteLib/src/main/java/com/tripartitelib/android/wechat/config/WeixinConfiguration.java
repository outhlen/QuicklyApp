package com.tripartitelib.android.wechat.config;

public class WeixinConfiguration {

    //正式环境的微信 APPID
    public static final String OFFICIAL_APP_ID = "wxa9a0fa1eb7bc4b85";
    //正式环境的微信 key
    public static final String OFFICIAL_APP_KEY = "46fb422701db7b3958e43a8046c448fd";


    /**
     * 获取微信的APP id
     * 默认正式环境
     * @return
     */
    public static String getWeixinAppId(int projectType){
        switch (projectType){
            case 1://开发环境
            case 2://测试环境
            case 3://正式环境
                return OFFICIAL_APP_ID;
            default://默认 正式环境
                return OFFICIAL_APP_ID;
        }
    }

    /**
     * 获取微信的APP key
     * 默认正式环境
     * @return
     */
    public static String getWeixinAppkey(int projectType){
        switch (projectType){
            case 1://开发环境
            case 2://测试环境
            case 3://正式环境
                return OFFICIAL_APP_KEY;
            default://默认 正式环境
                return OFFICIAL_APP_KEY;
        }
    }

}
