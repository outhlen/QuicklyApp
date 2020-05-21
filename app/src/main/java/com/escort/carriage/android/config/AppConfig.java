package com.escort.carriage.android.config;


import com.escort.carriage.android.BuildConfig;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/09/02
 *    desc   : App 配置管理类
 */
public final class AppConfig {

    public static final String HX_APPKEY= "1460200428068026#kefuchannelapp80531";
    public static final String HX_TENANTID = "80531";
    public static final String HX_IMNUMBER = "kefuchannelimid_232887";
    public static String  HXUSERNAME  = "1248555330743730177";
    public static String  HXPASSWORD  = "1248555330743730177";

    /**
     * 当前是否为 Debug 模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 获取当前应用的包名
     */
    public static String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    /**
     * 获取当前应用的版本名
     */
    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取当前应用的版本码
     */
    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 获取当前应用的渠道名
     */
    public static String getProductFlavors() {
        return BuildConfig.FLAVOR;
    }
}