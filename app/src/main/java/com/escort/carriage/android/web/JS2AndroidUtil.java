package com.escort.carriage.android.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class JS2AndroidUtil {

    private Activity activity;

    public JS2AndroidUtil(Activity activity){
        this.activity = activity;
    }

    /**
     * 关闭容器页面
     */
    @JavascriptInterface
    public void finishPage() {
        if(activity != null){
            activity.finish();
        }
    }
}
