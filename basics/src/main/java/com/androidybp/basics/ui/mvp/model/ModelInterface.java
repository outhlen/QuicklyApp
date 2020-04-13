package com.androidybp.basics.ui.mvp.model;

import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.okhttp3.listener.RequestCallback;
import okhttp3.RequestBody;

public interface ModelInterface<T extends ResponceJsonEntity> {

    /**
     * @param url         请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param json        请求的json  可以为null
     * @param requestType 当前请求的标示 用于区分请求类型的
     * @param hasCode     当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isCurrentThread 是否必须在子线程中执行  true 必须在子线程中执行  false 在当前线程中执行
     * @param callback    响应数据回调 可以为null
     */
    void toPostJsonService(String url, String json, int requestType, Object hasCode, boolean isCurrentThread, RequestCallback<T> callback);

    /**
     * @param url           请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param json          请求的json  可以为null
     * @param requestType   当前请求的标示 用于区分请求类型的
     * @param hasCode       当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isCurrentThread   是否必须在子线程中执行  true 必须在子线程中执行  false 在当前线程中执行
     * @param isSonCallback 是否需要子线程返回数据  true -- 只需要子线程响应   false -- 只需要主线程响应  默认是 false
     * @param callback      响应数据回调 可以为null
     */
    void toPostJsonService(String url, String json, int requestType, Object hasCode, boolean isCurrentThread, boolean isSonCallback, RequestCallback<T> callback);

    /**
     * @param url         请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param body        请求的RequestBody
     * @param requestType 当前请求的标示 用于区分请求类型的
     * @param hasCode     当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isCurrentThread 是否必须在子线程中执行  true 必须在子线程中执行  false 在当前线程中执行
     * @param callback    响应数据回调 可以为null
     */
    void toPostBodyService(String url, RequestBody body, int requestType, Object hasCode, boolean isCurrentThread, RequestCallback<T> callback);

    /**
     * @param url           请求的url  不能为null  如果为null 将不走流程  也没有回调
     * @param body          请求的RequestBody
     * @param requestType   当前请求的标示 用于区分请求类型的
     * @param hasCode       当前请求的一个标示 用于页面关闭后 去掉请求链接的一个code标示
     * @param isCurrentThread   是否必须在子线程中执行  true 必须在子线程中执行  false 在当前线程中执行
     * @param isSonCallback 是否需要子线程返回数据  true -- 只需要子线程响应   false -- 只需要主线程响应  默认是 false
     * @param callback      响应数据回调 可以为null
     */
    void toPostBodyService(String url, RequestBody body, int requestType, Object hasCode, boolean isCurrentThread, boolean isSonCallback, RequestCallback<T> callback);


    /**
     * 销毁请求
     */
    void clearData();


    /**
     * 销毁请求  为了清除一些临时变量
     */
    void clear();

    /**
     * true 代表数据正在请求中
     * false 当前没有正在请求的数据
     */
    boolean getRequestTage();
}
