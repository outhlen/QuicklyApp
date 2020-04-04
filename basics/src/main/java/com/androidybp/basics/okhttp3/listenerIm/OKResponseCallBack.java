package com.androidybp.basics.okhttp3.listenerIm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.text.TextUtils;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.assist.ResponseAssist;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.okhttp3.interceptor.response.ResponseInterceptor;
import com.androidybp.basics.utils.encryption.Base64Utils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.thread.ThreadUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 这个类  把所有数据都解析出来了   返回调用的方法都是主线程
 * <p>
 * 这个方法只适合 单向操作  如果要多并发  请求  那就创建多个对象
 */

public abstract class OKResponseCallBack implements Callback {
    protected int type;
    public Class<?> clazz;

    public OKResponseCallBack() {
        this.clazz = setResponClass(type);
    }

    /**
     * 必须是继承 ResponceJsonEntity  对象的子类
     *
     * @param type
     * @return
     */
    protected abstract Class<?> setResponClass(int type);

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public synchronized void onFailure(Call call, IOException e) {
        String s = call.request().headers().get("rqanty");
        int requestType = type;
        if (!TextUtils.isEmpty(s)) {
            try {
                requestType = Integer.parseInt(s);
            } catch (Exception ex) {
                requestType = type;
            }
        }
        if(isNetworkConnected(ApplicationContext.getInstance().context)){
            //说明有网   只是网络链接超时了
            callbackTimeOutMainMethod(requestType, requestType);
        } else {
            //说明没有网络链接了
            noNetworkConnectedMainThreadCallback(requestType, call, e);

        }

    }

    @Override
    public synchronized void onResponse(Call call, Response response) {
        if (getSleepOneSecond()) {
            SystemClock.sleep(1000);
        }
        String s = response.request().headers().get("rqanty");
        int requestType = type;
        if (!TextUtils.isEmpty(s)) {
            try {
                requestType = Integer.parseInt(s);
            } catch (Exception e) {
                requestType = type;
            }
        }
        clazz = setResponClass(requestType);
        final int finalRequestType = requestType;//桥接 内部类使用的  没有具体意义
        String decode = null;
        try {
            String string = response.body().string();
//            decode = Base64Utils.decode(string);
            decode = string;
            LogUtils.showE("数据响应", "requestUrl = " + response.request().url().toString() + "     响应数据 = " + decode);
            final Object jsonBean = JsonManager.getJsonBean(decode, clazz);
            final int interceptor = ResponseInterceptor.interceptor((ResponceJsonEntity) jsonBean);
            if (interceptor != -1) {
                ThreadUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        interceptor(jsonBean, interceptor, finalRequestType);
                    }
                });
            } else {
                String cookie = ResponseAssist.analysisCookie(response);
                boolean isUpdateCache = ResponseAssist.getCookieHeader(cookie);//解析session
                responseSessionId(cookie, isUpdateCache);
                if (ThreadUtils.isMainThread()) {
                    responseBeanMainThread(jsonBean, finalRequestType);
                } else {
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            responseBeanMainThread(jsonBean, finalRequestType);
                        }
                    });
                }
                responseBeanChildThread(jsonBean, finalRequestType);
            }

        } catch (Exception e) {
            callbackTimeOutMainMethod(requestType, finalRequestType);
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

    /**
     * 每次解析都会调用这个方法  自己做好判断
     *
     * @param cookie        sessionId 值
     * @param isUpdateCache 是否更换了本地的
     */
    protected void responseSessionId(String cookie, boolean isUpdateCache) {

    }

    /**
     * 子类选择性重写  是否需要  父类当session改变后  自动更新缓存数据
     *
     * @param finalRequestType 当前的请求类型
     * @return true -- (默认)需要父类更新缓存   false -- 不需要父类更新
     */
    protected boolean isUpdateCache(int finalRequestType) {
        return true;
    }

    /**
     * 子线程调用  子类可以选择性  实现
     *
     * @param jsonBean
     * @param finalRequestType
     */
    protected void responseBeanChildThread(Object jsonBean, int finalRequestType) {

    }

    /**
     * 当请求超时  或者  解析数据失败的时候 调用这个方法
     *
     * @param requestType
     * @param finalRequestType
     */
    private void callbackTimeOutMainMethod(int requestType, final int finalRequestType) {
        if (ThreadUtils.isMainThread()) {
            requestTimeOutMainThread(requestType);
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    requestTimeOutMainThread(finalRequestType);
                }
            });
        }
    }

    /**
     * 没有网络的时候   执行这个方法   在
     * @param requestType
     * @param call
     * @param e
     */
    private void noNetworkConnectedMainThreadCallback(final int requestType, final Call call, final IOException e){
        if(ThreadUtils.isMainThread()){
            noNetworkConnected(requestType, call, e);
        }else{
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    noNetworkConnected(requestType, call, e);
                }
            });
        }

    }

    /**
     * 拦截器拦截回调
     *
     * @param object      后台返回的数据对象 可强转
     * @param interceptor 拦截类型  见 TypeModel
     * @param type        当前网络请求的标识
     */
    protected abstract void interceptor(Object object, int interceptor, int type);

    /**
     * 没有网络链接的时候走这个方法  子类酌情重写  给出对应的提示  并执行无网情况的动作
     * @param requestType  请求的code
     * @param call         本次请求的Call对象
     * @param e            异常信息
     */
    protected abstract void noNetworkConnected(int requestType, Call call, IOException e);

    //访问超时   主线程
    protected abstract void requestTimeOutMainThread(int type);

    //主线程  来处理最外层的Bean对象      带有类型
    protected abstract void responseBeanMainThread(Object mResponceBean, int type);

    /**
     * 是否需要睡眠上1秒  为了响应太快导致 主线程堵塞 导致的页面卡顿问题
     *
     * @return true 需要延迟1秒
     * false  不需要延迟1秒
     * <p>
     * 默认是不需要延迟的
     */
    protected boolean getSleepOneSecond() {
        return false;
    }


}
