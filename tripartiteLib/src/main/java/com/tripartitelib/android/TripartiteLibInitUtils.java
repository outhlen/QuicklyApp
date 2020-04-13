package com.tripartitelib.android;

import android.content.Context;

import com.androidybp.basics.ApplicationContext;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tripartitelib.android.wechat.config.WeixinConfiguration;

/**
 * @author Yangbp
 * @description: 初始化三方操作方法
 * @date :2020-03-23 10:47
 */
public class TripartiteLibInitUtils {

    private static volatile TripartiteLibInitUtils tripartiteLibInitUtils;

    private IWXAPI mWxApi;

    private TripartiteLibInitUtils(){}

    public static TripartiteLibInitUtils getUtils(){
        if(tripartiteLibInitUtils == null){
            synchronized (TripartiteLibInitUtils.class){
                if(tripartiteLibInitUtils == null){
                    tripartiteLibInitUtils = new TripartiteLibInitUtils();
                }
            }
        }
        return tripartiteLibInitUtils;
    }

    /**
     * 初始化 微信
     * @param context
     */
    public void registerToWX(Context context) {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(context, WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE), false);
        // 将该app注册到微信
        mWxApi.registerApp(WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE));


    }

    /**
     * 获取微信全局对象
     * @return
     */
    public IWXAPI getWechat(){
        if(mWxApi == null){
            registerToWX(ApplicationContext.getInstance().context);
        }
        return mWxApi;
    }

}
