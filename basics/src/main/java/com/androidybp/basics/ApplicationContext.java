package com.androidybp.basics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import androidx.core.app.NotificationManagerCompat;
import com.androidybp.basics.configuration.ResponseInterceptorInterface;
import com.androidybp.basics.ui.view.toast.CustomToastUtils;
import com.androidybp.basics.utils.action_bar.NotificationsUtils;
import com.androidybp.basics.utils.hint.ToastUtil;


/**
 * Created by yangbp
 * Application改造后的辅助工具类  全局使用
 */

public class ApplicationContext {



    public Application application = null;
    public Context context = null;

    public boolean isRegister = false;//用于判断用户是否登录
    public String versionName;//当前程序的版本名
    public int versionCode;//当前程序的版本号
    private Handler mainHandler;
    private static volatile ApplicationContext mApplicationContext;
    public boolean isNotifications = false;//是否有通知权限
    private ResponseInterceptorInterface responseInterceptorInterface;


    /**
     * 单例获取 当前类的实例
     *
     * @return
     */
    public static ApplicationContext getInstance() {
        if (mApplicationContext == null) {
            synchronized (ApplicationContext.class) {
                if (mApplicationContext == null) {
                    createApplicationContext();
                }
            }
        }
        return mApplicationContext;
    }

    private static void createApplicationContext() {
        mApplicationContext = new ApplicationContext();
    }

    /**
     * 设置全局参数
     * @param app
     */
    public void setApplicationContext(Application app){
        setApplication(app);
        initBuild();
    }

    /**
     * 设置全局配置的 上下文对象
     * @param application
     */
    public void setApplication(Application application){
        this.application = application;
        context = application;
    }

    /**
     * 初始化全局配置参数
     */
    public void initBuild(){
        mainHandler = new Handler();
        versionName = getVersionName(application);
        versionCode = getVersionCode(application);
    }

    /**
     * 这个方法是用于判断全局数据是否回收的方法  防止重现后系统报错
     */
    public void setBuild(Application app){
        if(application == null)
            setApplication(app);
        if(mainHandler == null)
            mainHandler = new Handler();
        if(TextUtils.isEmpty(versionName))
            versionName = getVersionName(application);
        if(versionCode == 0)
            versionCode = getVersionCode(application);

    }



    /**
     * 获取全局的context
     * @return
     */
    public  Context getContext(){
        return context;
    }

    /**
     *
     * @return 资源对象
     */
    public  Resources getResorces(){
        if(application != null){
            return application.getResources();
        }else{
            return null;
        }
    }
    /**
     * 获取全局的主线程的handler
     * @return
     */
    public  Handler getMainHandler(){
        return mainHandler;
    }



    //版本名
    public  String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public  int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private  PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 检查通知权限方法  这个方法要在Splash页面进行调用
     * @param activity
     */
    public void examineNotifications(Activity activity){
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        isNotifications = new NotificationsUtils().isNotificationEnabled(activity);
        //初始化自定义Toast组件
        ToastUtil.init();

    }


    public ResponseInterceptorInterface getResponseInterceptorInterface() {
        return responseInterceptorInterface;
    }

    public void setResponseInterceptorInterface(ResponseInterceptorInterface responseInterceptorInterface) {
        this.responseInterceptorInterface = responseInterceptorInterface;
    }
}
