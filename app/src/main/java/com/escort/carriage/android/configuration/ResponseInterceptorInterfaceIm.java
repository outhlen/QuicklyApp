package com.escort.carriage.android.configuration;

import android.app.Activity;
import android.content.Intent;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.cache.db.model.DataCacheKeyModel;
import com.androidybp.basics.configuration.ResponseInterceptorInterface;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.ui.manager.activity.ProjectActivityManager;
import com.escort.carriage.android.ui.activity.login.LoginActivity;

public class ResponseInterceptorInterfaceIm implements ResponseInterceptorInterface {
    @Override
    public void openLoginAct(ResponceJsonEntity jsonBean, int code) {
        Activity act = getOpenAct();
        act.startActivity(new Intent(act, LoginActivity.class));
        act.finish();
    }

    @Override
    public void exitLogin(Activity activity) {
        if(activity == null){
            activity = getOpenAct();
        }
        //清除所有用户数据
        ApplicationContext.getInstance().isRegister = false;
        //删除本地缓存的 高德地图配置
        CacheDBMolder.getInstance().removeProjectCacheData(DataCacheKeyModel.AMAP_DATA);
        CacheDBMolder.getInstance().clearData();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
        finishActs();
    }

    /**
     * 关闭除登录页面以外的全部页面
     */
    private void finishActs() {
        ProjectActivityManager.getManager().finishActsNotLogin();
    }

    private Activity getOpenAct(){
        return ProjectActivityManager.getManager().getOpenAct();
    }
}
