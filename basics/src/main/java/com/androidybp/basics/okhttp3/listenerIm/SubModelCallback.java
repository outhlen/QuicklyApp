package com.androidybp.basics.okhttp3.listenerIm;

import android.app.Activity;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;

public abstract class SubModelCallback extends OKResponseCallBack {

    public Activity mActivity;

    public SubModelCallback(){
        this.mActivity = setmActivity();
    }

    @Override
    public void interceptor(Object object, int interceptor, int type) {
        ApplicationContext.getInstance().isRegister = false;
        //清除Token和 session id
        CacheDBMolder.getInstance().removeTokenAndJsessionId(0);
        QuitChat();
        if(isInterceptor()){
            startLogActMain(mActivity);
        }
        interceptor(object, type);
    }

    /**
     * 设置Activity 对象   用来打开页面
     *
     * @return Activity对象
     */
    protected abstract Activity setmActivity();

    //拦截器拦截了
    public abstract void interceptor(Object object, int type);

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    /**
     * //手动登出网易
     * */
//    public abstract void QuitChat() {
//        LogoutImUtils.logoutImOrCache();
//    }
    public abstract void QuitChat();

    public abstract  void startLogActMain(Activity mActivity) ;

    /**
     * 是否需要进行全局登录拦截 默认是true的
     * @return
     */
    public boolean isInterceptor(){
        return true;
    }

    public void clearmActivity(){
        mActivity = null;
    }
}