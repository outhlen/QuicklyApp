package com.tripartitelib.android.wechat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tripartitelib.android.EnvironmentConfig;
import com.tripartitelib.android.wechat.WechatUtils;
import com.tripartitelib.android.wechat.bean.WXPayCallback;
import com.tripartitelib.android.wechat.config.WeixinConfiguration;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Yangbp
 * @description: 微信支付使用方法
 * @date :2020-03-23 10:33
 */
public abstract class WXPayBaseActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_wx);
//        textView = (TextView) findViewById(R.id.demo_text_01);
//        textView2 = (TextView) findViewById(R.id.demo_text_02);
        api = WXAPIFactory.createWXAPI(this, WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE));
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if (baseReq != null) {
//            textView.setText("openId = " + baseReq.openId + "   transaction = " + baseReq.transaction);
            LogUtils.showE("WBShareActivity", "请求的 = " + baseReq.toString());
//            ToastUtil.showToast("请求的 = " + baseReq.toString());
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        WXPayCallback callback = new WXPayCallback();
        callback.responseClassName = "ChannelPaymentPageFragment";
        if (baseResp != null) {
            LogUtils.showE("WBShareActivity", "baseResp = " + baseResp.toString());
//            textView2.setText("errCode = " + baseResp.errCode + "   errStr = " +
//                    baseResp.errStr + "   openId = " + baseResp.openId + "   transaction = " + baseResp.transaction);
            switch (baseResp.errCode) {
                case 0:
                    callback.success = true;
                    break;
                case -1:
                    callback.success = false;
                    break;
                case -2:
                    callback.success = false;
                    break;
            }
            callback.type = baseResp.errCode;
            callback.showText = baseResp.errStr;

        } else {
            callback.type = -100000;
        }
        LogUtils.showE("支付页面", "微信支付  微信结束回调 = " + JsonManager.createJsonString(callback));
        if(WechatUtils.wechatPlayCallback != null){
            WechatUtils.wechatPlayCallback.wechatPlayCallback(callback);
        }
        finish();
    }
}