package com.escort.carriage.android.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkHttpManager;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tripartitelib.android.EnvironmentConfig;
import com.tripartitelib.android.TripartiteLibInitUtils;
import com.tripartitelib.android.wechat.WechatUtils;
import com.tripartitelib.android.wechat.bean.WechatResponceGetAccessTokenEntity;
import com.tripartitelib.android.wechat.bean.WechatResponceGetUniIdEntity;
import com.tripartitelib.android.wechat.bean.WechatResponceGetUserInfoEntity;
import com.tripartitelib.android.wechat.config.WeixinConfiguration;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-23 10:45
 */
public abstract class WXEntryBaseActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //这句没有写,是不能执行回调的方法的
        TripartiteLibInitUtils.getUtils().getWechat().handleIntent(getIntent(), this);
    }
    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp baseResp) {
        Log.i(TAG, "onResp:------>");
        Log.i(TAG, "error_code:---->" + baseResp.errCode);
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                ToastUtil.showToastString("拒绝授权微信登录");
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                ToastUtil.showToastString(message);
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK://用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    final String code = ((SendAuth.Resp) baseResp).code;
                    Log.i(TAG, "code:------>" + code);
                    if (WechatUtils.wechatLoginCallbackType == 0) {
                        //这里拿到了这个code，去做2次网络请求获取access_token
                        ThreadUtils.openSonThread(new Runnable() {
                            @Override
                            public void run() {
                                toServiceGetAccessToken(code);
                            }
                        });
                    } else if (WechatUtils.wechatLoginCallbackType == 1) {
                        WechatUtils.wechatLoginCallback.wechatLoginCallback(code, true, WechatUtils.wechatLoginCallbackType, null);
                        finish();
                    } else {
                        //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                        ThreadUtils.openSonThread(new Runnable() {
                            @Override
                            public void run() {
                                toServiceGetWechatUserInfo(code);
                            }
                        });
                    }

                } else if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
                    WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseResp;
                    String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
                    Log.d(TAG, "onResp   ---   " + extraData);
                    String msg = "onResp   ---   errStr：" + baseResp.errStr + " --- errCode： " + baseResp.errCode + " --- transaction： "
                            + baseResp.transaction + " --- openId：" + baseResp.openId + " --- extMsg：" + launchMiniProResp.extMsg;
                    Log.d(TAG, msg);
                    //UnifyPayPlugin.getInstance(this).getWXListener().onResponse(this, baseResp);
                    finish();
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    ToastUtil.showToastString("微信分享成功");
                }
                break;
        }
//        //无论什么方式 都要将页面关闭
//        finish();
    }

    /**
     * 这种方式貌似不可行
     *
     * @param code
     */
    private void toServiceGetAccessToken(String code) {
        showLoading();
        // 去微信获取 accessToken
        final StringBuilder authURL = new StringBuilder();
        authURL.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
        authURL.append("appid=").append(WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE));
        authURL.append("&secret=").append(WeixinConfiguration.getWeixinAppkey(EnvironmentConfig.APK_CURRENT_TYPE));
        authURL.append("&code=").append(code);
        authURL.append("&grant_type=authorization_code");
        try {
            String responceStr = OkHttpManager.getManager().get(authURL.toString());
            final WechatResponceGetUniIdEntity jsonBean = JsonManager.getJsonBean(responceStr, WechatResponceGetUniIdEntity.class);
            if (jsonBean != null && !TextUtils.isEmpty(jsonBean.unionid)) {
                hideLoading();
                ThreadUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        WechatUtils.wechatLoginCallback.wechatLoginCallback(jsonBean.getUnionid(), true, WechatUtils.wechatLoginCallbackType, null);
                    }
                });

            } else {
                hideLoading();
                ToastUtil.showToastString("获取授权失败");
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            ToastUtil.showToastString("获取授权失败");
        }
        mianThreadFinish();

    }


    private void toServiceGetWechatUserInfo(String code) {
        showLoading();
        // 去微信获取 accessToken
        StringBuilder authURL = new StringBuilder();
        authURL.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
        authURL.append("appid=").append(WeixinConfiguration.getWeixinAppId(EnvironmentConfig.APK_CURRENT_TYPE));
        authURL.append("&secret=").append(WeixinConfiguration.getWeixinAppkey(EnvironmentConfig.APK_CURRENT_TYPE));
        authURL.append("&code=").append(code);
        authURL.append("&grant_type=authorization_code");
        try {
            String responceStr = OkHttpManager.getManager().get(authURL.toString());
            final WechatResponceGetAccessTokenEntity jsonBean = JsonManager.getJsonBean(responceStr, WechatResponceGetAccessTokenEntity.class);
            if (jsonBean != null && !TextUtils.isEmpty(jsonBean.access_token)) {
                StringBuilder userUrl = new StringBuilder();
                userUrl.append("https://api.weixin.qq.com/sns/userinfo?");
                userUrl.append("access_token=").append(jsonBean.access_token);
                userUrl.append("&openid=").append(jsonBean.openid);
                String userInfoStr = OkHttpManager.getManager().get(userUrl.toString());
                final WechatResponceGetUserInfoEntity userInfo = JsonManager.getJsonBean(userInfoStr, WechatResponceGetUserInfoEntity.class);
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getUnionid())) {
                    hideLoading();
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            WechatUtils.wechatLoginCallback.wechatLoginCallback(userInfo.getUnionid(), true, WechatUtils.wechatLoginCallbackType, userInfo);
                        }
                    });
                } else {
                    hideLoading();
                    ToastUtil.showToastString("获取授权失败");
                }

            } else {
                hideLoading();
                ToastUtil.showToastString("获取授权失败");
            }
        } catch (Exception e) {
            hideLoading();
            e.printStackTrace();
            ToastUtil.showToastString("获取授权失败");
        }
        mianThreadFinish();

    }


    /**
     * 处理微信发出的向第三方应用请求app message
     * <p>
     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
     * 做点其他的事情，包括根本不打开任何页面
     */
    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null) {
            Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
            startActivity(iLaunchMyself);
        }
    }

    /**
     * 处理微信向第三方应用发起的消息
     * <p>
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     * <p>
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
    public void onShowMessageFromWXReq(WXMediaMessage msg) {
//		ToastUtil.showShort(this, "hahhahahahha");
        if (msg != null && msg.mediaObject != null && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "加载中...");
    }

    private void hideLoading() {
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
    }

    private void mianThreadFinish() {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }

}
