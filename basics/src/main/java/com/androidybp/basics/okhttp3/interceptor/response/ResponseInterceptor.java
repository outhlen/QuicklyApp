package com.androidybp.basics.okhttp3.interceptor.response;


import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.configuration.ResponseInterceptorInterface;
import com.androidybp.basics.okhttp3.assist.ResponseAssist;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.utils.hint.SystemCodeUtils;

/**
 * 响应拦截器
 */

public class ResponseInterceptor {

    /**
     * @param jsonBean  后台响应对象
     * @return -1 说明不拦截  否则 说明拦截
     */
    public static int interceptor(ResponceJsonEntity jsonBean) {

        return -1;
    }

}
