package com.escort.carriage.android.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.cache.db.model.CacheDataEntity;
import com.androidybp.basics.configuration.BaseProjectConfiguration;
import com.androidybp.basics.entity.UserEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.templet.listerner.DialogCallbackListener;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.file.SharedPrefUtil;
import com.androidybp.basics.utils.permission.CuttingModel;
import com.androidybp.basics.utils.permission.PermissionUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ResponseInterceptorInterfaceIm;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.request.home.HomeListRequestEnity;
import com.escort.carriage.android.ui.activity.login.LoginActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import com.escort.carriage.android.ui.view.dialog.UserAgreementDialog;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-18 16:37
 */
public class SplashTwoActivity extends ProjectBaseActivity {

    private PermissionUtil permissionUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_splash_two);
        //初始化Toast组件
        ApplicationContext.getInstance().examineNotifications(this);
        ApplicationContext.getInstance().setResponseInterceptorInterface(new ResponseInterceptorInterfaceIm());
        inspectUserData();
        permissionUtil = new PermissionUtil();

        CacheDataEntity cacheDataEntity = CacheDBMolder.getInstance().getCacheDataEntity(null);
        if(cacheDataEntity.firstOpen){
            UserAgreementDialog userAgreementDialog = new UserAgreementDialog(this);
            userAgreementDialog.setListener(new DialogCallbackListener() {
                @Override
                public void clickCallBack(Dialog dialog, int stype) {
                    switch (stype) {
                        case 1://确定
                            CacheDataEntity cacheDataEntity = CacheDBMolder.getInstance().getCacheDataEntity(null);
                            cacheDataEntity.firstOpen = false;
                            CacheDBMolder.getInstance().setCacheDataEntity(cacheDataEntity, null, null);
                            requestPermissionMethod(0);
                            if (dialog != null)
                                dialog.dismiss();

                            break;
                        case 2://推出应用
                            finish();
                            if (dialog != null)
                                dialog.dismiss();
                            break;
                        case 3://打开协议
                            Intent intentUserAgreement = new Intent(SplashTwoActivity.this, VueActivity.class);
                            intentUserAgreement.putExtra("url", VueUrl.userAgreement);
                            startActivity(intentUserAgreement);
                            break;
                        case 4://打开协议
                            Intent intentPrivacyAgreement = new Intent(SplashTwoActivity.this, VueActivity.class);
                            intentPrivacyAgreement.putExtra("url", VueUrl.privacyAgreement);
                            startActivity(intentPrivacyAgreement);
                            break;

                    }

                }
            });
            userAgreementDialog.getLeftBtn().setTextColor(ResourcesTransformUtil.getColor(com.androidybp.basics.R.color.color_666666));
            userAgreementDialog.setCanceledOnTouchOutside(false);
            userAgreementDialog.setCancelable(false);
            userAgreementDialog.show();
        } else {
            requestPermissionMethod(0);
        }




    }

    private void requestPermissionMethod(int type) {
        String[] umCutting = new CuttingModel().getSplashCutting();
        if (permissionUtil.hasPermission(this, umCutting)) {
            //由于目前页面初始化太快  做3秒延时 后打开对应页面
            if(type == 0){
                openNextAct();
            } else {
                ThreadUtils.openSonThread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                openNextAct();
                            }
                        });
                    }
                });
            }

        } else {
            List<String> strings = Arrays.asList(umCutting);
            permissionUtil.requestPermissions(this, strings, 700);
        }
    }


    private void inspectUserData() {
        //判断是否需要自动登陆进去  1、先判断token  2、在判断用户数据
        String userToken = CacheDBMolder.getInstance().getUserToken();
        if (!TextUtils.isEmpty(userToken)) {
            //在判断userInfo是否为空
            UserEntity userInfo = CacheDBMolder.getInstance().getUserInfo(null);
            ApplicationContext.getInstance().isRegister = userInfo != null;
        } else {
            ApplicationContext.getInstance().isRegister = false;
        }
    }


    private void openNextAct() {
        if (ApplicationContext.getInstance().isRegister == true) {
            openMainAct();
        } else {
            openLoginAct();
        }
    }

    private void openMainAct() {
//        startActivity(new Intent(this, MainActivity.class));
        HomeActivity.startHomeActivity(this);
        finish();
    }

    private void openLoginAct() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 700){
            String str = "程序运行需要开启必要权限，否则无法正常使用,请前往 应用信息-->权限 页面设置该应用权限为允许";
            boolean perm = permissionUtil.verifyPermissions(this, permissions, grantResults, null, str, null, null);
            if (perm) {
                //打开对应popWind
                openNextAct();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BaseProjectConfiguration.OPEN_PROJECT_CODE){
            requestPermissionMethod(1);
        }
    }
}
