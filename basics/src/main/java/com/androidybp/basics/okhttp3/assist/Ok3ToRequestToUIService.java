package com.androidybp.basics.okhttp3.assist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.okhttp3.OkHttpManager;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.okhttp3.listener.RequestCallback;
import com.androidybp.basics.okhttp3.listenerIm.OKResponseCallBack;
import com.androidybp.basics.utils.encryption.Base64Utils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.thread.ThreadUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.RequestBody;

public class Ok3ToRequestToUIService<T extends ResponceJsonEntity> extends OKResponseCallBack implements Runnable {

    private String url;
    private String json;
    private Class<T> clazz;
    private int requestType;
    //是否需要子线程返回数据  true -- 只需要子线程响应   false -- 只需要主线程响应  默认是 false
    private boolean isSonCallback = false;
    private Object hasCode;
    private RequestCallback<ResponceJsonEntity> callback;

    /**
     * @param url         请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param json        请求的json  可以为null
     * @param clazz       解析对象  不能为null
     * @param requestType 当前请求的标示 用于区分请求类型的
     * @param hasCode     当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isSonThread 是否必须在子线程中执行  true 必须在子线程中执行  false 什么线程都可以
     * @param callback    响应数据回调 可以为null
     */
    public Ok3ToRequestToUIService(String url, String json, Class<T> clazz, int requestType, Object hasCode, boolean isSonThread, RequestCallback<ResponceJsonEntity> callback) {
        init(url, json, clazz, requestType, hasCode, isSonThread, callback);
    }

    /**
     * @param url           请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param json          请求的json  可以为null
     * @param clazz         解析对象  不能为null
     * @param requestType   当前请求的标示 用于区分请求类型的
     * @param hasCode       当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isSonThread   是否必须在子线程中执行  true 必须在子线程中执行  false 什么线程都可以
     * @param isSonCallback 是否需要子线程返回数据  true -- 只需要子线程响应   false -- 只需要主线程响应  默认是 false
     * @param callback      响应数据回调 可以为null
     */
    public Ok3ToRequestToUIService(String url, String json, Class<T> clazz, int requestType, Object hasCode, boolean isSonThread, boolean isSonCallback, RequestCallback<ResponceJsonEntity> callback) {
        this.isSonCallback = isSonCallback;
        init(url, json, clazz, requestType, hasCode, isSonThread, callback);
    }

    private void init(String url, String json, Class<T> clazz, int requestType, Object hasCode, boolean isSonThread, RequestCallback<ResponceJsonEntity> callback) {
        if (!TextUtils.isEmpty(url) && clazz != null) {
            this.url = url;
            this.json = json;
            this.clazz = clazz;
            this.requestType = requestType;
            this.hasCode = hasCode;
            this.callback = callback;
            if (isSonThread) {
                if (ThreadUtils.isMainThread()) {
                    ThreadUtils.openSonThread(this);
                } else {
                    postService(url, json, requestType, hasCode);
                }
            } else {
                postService(url, json, requestType, hasCode);
            }

        }
    }


    private void postService(String url, String json, int requestType, Object hasCode) {

        try {
            LogUtils.showE("Ok3ToRequestToUIService", "URL = " + url + "\n请求带的Json =" + Base64Utils.decode(json));
            OkHttpManager.getManager().post(url, json, true, this, hasCode, requestType);

        } catch (Exception e) {
            e.printStackTrace();
            //4、在请求过程中 有异常了
            if (callback != null) {
                if (isNetworkConnected(ApplicationContext.getInstance().context)) {
                    //说明有网   只是网络链接超时了
                    callback.requestTimeOutMainThread(requestType);
                } else {
                    //说明没有网络链接了
                    callback.noNetworkConnected(requestType, null, e);

                }
            }
        }
    }


    /**
     * 判断是否有网络连接
     *
     * @param context 当前上下文
     *                *@return
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
    protected Class<?> setResponClass(int type) {
        return clazz;
    }

    @Override
    protected void interceptor(Object object, int interceptor, int type) {
        if (callback != null) {
            if (object == null) {
                callback.interceptor(null, interceptor, type);
            } else {
                callback.interceptor((ResponceJsonEntity) object, interceptor, type);
            }
        }
        clearData();
    }

    @Override
    protected void noNetworkConnected(int requestType, Call call, IOException e) {
        if (callback != null) {
            callback.noNetworkConnected(requestType, call, e);
        }
        clearData();
    }

    @Override
    protected void requestTimeOutMainThread(int type) {
        if (callback != null) {
            callback.requestTimeOutMainThread(type);
        }
        clearData();
    }

    @Override
    protected void responseBeanMainThread(Object mResponceBean, int type) {
        if(!isSonCallback){
            if (callback != null) {
                if (mResponceBean == null) {
                    callback.analysisEntityError(type);
                } else {
                    callback.onResponseEntity((ResponceJsonEntity) mResponceBean, type);
                }
            }
            clearData();
        }


    }

    @Override
    protected void responseBeanChildThread(Object jsonBean, int finalRequestType) {
        super.responseBeanChildThread(jsonBean, finalRequestType);
        if(isSonCallback){
            if (callback != null) {
                if (jsonBean == null) {
                    callback.analysisEntityError(finalRequestType);
                } else {
                    callback.onResponseEntity((ResponceJsonEntity) jsonBean, finalRequestType);
                }
            }
            clearData();
        }
    }

    @Override
    public void run() {
        postService(url, json, requestType, hasCode);
    }


    private void clearData() {
        this.url = null;
        this.json = null;
        this.clazz = null;
        this.hasCode = null;
        this.callback = null;
    }
}
