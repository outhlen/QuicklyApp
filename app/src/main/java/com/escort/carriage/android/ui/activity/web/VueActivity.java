package com.escort.carriage.android.ui.activity.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.my.SharedJsBackBean;
import com.escort.carriage.android.ui.activity.base.BaseWebViewActivity;
import com.escort.carriage.android.ui.view.ProgressView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tripartitelib.android.EnvironmentConfig;
import com.tripartitelib.android.TripartiteLibInitUtils;
import com.tripartitelib.android.wechat.config.WeixinConfiguration;


import java.io.ByteArrayOutputStream;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VueActivity extends BaseWebViewActivity {

    @BindView(R.id.progressView)
    ProgressView mProgressView;
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_vue;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initWebView(mWebView, mProgressView);

        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void jsCallback(String action, String param) {
                LogUtils.showE("LHF", "网页回调 = action =" + action + ";    param = " + param);
                if (TextUtils.isEmpty(action)) {
                    return;
                }
                switch (action) {
                    case "back":
                        finish();
                        break;
                    case "shared"://TODO: 微信分享
                        shared(param);
                        break;
                    case "wechat":
                        wechat(param);
                        break;
                    case "setTradePassword"://修改交易密码成功
                        UserEntity userInfo = CacheDBMolder.getInstance().getUserInfo(null);
                        userInfo.setIsSetTradePassword(1);
                        CacheDBMolder.getInstance().setUserInfo(userInfo, null, null);
                        break;
                }
            }

            /**
             * 获取登录返回的数据对象 json
             * @return
             */
            @JavascriptInterface
            public String getUserLoginInfo() {
                return JsonManager.createJsonString(CacheDBMolder.getInstance().getUserInfo(null));
            }

            /**
             * 获取用户 详情信息的 json
             * @return
             */
            @JavascriptInterface
            public String getUserInfo() {
                return JsonManager.createJsonString(CacheDBMolder.getInstance().getUserInfoEntity(null));
            }


        }, "android");
        initBranch();
    }



    private void initBranch() {
        String url = getIntent().getStringExtra("url");
        if (url.contains("?")) {
            url += "&token=" + CacheDBMolder.getInstance().getUserToken();
        } else {
            url += "?token=" + CacheDBMolder.getInstance().getUserToken();
        }
        UserEntity userInfo = CacheDBMolder.getInstance().getUserInfo(null);
        if(userInfo != null){
            url += "&phone=" + userInfo.getPhone()
                    + "&isSetTradePassword=" + userInfo.getIsSetTradePassword()
                    + "&selfRecommend=" + userInfo.getSelfRecommend();
        }

        LogUtils.showE("LHF", "网页url = " + url);
        loadUrl(url);
    }

    private void shared(String param) {
        if (TextUtils.isEmpty(param)) {
            ToastUtil.showToastString("参数获取失败");
            return;
        }
        SharedJsBackBean bean = JsonManager.getJsonBean(param, SharedJsBackBean.class);


        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();

        UserEntity userInfo = CacheDBMolder.getInstance().getUserInfo(null);

        textObj.text = bean.getUrl() + userInfo.getSelfRecommend();

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = textObj.text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.userOpenId = WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE);
        //调用api接口，发送数据到微信
        TripartiteLibInitUtils.getUtils().getWechat().sendReq(req);

    }


    private void wechat(String param) {
        if (TextUtils.isEmpty(param)) {
            ToastUtil.showToastString("参数获取失败");
            return;
        }
        SharedJsBackBean bean = JsonManager.getJsonBean(param, SharedJsBackBean.class);


        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        UserEntity userInfo = CacheDBMolder.getInstance().getUserInfo(null);
        webpage.webpageUrl = bean.getUrl()  + userInfo.getSelfRecommend();

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = bean.getShareTitle();
        msg.description = bean.getShareDescribe();
        Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = bitmap2Bytes(thumbBmp);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        req.userOpenId = WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE);

        //调用api接口，发送数据到微信
        TripartiteLibInitUtils.getUtils().getWechat().sendReq(req);
    }

    public  byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
