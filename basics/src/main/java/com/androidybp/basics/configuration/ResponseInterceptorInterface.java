package com.androidybp.basics.configuration;

import android.app.Activity;

import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

public interface ResponseInterceptorInterface {
    void openLoginAct(ResponceJsonEntity jsonBean, int code);
    void exitLogin(Activity activity);
}
