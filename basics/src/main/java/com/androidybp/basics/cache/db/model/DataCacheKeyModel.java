package com.androidybp.basics.cache.db.model;

/**
 * Created by yangbp on 2017/12/27.
 *
 * 数据缓存保存参数保存类
 */

public class DataCacheKeyModel {

    // =======================================
    // ============ 1、用户关键信息缓存和登录流程相关数据缓存 ==============
    // =======================================
    public static final String USER_INFO = "user_info";//用户信息 登录后获取
    public static final String USER_INFO_ENTITY = "user_info_entity";//用户信息 跟登录返回数据不一样

    // =======================================
    // ============ 2、APP属性缓存信息 ==============
    // =======================================
    public static final String APP_CACHEDATA = "app_cachedata";//当前APP一下配置信息  缓存集中类
    public static final String USER_TOKEN = "user_token";//用户登录使用的Token数据
    public static final String END_LOCATION = "end_location";//最后一次定位的地理位置
    public static final String AMAP_DATA = "amap_data";//高德地图猎鹰轨迹 使用参数

    // =======================================
    // ============ 3、Splash 相关缓存数据 ==============
    // =======================================
    public static final String SPLASH_PAGE_IMAGE = "splash_page_image";//Splash页面的网络图片


    // =======================================
    // ============ 4、HomeFrag 相关缓存数据 ==============
    // =======================================
    public static final String HOMEFRG_PAGE_IMAGE = "homefrg_page_image";//信息首页的轮播图   页面的网络图片

    // =======================================
    // ============ 5、个推相关参数配置 ==============
    // =======================================

    public static final String GETUI_IDENTIFYING = "getui_identifying";//个推相关参数配置
}
