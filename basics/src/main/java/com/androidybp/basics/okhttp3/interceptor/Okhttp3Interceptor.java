package com.androidybp.basics.okhttp3.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangbp
 */
public class Okhttp3Interceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response proceed;
        Request request = chain.request();
//        if(BaseProjectConfiguration.requestIntercept){
//            String url = request.url().toString();
//            String replace = url.replace(UrlModel.NAME, BaseProjectUrl.OFFICIAL_NAME);
//            Request newRequest = request.newBuilder().url(replace).build();
//            proceed = chain.proceed(newRequest);
//        } else {
            proceed = chain.proceed(request);
//        }

        return proceed;
    }
}
