package com.androidybp.basics.okhttp3.assist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;


import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkHttpManager;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.okhttp3.interceptor.response.ResponseInterceptor;
import com.androidybp.basics.okhttp3.listener.RequestCallback;
import com.androidybp.basics.utils.encryption.Base64Utils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.thread.ThreadUtils;

import okhttp3.Response;

/**
 * 异步请求数据 同时异步响应, 无论从主线程调用还是从子线程调用 都会重新创建一个线程去自行网络请求操作
 *
 * 自带线程判断  可以在主线程中调用
 *
 * 里面自带拦截判断   只需要把回调实现好就行了
 *
 * 里面所有方法都是私有的  这就是一个工具类  别想着是万能的
 */
public class Ok3ToRequestToSonService<T extends ResponceJsonEntity> implements Runnable {

    private String url;
    private String json;
    private Class<T> clazz;
    private int requestType;
    private Object hasCode;
    private RequestCallback<ResponceJsonEntity> callback;
    /**
     *
     * @param url         请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param json        请求的json  可以为null
     * @param clazz       解析对象  不能为null
     * @param requestType       当前请求的标示 用于区分请求类型的
     * @param hasCode           当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param callback          响应数据回调 可以为null
     */
    public Ok3ToRequestToSonService(String url, String json, Class<T> clazz, int requestType, Object hasCode, RequestCallback<ResponceJsonEntity> callback){
        if(!TextUtils.isEmpty(url) && clazz != null){
            this.url = url;
            this.json = json;
            this.clazz = clazz;
            this.requestType = requestType;
            this.hasCode = hasCode;
            this.callback = callback;
            if(ThreadUtils.isMainThread()){
                ThreadUtils.openSonThread(this);
            } else {
                postService(url, json, clazz, requestType, hasCode, callback);
            }
        }
    }

    private void postService(String url, String json, Class<T> clazz, int requestType, Object hasCode, RequestCallback<ResponceJsonEntity> callback){

        try {
            Response response = OkHttpManager.getManager().postSynchronization(url, json, hasCode, true);
            String string = Base64Utils.decode(response.body().string());
            LogUtils.showE("Ok3ToRequestToSonService", "URL = " + url + "\n请求带的Json =" + Base64Utils.decode(json) + "\n响应Json = " + string);
            T t = JsonManager.getJsonBean(string, clazz);
            // 说明解析数据异常了 走异常流程
            if (t == null) {
                //1、解析后台返回数据异常了  调用 analysisEntityError
                if(callback != null){
                    callback.analysisEntityError(requestType);
                }
            } else {
                //2、先判断是否要登录拦截
                int interceptor = ResponseInterceptor.interceptor(t);
                if(interceptor != -1){
                    //登录拦截  调用 interceptor
                    if(callback != null){
                        callback.interceptor(t, interceptor, requestType);
                    }
                } else {
                    //3、说明数据请求成功了  调用  onResponseEntity
                    if(callback != null){
                        callback.onResponseEntity(t, requestType);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //4、在请求过程中 有异常了
            if(callback != null){
                if(isNetworkConnected(ApplicationContext.getInstance().context)){
                    //说明有网   只是网络链接超时了
                    callback.requestTimeOutMainThread(requestType);
                } else {
                    //说明没有网络链接了
                    callback.noNetworkConnected(requestType, null, e);

                }
            }
        } finally {
            this.url = null;
            this.json = null;
            this.clazz = null;
            this.hasCode = null;
            this.callback = null;
        }
    }

    /**
     * 判断是否有网络连接
     *
     * @param context 当前上下文
     * *@return
     */
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    public void run() {
        postService(url, json, clazz, requestType, hasCode, callback);
    }
}
