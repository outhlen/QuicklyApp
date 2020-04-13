package com.androidybp.basics.okhttp3;

import android.text.TextUtils;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.R;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.okhttp3.assist.ResponseAssist;
import com.androidybp.basics.okhttp3.interceptor.Okhttp3Interceptor;
import com.androidybp.basics.okhttp3.listenerIm.MyDns;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 这是一个用于访问网络的工具类
 *
 * 特殊方法：
 *      postAssignSessionId（） -- 给定session请求数据
 */
public class OkHttpManager {
    private static final String CHARSET_NAME = "UTF-8";
//    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    //        private static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).build();
    private OkHttpClient okHttpClient = getHttpsClient();
    private static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static volatile OkHttpManager okHttpManager;

    /**
     * 获取单例
     * @return
     */

    public static OkHttpManager getManager(){
        if(okHttpManager == null){
            synchronized (OkHttpManager.class){
                if(okHttpManager == null){
                    okHttpManager = new OkHttpManager();
                }
            }
        }
        return okHttpManager;
    }

    /*
    *******************************************************************************************************
                     ↓↓↓↓↓↓↓↓ 创建支持https不带证书验证的 OKhttpClient     开始  ↓↓↓↓↓↓↓↓
    *******************************************************************************************************/
    public OkHttpClient.Builder getHttpsClientBuilder(){

        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            //将请求超时时间 设置为15秒
            builder.readTimeout(15, TimeUnit.SECONDS);
            builder.writeTimeout(15, TimeUnit.SECONDS);
            builder.connectTimeout(15, TimeUnit.SECONDS);
//            builder.addInterceptor(new Okhttp3Interceptor());
            //使用自定义的DNS
            builder.dns(new MyDns());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OkHttpClient getHttpsClient(){
        return getHttpsClientBuilder().build();
    }

    public void setOkhttp(OkHttpClient client){

        okHttpClient = client;

    }

    public  OkHttpClient getOkHttp() {
        return okHttpClient;
    }
    /*
    *******************************************************************************************************
          ↑↑↑↑↑↑↑↑↑↑         创建支持https不带证书验证的 OKhttpClient     结束  ↑↑↑↑↑↑↑↑↑↑
    *******************************************************************************************************/



    /**
     * 异步post json
     *
     * @param url
     * @param json
     * @param responseCallback
     * @throws IOException
     */
    public void post(String url, String json, Callback responseCallback) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("params", json);
        RequestBody body = bodyBuilder.build();
//        RequestBody body = RequestBody.create(JSON, json);
        Request request;
        if (!ApplicationContext.getInstance().isRegister) {
            //判断当前用户是在登录状态
            request = setIdentification().url(url).post(body).build();
        } else {
            request = setLocation().url(url).post(body).build();
        }
        enqueue(request, responseCallback);
    }

    /**
     * 同步post json数据   目前是获取代办事宜小红点使用  和 获取通讯录  发给服务器使用
     *
     * @param url   请求接口
     * @param json  json数据
     * @param obj   请求标识  用于杀死请求的标识
     * @param isAnalysisJession   是否需要sessionId 拦截
     * @return  response对象
     * @throws IOException
     */
    public Response postSynchronization(String url, String json, Object obj, boolean isAnalysisJession) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = setLocation().url(url).tag(obj).post(body).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            if(isAnalysisJession){
                String cookie = ResponseAssist.analysisCookie(response);
                ResponseAssist.getCookieHeader(cookie);
                LogUtils.showE("jsession", "jsession = " + cookie);
            }
            return response;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 同步post json数据   目前是获取代办事宜小红点使用  和 获取通讯录  发给服务器使用
     *
     * @param url   请求接口
     * @param body  请求体
     * @param obj   请求标识  用于杀死请求的标识
     * @param isAnalysisJession   是否需要sessionId 拦截  正常情况下需要拦截的
     * @return  response对象
     * @throws IOException
     */
    public  Response postSynchronization(String url, RequestBody body, Object obj, boolean isAnalysisJession) throws IOException {
        Request request = setLocation().url(url).tag(obj).post(body).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            if(isAnalysisJession){
                String cookie = ResponseAssist.analysisCookie(response);
                ResponseAssist.getCookieHeader(cookie);
                LogUtils.showE("jsession", "jsession = " + cookie);
            }
            return response;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 通用同步请求。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public  Response execute(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }

    /**
     * 通用异步请求
     *
     * @param request
     * @param responseCallback
     */
    public  void enqueue(Request request, Callback responseCallback) {
        okHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request Request的对象
     */
    public  void enqueue(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

        });
    }


