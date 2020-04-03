package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.verification.VerificationUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserEntity;
import com.escort.carriage.android.http.MyStringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 修改手机号
 */
public class ChangePhoneActivity extends ProjectBaseActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvGetCheckCode)
    TextView tvGetCheckCode;
    @BindView(R.id.etSmsCode)
    EditText etSmsCode;


    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_change_phone);
        bind = ButterKnife.bind(this);

        setPageActionBar();

        ButterKnife.bind(this);
//        mTvTitle.setText("修改手机号");

    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("修改手机号");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.tvGetCheckCode, R.id.tvSubmit})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tvGetCheckCode://获取验证码

                break;
            case R.id.tvSubmit://提交
//                changePhone();
                getSmsCode();
                break;
        }
    }

    /**
     * 修改
     */
    private void loginByPhone() {
        String phone = etPhone.getText().toString();
        String code = etSmsCode.getText().toString();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToastString("请输入手机号");
        } else if(TextUtils.isEmpty(code)){
            ToastUtil.showToastString("请输入验证码");
        } else if(!VerificationUtil.verificationText(phone, VerificationUtil.MOBILE_NUM)){
            ToastUtil.showToastString("请输入正确的手机号");
        } else {
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            HashMap<String, Object> data = new HashMap<>();
            data.put("phone", phone);
            data.put("code", code);
            requestEntity.setData(data);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.USERLOGIN_UPDATEUSERPHONE, jsonString).execute(new MyStringCallback<ResponseUserEntity>() {
                @Override
                public void onResponse(ResponseUserEntity s) {
                    UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                    if(s != null ){
                        if(s.success){
                            ToastUtil.showToastString("修改成功");
                            finish();
                        }else {
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



    /**
     * 获取验证码
     */
    private void getSmsCode() {
        String phone = etPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToastString("请输入手机号");
        } else if(!VerificationUtil.verificationText(phone, VerificationUtil.MOBILE_NUM)){
            ToastUtil.showToastString("请输入正确的手机号");
        } else {
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            requestEntity.setData(phone);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.USERLOGIN_UPDATEPHONECODE, jsonString).execute(new MyStringCallback<ResponceBean>() {
                @Override
                public void onResponse(ResponceBean s) {
                    UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                    if(s != null ){
                        if(s.success){
                            Intent intent = new Intent(ChangePhoneActivity.this, ChangePhoneSetCodeActivity.class);
                            intent.putExtra("phoneNum", phone);
                            startActivityForResult(intent, 333);

                        }else {
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
        tvGetCheckCode.setText((System.currentTimeMillis() / 1000)+"秒后重试");
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetCheckCode.setText((millisUntilFinished / 1000) + "秒后重试");
            }

            @Override
            public void onFinish() {
                tvGetCheckCode.setClickable(true);//把TV设置为可点击
                tvGetCheckCode.setText("获取验证码");
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 333 && resultCode == 666){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind != null){
            bind.unbind();
            bind = null;
        }
    }
}
