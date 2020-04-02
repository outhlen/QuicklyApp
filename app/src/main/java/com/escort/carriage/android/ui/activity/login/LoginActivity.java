package com.escort.carriage.android.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.manager.listener.LoginActInstener;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.verification.VerificationUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.login.WxLoginDataEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserEntity;
import com.escort.carriage.android.entity.response.login.ResponseWxUserEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.jpush.JPushUtil;
import com.escort.carriage.android.jpush.JpushConfig;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.utils.Sh256;
import com.lzy.okgo.model.Response;
import com.tripartitelib.android.wechat.WechatUtils;
import com.tripartitelib.android.wechat.bean.WechatResponceGetUserInfoEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends ProjectBaseEditActivity implements LoginActInstener {


    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPwd)
    TextView tvForgetPwd;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.ivPhoneLogin)
    ImageView ivPhoneLogin;
    @BindView(R.id.ivWxLogin)
    ImageView ivWxLogin;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPwd)
    EditText etPwd;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        setPageActionBar();
        removeUserInfo();
//        etUserName.setText("17865159987");
//        etPwd.setText("123456a");

//        etUserName.setText("13075364484");
//        etPwd.setText("123456qq");


    }

    public void removeUserInfo(){
        CacheDBMolder.getInstance().clearData();
        ApplicationContext.getInstance().isRegister = false;
    }


    private void setPageActionBar() {
        //获取顶部状态栏的高度 给对应View设置高度
        View statusBar = findViewById(R.id.statusBarView);
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        statusBar.setLayoutParams(layoutParams);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setText("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveUserEntity(UserEntity entity) {
        if(entity != null){
            cacheData(entity);
            finish();
        }
    }

    private void cacheData(UserEntity jsonBean) {
        ApplicationContext.getInstance().isRegister = true;
        CacheDBMolder.getInstance().setUserInfo(jsonBean, null, null);
        CacheDBMolder.getInstance().setUserToken(jsonBean.getToken());
        //打开首页
//        startActivity(new Intent(this, MainActivity.class));
        HomeActivity.startHomeActivity(this);
    }



    @OnClick({R.id.tvLogin, R.id.tvForgetPwd, R.id.tvRegister, R.id.ivPhoneLogin, R.id.ivWxLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                login();
                break;
            //忘记密码
            case R.id.tvForgetPwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            //注册
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterPhoneActivity.class));
                break;
            //手机号登陆
            case R.id.ivPhoneLogin:
                startActivityForResult(new Intent(this, LoginPhoneActivity.class), 666);
                break;
            case R.id.ivWxLogin:
                WechatUtils.wxLogin(2, new WechatUtils.WechatLoginCallback() {
                    @Override
                    public void wechatLoginCallback(String uid, boolean success, int type, @Nullable WechatResponceGetUserInfoEntity entity) {
                        LogUtils.showE("微信授权登录", "uid = " + uid + "    entity = " + JsonManager.createJsonString(entity));
                        wxLoginStep2(entity.getUnionid(), entity.headimgurl, entity.nickname);
//                        wxLoginStep1(uid);
                    }
                });
                break;
        }
    }

    private void login() {
        String pwd = etPwd.getText().toString();
        String name = etUserName.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtil.showToastString("请输入手机号");
        } else if(TextUtils.isEmpty(pwd)){
            ToastUtil.showToastString("请输入密码");
        } else if(!VerificationUtil.verificationText(name, VerificationUtil.MOBILE_NUM)){
            ToastUtil.showToastString("请输入正确的手机号");
        } else {
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            HashMap<String, Object> data = new HashMap<>();
            data.put("phone", name);
//            data.put("password", pwd);
            data.put("password", Sh256.getSHA256(pwd));
            data.put("registerId", JPushInterface.getRegistrationID(this));
            requestEntity.setData(data);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.LOGIN, jsonString).execute(new MyStringCallback<ResponseUserEntity>() {
                @Override
                public void onResponse(ResponseUserEntity s) {
                    UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                    if(s != null ){
                        if(s.success){
                            cacheData(s.data);
                            finish();
                        } else {
                            ToastUtil.showToastString(s.message);
                        }
                    }
                }

                @Override
                public Class<ResponseUserEntity> getClazz() {
                    return ResponseUserEntity.class;
                }
            });
        }
    }

    private void showLoading() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "加载中...");
    }

    private void hideLoading() {
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
    }
    private void wxLoginStep1(String code) {
        showLoading();
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> data = new HashMap<>();
        data.put("param", code);
        requestEntity.setData(data);
        String json = JsonManager.createJsonString(requestEntity);
        LogUtils.showE("微信授权登录", ProjectUrl.GETUNIONID + "    请求JSON = " + json);
        OkgoUtils.post(ProjectUrl.GETUNIONID, json).execute(new MyStringCallback<ResponseWxUserEntity>() {
            @Override
            public void onResponse(ResponseWxUserEntity resp) {
                hideLoading();
                if (resp.isSuccess()) {
                    WxLoginDataEntity data = resp.getData();
                    wxLoginStep2(data.getUnionid(), data.getHeadimgurl(), data.getNickname());
                } else {
                    ToastUtil.showToastString(resp.message);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public Class<ResponseWxUserEntity> getClazz() {
                return ResponseWxUserEntity.class;
            }
        });

    }


    private void wxLoginStep2(String code, String headImgUrl, String nickname) {
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
                        Intent intent = new Intent(LoginActivity.this,
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
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