    /**
     * 同步get
     *
     * @param url
     * @return
     */
    public String get(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
//
//    /**
//     * 同步get请求
//     *
//     * @param url
//     * @param data
//     * @return
//     */
//    public String get(String url, Map<String, String> data) throws Exception {
//        url = getRequestUrl(url, data);
//        Request request = new Request.Builder().url(url).build();
//        Response response = execute(request);
//        if (response.isSuccessful()) {
//            return response.body().string();
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
//    }
//
    /**
     * 异步get请求
     *
     * @param url
     * @param responseCallback
     * @return
     */
    public  void get(String url, Callback responseCallback){
        Request request = new Request.Builder().url(url).build();
        enqueue(request, responseCallback);
    }
//
//    /**
//     * 同线程去请求网络获取 返回数据
//     * @param url
//     * @return
//     */
//    public  Response getReturnResponse(String url) throws Exception {
//        Request request = new Request.Builder().url(url).build();
//        return execute(request);
//    }
//
//    /**
//     * 异步get
//     *
//     * @param url
//     * @param data
//     * @param responseCallback
//     * @return
//     */
//    public void get(String url, Map<String, String> data, Callback responseCallback) throws Exception {
//        url = getRequestUrl(url, data);
//        Request request = new Request.Builder().url(url).build();
//
//        enqueue(request, responseCallback);
//    }
//
//    /**
//     * 同步post json数据
//     *
//     * @param url
//     * @param json
//     * @return
//     * @throws IOException
//     */
//    public String post(String url, String json) throws IOException {
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = setIdentification().url(url).post(body).build();
//        Response response = execute(request);
//        if (response.isSuccessful()) {
//            return response.body().string();
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
//    }
//
//
//    /**
//     * 同步post 键值对数据
//     *
//     * @param url  地址
//     * @param data 内容
//     * @return 相应体
//     * @throws IOException
//     */
//    public String post(String url, Map<String, String> data) throws IOException {
//        FormBody.Builder builder = new FormBody.Builder();
//        for (Map.Entry<String, String> item : data.entrySet()) {
//            builder.add(item.getKey(), item.getValue());
//        }
//
//        FormBody body = builder.build();
//        Request request = new Request.Builder().url(url).post(body).build();
//
//        Response response = execute(request);
//        if (response.isSuccessful()) {
//            return response.body().string();
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
//    }
//
//    /**
//     * 同步 post 传递字符串
//     *
//     * @param url  地址
//     * @param data 内容
//     * @throws IOException
//     */
//    public String postString(String url, String data) throws IOException {
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, data))
//                .build();
//        Response response = execute(request);
//        if (response.isSuccessful()) {
//            return response.body().string();
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
//    }
//
//    /**
//     * 给服务器使用 json方式提交数据 不去管是否成功
//     *
//     * @param url  访问的url
//     * @param json 带的json
//     * @throws IOException
//     */
//    public  void postNoResponse(String url, String json) throws IOException {
//        if (TextUtils.isEmpty(url))
//            return;
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = setLocation().url(url).post(body).build();
//        enqueue(request);
//
//    }
//
//    /**
//     * 给服务器使用 json方式提交数据 不去管是否成功
//     *
//     * @param url  访问的url
//     * @param json 带的json
//     * @throws IOException
//     */
//    public  void postAssignSessionId(String url, String json, String sessionId, Object hasCode, int type, Callback responseCallback) throws IOException {
//        if (TextUtils.isEmpty(url))
//            return;
//        RequestBody body = RequestBody.create(JSON, json);
//        Request.Builder rqanty = setIdentification();
//        if(!TextUtils.isEmpty(sessionId)){
//            rqanty.addHeader("cookie", sessionId.trim());
//        }
//        Request request = rqanty.addHeader("rqanty", String.valueOf(type)).url(url).post(body).tag(hasCode).build();
//        enqueue(request, responseCallback);
//    }


    /**
     * @param url              访问地址
     * @param json             请求json
     * @param responseCallback 接受响应的实体类
     * @param hasCode          当前请求的标识
     * @throws IOException
     */
    public  void post(String url, String json, Callback responseCallback, Object hasCode) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request;

        if (!ApplicationContext.getInstance().isRegister) {//判断当前用户是在登录状态
            request = setIdentification().url(url).post(body).tag(hasCode).build();
        } else {
            request = setLocation().url(url).post(body).tag(hasCode).build();
        }
        enqueue(request, responseCallback);
    }

    /**
     * @param url              访问地址
     * @param json             请求json
     * @param responseCallback 接受响应的实体类
     * @param flag             是否登录
     * @throws IOException
     */
    public  void post(String url, String json, boolean flag, Callback responseCallback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request;
        if (!flag) {//判断当前用户是在登录状态
            request = setIdentification().url(url).post(body).build();
        } else {
            request = setLocation().url(url).post(body).build();
        }
        enqueue(request, responseCallback);
    }

    /**
     * @param url              访问地址
     * @param json             请求json
     * @param responseCallback 接受响应的实体类
     * @param flag             是否登录
     * @param hasCode          当前请求的标识
     * @throws IOException
     */
    public  void post(String url, String json, boolean flag, Callback responseCallback, Object hasCode) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request;
        if (!flag) {//判断当前用户是在登录状态
            request = setIdentification().url(url).post(body).tag(hasCode).build();
        } else {
            request = setLocation().url(url).post(body).tag(hasCode).build();
        }
        enqueue(request, responseCallback);
    }

    /**
     * @param url              访问地址
     * @param json             请求json
     * @param responseCallback 接受响应的实体类
     * @param flag             是否上传SessionId
     * @param hasCode          当前请求的标识
     * @param type             当前请求的标识
     * @throws IOException
     */
    public void post(String url, String json, boolean flag, Callback responseCallback,
                            Object hasCode, int type) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request;
        request = setRequestHeader(type, !flag)
                .url(url)
                .post(body)
                .tag(hasCode)
                .build();
        enqueue(request, responseCallback);
    }

    /**
     * @param url              访问地址
     * @param body             请求Body
     * @param responseCallback 接受响应的实体类
     * @param flag             是否上传SessionId
     * @param hasCode          当前请求的标识
     * @param type             当前请求的标识
     * @throws IOException
     */
    public void post(String url, RequestBody body, boolean flag, Callback responseCallback,
                            Object hasCode, int type) throws IOException {
        Request request;
        request = setRequestHeader(type, !flag)
                .url(url)
                .post(body)
                .tag(hasCode)
                .build();
        enqueue(request, responseCallback);
    }



    /**
     * 设置请求头   添加一个标识 用于在响应的时候 判断请求的类型
     *
     *  "rqante"  --  request android type  每个单词的前两个单词
     * @param type 当前请求网络的类型
     *             @param flag 是否要带cookie   true -- 不带cookie    false 是带coolie
     * @return
     */
    private  Request.Builder setRequestHeader(int type, boolean flag){
        if(flag){
            return setIdentification().addHeader("rqanty", String.valueOf(type));
        }else{
            return setLocation().addHeader("rqanty", String.valueOf(type));
        }

    }

    /**
     * 设置请求头
     * @return
     */
    public  Request.Builder setLocation() {
        String token = CacheDBMolder.getInstance().getUserToken();
        Request.Builder builder = setIdentification();
        if(!TextUtils.isEmpty(token)) {//如果
            builder = builder.addHeader("Authorization", "Bearer " + token);
        }
        return builder;
    }

    public  Request.Builder setIdentification() {
        return new Request.Builder();
    }

    /**
     * 异步post key-value
     *
     * @param url
     * @param data
     * @param responseCallback
     * @throws IOException
     */
    public  void post(String url, Map<String, String> data, Callback responseCallback) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> item : data.entrySet()) {
            builder.add(item.getKey(), item.getValue());
        }
        //   RequestBody requestBody = RequestBody.create(MediaType.parse(""), "");
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();

        enqueue(request, responseCallback);
    }



    /**
     * 同步put
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String put(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(url).put(body).build();

        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 同步put key-value
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    public String put(String url, Map<String, String> data) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> item : data.entrySet()) {
            builder.add(item.getKey(), item.getValue());
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).put(body).build();

        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 异步put json
     *
     * @param url
     * @param json
     * @throws IOException
     */
    public void put(String url, String json, Callback responseCallback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(url).put(body).build();
        enqueue(request, responseCallback);
    }

    /**
     * 异步put key-value
     *
     * @param url
     * @param data
     * @param responseCallback
     * @throws IOException
     */
    public void put(String url, Map<String, String> data, Callback responseCallback) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> item : data.entrySet()) {
            builder.add(item.getKey(), item.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).put(body).build();

        enqueue(request, responseCallback);
    }



    public String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     *
     * @param url
     * @param name
     * @param value
     * @return
     */
    public String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }

    /**
     * get方式URL拼接
     *
     * @param url
     * @param map
     * @return
     */
    private  String getRequestUrl(String url, Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return url;
        } else {
            StringBuilder newUrl = new StringBuilder(url);
            if (url.indexOf("?") == -1) {
                newUrl.append("?rd=" + Math.random());
            }

            for (Map.Entry<String, String> item : map.entrySet()) {
                if (false == TextUtils.isEmpty(item.getKey().trim())) {
                    try {
                        newUrl.append("&" + item.getKey().trim() + "=" + URLEncoder.encode(String.valueOf(item.getValue().trim()), "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return newUrl.toString();
        }
    }

    /**
     * 干掉指定的任务
     *
     * @param tag
     */

    public  void cancelByTag(Object tag) {
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    /**
     * 上传图片
     */
    public void put(MediaType mediaType, String uploadUrl, String localPath, Callback back) throws IOException {
        File file = new File(localPath);
        RequestBody body = RequestBody.create(mediaType, file);
        Request request = new Request.Builder()
                .url(uploadUrl)
                .put(body)
                .build();

        enqueue(request, back);

    }

    //上传JPG图片
    public void putImg(String uploadUrl, String localPath, Callback back) throws IOException {
//        MediaType Image = MediaType.parse("image/jpeg; charset=utf-8");
        MediaType Image = MediaType.parse("image/png; charset=UTF-8");
        put(Image, uploadUrl, localPath, back);
    }

    public void putFile(String url, String filePath, Callback back) {

        File file = new File(filePath);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        enqueue(request, back);
    }

    public void zhyPutImag(String url, String filePath, Callback back) {
        File file = new File(filePath);

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"username\""),
                        RequestBody.create(null, "android"))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"tupian.jpg\""), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        enqueue(request, back);
    }

    /**
     * 批量上传图片   要求后台的名字为 mFile   这个方法可行
     *
     * @param url     接口
     * @param paths   要上传的图片地址
     * @param back    Callback对象
     * @param hasCode 当前请求的标识  用于取消请求用的  一般为当前对象的hashCode
     */
    public void requestImages(String url, String[] paths, Callback back, Object hasCode) {
//        "application/octet-stream"
        MediaType Image = MediaType.parse("image/png; charset=UTF-8");


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"username\""),
                RequestBody.create(null, "android"));
        for (String path : paths) {
            builder.addPart(Headers.of(
                    "Content-Disposition",
                    "form-data; name=\"mFile\"; filename=\"tupian.jpg\""), RequestBody.create(MediaType.parse("image/png; charset=UTF-8"), new File(path)));
        }
        //构建请求体
        RequestBody requestBody = builder.build();
        //构建请求
        if (ApplicationContext.getInstance().isRegister) {
            Request request = setLocation().tag(hasCode)
                    .url(url)//地址
                    .post(requestBody)//添加请求体
                    .build();
            enqueue(request, back);
        } else {
            ToastUtil.showToastResources(R.string.please_login);
        }
    }

     class OkhttpInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtils.showE("Okhttp --- request", "request:" + request.toString());
            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            LogUtils.showE("Okhttp --- request", String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtils.showE("Okhttp --- response body", "response body:" + content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }
}


