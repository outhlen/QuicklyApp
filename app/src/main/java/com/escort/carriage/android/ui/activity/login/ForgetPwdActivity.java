package com.escort.carriage.android.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.verification.VerificationUtil;
import com.escort.carriage.android.ProjectApplication;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

public class ForgetPwdActivity extends ProjectBaseEditActivity {


    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etSmsCode)
    EditText etSmsCode;
    @BindView(R.id.tvGetCheckCode)
    TextView tvGetCheckCode;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_forget_pwd);
        bind = ButterKnife.bind(this);
        setPageActionBar();
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

    @OnClick({R.id.tvGetCheckCode, R.id.tvNext, R.id.tvLoginError})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetCheckCode:
                getSmsCode();
                break;
            case R.id.tvNext:
                next();
                break;
            case R.id.tvLoginError:
                ToastUtil.showToastString("登录遇到问题，请联系客服");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666 && resultCode == 666) {
            finish();
        }
    }

    /**
     * 下一步
     */
    private void next() {
        String phone = etUserName.getText().toString();
        String code = etSmsCode.getText().toString();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToastString("请输入手机号");
        } else if(TextUtils.isEmpty(code)){
            ToastUtil.showToastString("请输入验证码");
        } else if(!VerificationUtil.verificationText(phone, VerificationUtil.MOBILE_NUM)){
            ToastUtil.showToastString("请输入正确的手机号");
        } else {

        }
        Intent intent = new Intent(this, ForgetSetPwdActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("smsCode", code);
        startActivityForResult(intent, 666);
    }



    /**
     * 获取验证码
     */
    private void getSmsCode() {
        String phone = etUserName.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastString("请输入手机号");
        } else if (!VerificationUtil.verificationText(phone, VerificationUtil.MOBILE_NUM)) {
            ToastUtil.showToastString("请输入正确的手机号");
        } else {
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            requestEntity.setData(phone);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.LOGIN_SENDSMSFORGETPASSWORD, jsonString).execute(new MyStringCallback<ResponceBean>() {
                @Override
                public void onResponse(ResponceBean s) {
                    UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                    if (s != null) {
                        if (s.success) {
                            ToastUtil.showToastString("发送成功");
                            delayed();
                        } else {
                            ToastUtil.showToastString(s.message);
                        }
                    }
                }

                @Override
                public Class<ResponceBean> getClazz() {
                    return ResponceBean.class;
                }
            });
        }
    }


    /**
     * 延时验证码
     */
    private void delayed() {
        //一分钟以后重试
        tvGetCheckCode.setClickable(false);//把TV设置为不可点击
        long time = System.currentTimeMillis() / 1000;
        tvGetCheckCode.setText(time + "秒后重试");
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if(tvGetCheckCode != null){
                    tvGetCheckCode.setText(time + "秒后重试");
                }
            }

            @Override
            public void onFinish() {
                if(tvGetCheckCode != null){
                    tvGetCheckCode.setClickable(true);//把TV设置为可点击
                    tvGetCheckCode.setText("获取验证码");
                }
            }
        }.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
    }

}
