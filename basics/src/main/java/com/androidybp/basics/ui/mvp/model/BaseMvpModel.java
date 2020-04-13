package com.androidybp.basics.ui.mvp.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.okhttp3.OkHttpManager;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.okhttp3.listener.RequestCallback;
import com.androidybp.basics.okhttp3.listenerIm.OKResponseCallBack;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.thread.ThreadUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.RequestBody;

/**
 * 通用的model 管理类
 */
public abstract class BaseMvpModel<T extends ResponceJsonEntity> extends OKResponseCallBack implements Runnable, ModelInterface<T> {

    protected String url;
    protected String json;
    protected RequestBody body;
    protected int requestType;
    //是否需要子线程返回数据  true -- 子线程响应   false -- 主线程响应  默认是 false
    protected boolean isSonCallback = false;
    protected Object hasCode;
    protected boolean isRequest;
    protected RequestCallback<T> callback;
    public boolean isPostRequest = true;


    /**
     *
     *
     * @param url           请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param json          请求的json  可以为null
     * @param requestType   当前请求的标示 用于区分请求类型的
     * @param hasCode       当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isSonThread   是否必须在子线程中执行  true 必须在子线程中执行  false 在当前线程中执行
     * @param callback      响应数据回调 可以为null
     */
    private void init(String url, String json, int requestType, Object hasCode, boolean isSonThread, RequestCallback<T> callback) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            this.json = json;
            this.requestType = requestType;
            this.hasCode = hasCode;
            this.callback = callback;
            if (isSonThread) {
                if (ThreadUtils.isMainThread()) {
                    ThreadUtils.openSonThread(this);
                } else {
                    toServer(url, json, requestType, hasCode);
                }
            } else {
                toServer(url, json, requestType, hasCode);
            }

        }
    }
/**
     *
     *
     * @param url           请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param body          请求的RequestBody
     * @param requestType   当前请求的标示 用于区分请求类型的
     * @param hasCode       当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isSonThread   是否必须在子线程中执行  true 必须在子线程中执行  false 在当前线程中执行
     * @param callback      响应数据回调 可以为null
     */
    private void init(String url, RequestBody body, int requestType, Object hasCode, boolean isSonThread, RequestCallback<T> callback) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            this.body = body;
            this.requestType = requestType;
            this.hasCode = hasCode;
            this.callback = callback;
            if (isSonThread) {
                if (ThreadUtils.isMainThread()) {
                    ThreadUtils.openSonThread(this);
                } else {
                    toServer(url, body, requestType, hasCode);
                }
            } else {
                toServer(url, body, requestType, hasCode);
            }

        }
    }

    private void toServer(String url, String json, int requestType, Object hasCode) {

        try {
            isRequest = true;
            LogUtils.showE("BaseMvpModel", "URL = " + url + "\n请求带的Json =" + json);
            if(isPostRequest){
                OkHttpManager.getManager().post(url, json, true, this, hasCode, requestType);
            } else {
                OkHttpManager.getManager().post(url, json, true, this, hasCode, requestType);
            }

        } catch (Exception e) {
            e.printStackTrace();
            isRequest = false;
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

    private void toServer(String url, RequestBody body, int requestType, Object hasCode) {

        try {
            isRequest = true;
            OkHttpManager.getManager().post(url, body, true, this, hasCode, requestType);

        } catch (Exception e) {
            e.printStackTrace();
            isRequest = false;
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


    @Override
    public boolean getRequestTage() {
        return isRequest;
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
    protected void interceptor(Object object, int interceptor, int type) {
        isRequest = false;
        if (callback != null) {
            if (object == null) {
                callback.interceptor(null, interceptor, type);
            } else {
                setInterceptorCallback(object, interceptor, type);
            }
        }
    }

    /**
     * 子类可以选择行性重写
     * @param object         后台返回的解析对象
     * @param interceptor    拦截标示
     * @param type           请求标示
     */
    protected void setInterceptorCallback(Object object, int interceptor, int type) {
        callback.interceptor((T)object, interceptor, type);
    }

    @Override
    protected void noNetworkConnected(int requestType, Call call, IOException e) {
        isRequest = false;
        if (callback != null) {
            callback.noNetworkConnected(requestType, call, e);
        }
    }

    @Override
    protected void requestTimeOutMainThread(int type) {
        isRequest = false;
        if (callback != null) {
            callback.requestTimeOutMainThread(type);
        }
    }

    @Override
    protected void responseBeanMainThread(Object mResponceBean, int type) {
        isRequest = false;
        if(!isSonCallback){
            if (callback != null) {
                if (mResponceBean == null) {
                    callback.analysisEntityError(type);
                } else {
                    setResponseCallback(mResponceBean, type);
                }
            }
        }


    }

    /**
     * 设置回调数据的方法
     * 子类可以选择性自己重写
     * @param mResponceBean   相应数据的对象
     * @param type            请求标示
     */
    protected void setResponseCallback(Object mResponceBean, int type) {
        callback.onResponseEntity((T)mResponceBean, type);
    }

    @Override
    protected void responseBeanChildThread(Object jsonBean, int finalRequestType) {
        super.responseBeanChildThread(jsonBean, finalRequestType);
        isRequest = false;
        if(isSonCallback){
            if (callback != null) {
                if (jsonBean == null) {
                    callback.analysisEntityError(finalRequestType);
                } else {
                    setResponseCallback(jsonBean, finalRequestType);
                }
            }
        }
    }

    @Override
    public void run() {
        if(body != null){
            toServer(url, body, requestType, hasCode);
        } else {
            toServer(url, json, requestType, hasCode);
        }

    }

    @Override
    public void toPostJsonService(String url, String json, int requestType, Object hasCode, boolean isSonThread, RequestCallback<T> callback) {
        init(url, json, requestType, hasCode, isSonThread, callback);
    }

    @Override
    public void toPostJsonService(String url, String json, int requestType, Object hasCode, boolean isSonThread, boolean isSonCallback, RequestCallback<T> callback) {
        this.isSonCallback = isSonCallback;
        init(url, json, requestType, hasCode, isSonThread, callback);
    }

    @Override
    public void toPostBodyService(String url, RequestBody body, int requestType, Object hasCode, boolean isCurrentThread, RequestCallback<T> callback) {
        init(url, body, requestType, hasCode, isCurrentThread, callback);
    }

    @Override
    public void toPostBodyService(String url, RequestBody body, int requestType, Object hasCode, boolean isCurrentThread, boolean isSonCallback, RequestCallback<T> callback) {
        this.isSonCallback = isSonCallback;
        init(url, body, requestType, hasCode, isCurrentThread, callback);
    }

    @Override
    public void clearData() {
        this.url = null;
        this.json = null;
        this.body = null;
        this.clazz = null;
        this.hasCode = null;
        this.callback = null;
    }

    @Override
    public void clear(){
        clearData();
    }
}
