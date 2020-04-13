package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.utils.Sh256;

import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Yangbp
 * @description: 设置中心 修改密码
 * @date :2020-03-16 15:26
 */
public class UpdataPwdActivity extends ProjectBaseEditActivity {

    @BindView(R.id.tv_pass_old_edt)
    EditText etOldPwd;

    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etPwd2)
    EditText etPwd2;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_updata_pwd);
        bind = ButterKnife.bind(this);
        setPageActionBar();
    }


    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("修改密码");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.tvFinishRegister)
    public void onViewClicked() {
        String oldPw = etOldPwd.getText().toString();
        String pwdStr = etPwd.getText().toString();
        String pwd2Str = etPwd2.getText().toString();

//        if (TextUtils.isEmpty(oldPw)) {
//            ToastUtil.showToastString("请输入旧密码");
//        } else
            if (TextUtils.isEmpty(pwdStr)) {
            ToastUtil.showToastString("请输入新密码");
        } else if(TextUtils.isEmpty(pwd2Str)){
            ToastUtil.showToastString("请再次输入密码");
        } else if(!TextUtils.equals(pwdStr, pwd2Str)){
            ToastUtil.showToastString("两次密码不一致");
        } else if(checkPwdIsNo(pwdStr)){
            ToastUtil.showToastString("请设置6-10位数字和字母组合");
        } else {
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            HashMap<String, Object> data = new HashMap<>();
            if(!TextUtils.isEmpty(oldPw)){
                data.put("oldLoginPwd", Sh256.getSHA256(oldPw));
            }
            data.put("loginPwd", Sh256.getSHA256(pwdStr));
            data.put("loginPwdAgain", Sh256.getSHA256(pwdStr));
            requestEntity.setData(data);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.LOGIN_UPDATELOGINPASSWORDCHECK, jsonString).execute(new MyStringCallback<ResponceBean>() {
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
