package com.escort.carriage.android.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.utils.Sh256;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-16 15:26
 */
public class ForgetSetPwdActivity extends ProjectBaseEditActivity {

    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etPwd2)
    EditText etPwd2;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_forget_pwd_set);
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

    @OnClick(R.id.tvFinish)
    public void onViewClicked() {
        String pwdStr = etPwd.getText().toString();
        String pwd2Str = etPwd2.getText().toString();

        if (TextUtils.isEmpty(pwdStr)) {
            ToastUtil.showToastString("请输入密码");
        } else if(TextUtils.isEmpty(pwd2Str)){
            ToastUtil.showToastString("请再次输入密码");
        } else if(!TextUtils.equals(pwdStr, pwd2Str)){
            ToastUtil.showToastString("两次密码不一致");
        } else if(checkPwdIsNo(pwdStr)){
            ToastUtil.showToastString("请设置6-10位数字和字母组合");
        } else {
            Intent intent = getIntent();
            String phone = intent.getStringExtra("phone");
            String smsCode = intent.getStringExtra("smsCode");
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            HashMap<String, Object> data = new HashMap<>();
            data.put("phone", phone);
            data.put("code", smsCode);
            data.put("loginPwd", Sh256.getSHA256(pwdStr));
            data.put("loginPwdAgain", Sh256.getSHA256(pwd2Str));
            requestEntity.setData(data);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.LOGIN_FORGETPASSWORD, jsonString).execute(new MyStringCallback<ResponceBean>() {
                @Override
                public void onResponse(ResponceBean s) {
                    UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                    if(s != null ){
                        if(s.success){
                            setResult(666);
                            finish();
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
     * 密码不可以
     */
    public static boolean checkPwdIsNo(String pwd) {
        if (pwd.matches("[0-9]+")) {
            return true;
        }
        if (pwd.matches("[a-zA-Z]+")) {
            return true;
        }
        if (pwd.length() < 6) {
            return true;
        }
        if (pwd.length() > 10) {
            return true;
        }
        return false;
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
