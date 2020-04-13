package com.escort.carriage.android.mvp.persenter;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.ui.mvp.model.CommonMvpModel;
import com.androidybp.basics.ui.mvp.model.ModelInterface;
import com.androidybp.basics.ui.mvp.presenter.BaseActRequestPresenter;
import com.androidybp.basics.utils.permission.PermissionUtil;
import com.escort.carriage.android.configuration.ProjectCacheKeyConfig;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.SplashImvEntity;
import com.escort.carriage.android.ui.activity.SplashActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class SplashActPersenter extends BaseActRequestPresenter<SplashActivity, ResponceBean> implements
        CommonMvpModel.CommonMvpModelInterface {

    /**
     * 当前请求的 标示
     */
    private int requestType = 10001;



    public CommonMvpModel getModelObj(){
        return (CommonMvpModel)model;
    }

    @Override
    public Class getResponClass(int type)  {
        return ResponceBean.class;
    }



    @Override
    public ModelInterface<ResponceJsonEntity> getModel() {
        return new CommonMvpModel();
    }

    @Override
    public void initView() {
        //先检查权限
        //1、先授权
        PermissionUtil permissionUtil = new PermissionUtil();
        String[] umCutting = permissionUtil.mCuttingModel.getMemoryCutting();
        if (permissionUtil.hasPermission(((SplashActivity)view), umCutting)) {
            //有权限
            initPageData();

        } else {
            //没有权限  去授权
            ArrayList<String> strings = new ArrayList<String>(umCutting.length);
            Collections.addAll(strings,umCutting);
            permissionUtil.requestPermissions((SplashActivity)view, strings, 700);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 700){
            initPageData();
        }
    }


    private void initPageData() {
        //开启倒计时
        view.setPageView(null, 3);
        //先获取本地 缓存信息  顺便在去获取网络请求数据
        String projectDataEntity =
                CacheDBMolder.getInstance().getProjectDataEntity(ProjectCacheKeyConfig.splashImageEntityKey);
        if (projectDataEntity != null) {
            SplashImvEntity jsonBean = JsonManager.getJsonBean(projectDataEntity, SplashImvEntity.class);
            if (jsonBean != null) {
                view.setPageView(jsonBean, 1);
            }
        }
//        val body = RequestSignUtils.getRequestSignUtils().getRequestBody(ProjectUrl.PUBLIC_GETSLIDE).add("slide_cid", "5").build()
        FormBody body = new FormBody.Builder().add("slide_cid", "6").build();
        toService(ProjectUrl.PUBLIC_GETSLIDE, requestType, body);

    }

    @Override
    public void onResponseEntity(ResponceBean jsonBean, int finalRequestType) {
        if(jsonBean != null && jsonBean.data != null){

        } else {
            //清空数据
            CacheDBMolder.getInstance().removeProjectDataEntity(ProjectCacheKeyConfig.splashImageEntityKey);
        }

    }

    /**
     * 展示方法
     */
    public void toService(String url, int requestCode, RequestBody body) {
//        view?.showUplodingAnim(requestCode, "请求数据中")
        if (model.getRequestTage()) {
//            ToastUtil.showToastString("正在等待相应，请稍后")
        } else {
            model.toPostBodyService(url, body, requestCode, this, false, this);
        }
    }

}
