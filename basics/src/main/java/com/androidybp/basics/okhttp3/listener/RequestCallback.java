package com.androidybp.basics.okhttp3.listener;

import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

import okhttp3.Call;

public interface RequestCallback<T extends ResponceJsonEntity> {
    /**
     * 拦截器拦截回调
     *
     * @param jsonBean      后台返回的数据对象 可强转
     * @param interceptor 拦截类型  见 TypeModel
     * @param type        当前网络请求的标识
     */
    void interceptor(T jsonBean, int interceptor, int type);

    /**
     * 没有网络链接的时候走这个方法  子类酌情重写  给出对应的提示  并执行无网情况的动作
     * @param requestType  请求的code
     * @param call         本次请求的Call对象
     * @param e            异常信息
     */
    void noNetworkConnected(int requestType, Call call, Exception e);

    /**
     * 访问超时   主线程
     *
     * @param type  请求的code
     */
    void requestTimeOutMainThread(int type);

    /**
     * 解析返回数据时异常了
     * @param type   请求的code
     */
    void analysisEntityError(int type);

    /**
     * 子线程调用  子类可以选择性  实现
     *
     * @param jsonBean
     * @param finalRequestType
     */
    void onResponseEntity(T jsonBean, int finalRequestType);


}
