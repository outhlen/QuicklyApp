package com.tripartitelib.android.wechat;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tripartitelib.android.EnvironmentConfig;
import com.tripartitelib.android.TripartiteLibInitUtils;
import com.tripartitelib.android.wechat.bean.WXPayCallback;
import com.tripartitelib.android.wechat.bean.WXResponseMembers;
import com.tripartitelib.android.wechat.bean.WechatResponceGetUserInfoEntity;
import com.tripartitelib.android.wechat.config.WeixinConfiguration;

/**
 * @author Yangbp
 * @description:  微信操作 使用类
 * @date :2020-03-23 11:24
 */
public class WechatUtils {

    // ------------------------------  微信登陆部分

    public static WechatLoginCallback wechatLoginCallback;
    public static int wechatLoginCallbackType;//0 回调 unid 1：回调微信code 2:使用微信官方说法 先获取accessToken 在获取用户uniId


    /**
     * 微信登录
     */
    public static void wxLogin(int type, WechatLoginCallback callback) {
        wechatLoginCallback = callback;
        wechatLoginCallbackType = type;
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        TripartiteLibInitUtils.getUtils().getWechat().sendReq(req);
    }


    public interface WechatLoginCallback{
        void wechatLoginCallback(String uid, boolean success, int type, @Nullable WechatResponceGetUserInfoEntity entity);
    }

    // ------------------------------  微信登陆部分   结束


    // ------------------------------  微信支付部分   开始

    public static WechatPlayCallback wechatPlayCallback;


    /**
     * 微信支付 使用工具类
     * @param context
     * @param members
     * @param playCallback
     */
    public static void wxPlay(Context context, WXResponseMembers members, final WechatOpenPlayCallback playCallback){
        IWXAPI mWxApi = WXAPIFactory.createWXAPI(context, null);
        PayReq req = new PayReq();
        req.appId = members.appid;// 微信开放平台审核通过的应用APPID
        req.partnerId = members.partnerid;// 微信支付分配的商户号
        req.prepayId = members.prepayid;// 预支付订单号
        req.nonceStr = members.noncestr;// 随机字符串
        req.timeStamp = members.timestamp;// 时间戳
        req.packageValue = "Sign=WXPay";//
        req.sign = members.sign;// 签名
        mWxApi.registerApp(WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE));
        boolean b = mWxApi.sendReq(req);
        LogUtils.showE("支付页面", "发起微信支付申请 = " + b);
        wechatPlayCallback = new WechatPlayCallback() {
            @Override
            public void wechatPlayCallback(WXPayCallback wxPayCallback) {
                LogUtils.showE("支付页面", "微信支付  结束回调 = " + wxPayCallback.type);
                switch (wxPayCallback.type) {
                    case 0:
                        if(playCallback != null){
                            playCallback.playSeccess();
                        }
                        break;
                    case -1:
                        if (TextUtils.isEmpty(wxPayCallback.showText)) {
                            ToastUtil.showToastString("请求失败，请更改其他支付方式");
                        } else {
                            ToastUtil.showToastString(wxPayCallback.showText);
                        }

                        break;
                    case -2:
                        if (TextUtils.isEmpty(wxPayCallback.showText)) {
                            ToastUtil.showToastString("支付失败");
                        } else {
                            ToastUtil.showToastString(wxPayCallback.showText);
                        }
                        break;
                    case -100000:
                        ToastUtil.showToastString("支付失败");
                        break;
                }
            }
        };
    }

    public interface WechatPlayCallback{
        void wechatPlayCallback(WXPayCallback wxPayCallback);
    }

    public interface WechatOpenPlayCallback{
        void playSeccess();
    }

    // ------------------------------  微信支付部分   结束


}
