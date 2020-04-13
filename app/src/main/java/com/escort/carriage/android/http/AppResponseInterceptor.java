package com.escort.carriage.android.http;

import android.text.TextUtils;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.configuration.ResponseInterceptorInterface;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-16 11:00
 */
public class AppResponseInterceptor {
    /**
     * @param jsonBean  后台响应对象
     * @return -1 说明不拦截  否则 说明拦截
     */
    public static int interceptor(ResponceJsonEntity jsonBean) {

        if (jsonBean != null) {
            if(TextUtils.equals("10001", jsonBean.code)){
                ResponseInterceptorInterface responseInterceptorInterface = ApplicationContext.getInstance().getResponseInterceptorInterface();
                if(responseInterceptorInterface != null){
                    responseInterceptorInterface.openLoginAct(jsonBean, 10001);
                }
                return 10001;
            }
        }
        return -1;
    }
}
