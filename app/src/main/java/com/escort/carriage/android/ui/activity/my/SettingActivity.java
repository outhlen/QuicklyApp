package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseSingleClickActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.web.VueActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-20 17:05
 */
public class SettingActivity extends ProjectBaseSingleClickActivity {

    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_setting);
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
        tvTitle.setText("设置");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @OnClick({R.id.llChangeAccount, R.id.llPayPwd, R.id.llLoginPwd, R.id.llChangePhone, R.id.llUserAgreement, R.id.llPrivacyRights, R.id.llAbout, R.id.tvExitLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llChangeAccount:
                break;
            case R.id.llPayPwd://交易密码
                Intent intentPayPwd = new Intent(this, VueActivity.class);
                intentPayPwd.putExtra("url", VueUrl.setTradePassword);
                startActivity(intentPayPwd);
                break;
            case R.id.llLoginPwd:
                startActivity(new Intent(this, UpdataPwdActivity.class));
                break;
            case R.id.llChangePhone:
                startActivity(new Intent(this, ChangePhoneActivity.class));
                break;
            case R.id.llUserAgreement://用户协议
                Intent intentUserAgreement = new Intent(this, VueActivity.class);
                intentUserAgreement.putExtra("url", VueUrl.userAgreement);
                startActivity(intentUserAgreement);
                break;
            case R.id.llPrivacyRights://隐私权限
                Intent intentPrivacyAgreement = new Intent(this, VueActivity.class);
                intentPrivacyAgreement.putExtra("url", VueUrl.privacyAgreement);
                startActivity(intentPrivacyAgreement);
                break;

            case R.id.llAbout://关于我们
                Intent intentAbout = new Intent(this, VueActivity.class);
                intentAbout.putExtra("url", VueUrl.versionInformation);
                startActivity(intentAbout);
                break;
            case R.id.tvExitLogin:
                //调用接口  退出登录
                outLogin();
                break;
        }
    }

    private void outLogin(){
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "提交数据");

        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USERINFO_GETUSERINFO, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        ApplicationContext.getInstance().getResponseInterceptorInterface().exitLogin(null);
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
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

    }
}
