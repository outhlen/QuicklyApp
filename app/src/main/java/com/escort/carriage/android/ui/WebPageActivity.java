package com.escort.carriage.android.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.FunctionListActivity;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.bean.my.SharedJsBackBean;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.UserInfoManagerActivity;
import com.escort.carriage.android.ui.activity.login.LoginActivity;
import com.escort.carriage.android.ui.activity.login.RegisterPhoneActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tripartitelib.android.EnvironmentConfig;
import com.tripartitelib.android.TripartiteLibInitUtils;
import com.tripartitelib.android.wechat.WechatUtils;
import com.tripartitelib.android.wechat.bean.WechatResponceGetUserInfoEntity;
import com.tripartitelib.android.wechat.config.WeixinConfiguration;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  第三方登录 分享
 */
public class WebPageActivity extends ProjectBaseActivity {

    @BindView(R.id.wx_login)
    LinearLayout wxLogin;
    @BindView(R.id.wx_share)
    LinearLayout wxShare;
    @BindView(R.id.wx_wechar)
    LinearLayout wxChat;
    @BindView(R.id.wx_ercode)
    LinearLayout erCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_third);
        ButterKnife.bind(this);

    }

    private void showLoading() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "加载中...");
    }

    private void hideLoading() {
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
    }

    @OnClick({R.id.wx_login, R.id.wx_share, R.id.wx_wechar,R.id.wx_ercode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wx_login://微信登录
                WechatUtils.wxLogin(2, new WechatUtils.WechatLoginCallback() {
                    @Override
                    public void wechatLoginCallback(String uid, boolean success, int type, @Nullable WechatResponceGetUserInfoEntity entity) {
                        LogUtils.showE("微信授权登录", "uid = " + uid + "    entity = " + JsonManager.createJsonString(entity));
                        wxLoginStep2(entity.getUnionid(), entity.headimgurl, entity.nickname);
                    }
                });
                break;
            case R.id.wx_share://分享
                 String params   = "{\"url\":\"http://app_register.xeyb56.com/undertake?parentRecommend=\"}";
                 shared(params);
                break;
            case R.id.wx_wechar://朋友圈
                String params1   = "{\"url\":\"http://app_register.xeyb56.com/undertake?parentRecommend=\",\"shareTitle\":\"公共服务\",\"shareDescribe\":\"公共服务: 打破传统物流行业的潜规则，让客户拥有定价权 !\"}";
                wechat(params1);
                break;

        }
    }

    public  byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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


    private void wxLoginStep2(String code, String headImgUrl, String nickname) {
        showLoading();
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> data = new HashMap<>();
        data.put("wxUnionid", code);
        requestEntity.setData(data);
        String json = JsonManager.createJsonString(requestEntity);
        LogUtils.showE("WxentryActivity", "JSON = " + json);
        OkgoUtils.post(ProjectUrl.LOGINWXUNIONID, json).execute(new MyStringCallback<ResponseUserEntity>() {
            @Override
            public void onResponse(ResponseUserEntity resp) {
                hideLoading();
                if (resp.isSuccess()) {
                    EventBus.getDefault().post(resp.data);
                } else {
                    if ("11006".equals(resp.code)) {
                        //未绑定,跳注册
                        Intent intent = new Intent(WebPageActivity.this,
                                RegisterPhoneActivity.class);
                        intent.putExtra("wxUnionid", code);
                        intent.putExtra("headImgUrl", headImgUrl);
                        intent.putExtra("nickname", nickname);
                        intent.putExtra("openType", 1);
                        startActivityForResult(intent, 666);
                    } else {
                        ToastUtil.showToastString(resp.message);
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public Class<ResponseUserEntity> getClazz() {
                return ResponseUserEntity.class;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 666 && resultCode == 666){
            if(data != null){
                String json = data.getStringExtra("json");
                UserEntity jsonBean = JsonManager.getJsonBean(json, UserEntity.class);
                cacheData(jsonBean);
                finish();
            }
        }
    }

    private void cacheData(UserEntity jsonBean) {
        ApplicationContext.getInstance().isRegister = true;
        CacheDBMolder.getInstance().setUserInfo(jsonBean, null, null);
        CacheDBMolder.getInstance().setUserToken(jsonBean.getToken());
        //打开首页
        startActivity(new Intent(this, FunctionListActivity.class));
    }

}
