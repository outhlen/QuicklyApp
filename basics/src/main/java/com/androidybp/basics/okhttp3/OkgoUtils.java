package com.androidybp.basics.okhttp3;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.utils.hint.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OkgoUtils {
    /***
     * 判断返回的数据 status 、info
     */
    public static HashMap<String, String> httpinfo(String content) {
        String status = "";
        String info = "";
        String data = "";
        try {
            JSONObject json = new JSONObject(content);
            status = json.optString("flag");
            info = json.optString("msg");
            data = json.optString("data");
        } catch (JSONException e) {
            LogUtils.showI("HttpUtils", content);
            e.printStackTrace();
            info = "服务器数据异常";
        }
        HashMap<String, String> map = new HashMap<>();

        map.put("flag", status);
        map.put("msg", info);
        map.put("data", data);

        return map;
    }


    public static GetRequest<String> get(String url) {
        String token = CacheDBMolder.getInstance().getUserToken();

        return OkGo.<String>get(url).tag(ApplicationContext.getInstance().context)
                .headers("Authorization", "Bearer " + token);
    }

    public static DeleteRequest<String> delete(String url) {
        String token = CacheDBMolder.getInstance().getUserToken();

        return OkGo.<String>delete(url).tag(ApplicationContext.getInstance().context)
                .headers("Authorization", "Bearer " + token);
    }


    public static PostRequest<String> post(String url, String json) {
        LogUtils.showE("请求的Json", "json = " + json);
        RequestBody body =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        PostRequest<String> stringPostRequest = OkGo.<String>post(url).tag(ApplicationContext.getInstance().context).upRequestBody(body);
        return stringPostRequest.headers("token", getToken())
                .headers("timestamp", new Date().getTime() + "")
                .headers("timestamp", new Date().getTime() + "")
                .headers("deviceType", "ANDROID");
    }
public static PostRequest<String> post(String url, File file) {
    MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"), file);
    multipartBodybuilder.addFormDataPart("file", file.getName(), fileRQ);
    PostRequest<String> stringPostRequest = OkGo.<String>post(url).tag(ApplicationContext.getInstance().context).upRequestBody(multipartBodybuilder.build());
        return stringPostRequest.headers("token", getToken())
                .headers("timestamp", new Date().getTime() + "")
                .headers("timestamp", new Date().getTime() + "")
                .headers("deviceType", "ANDROID");
    }

    public static PostRequest<String> post(String url, RequestBody requestBody) {
        String token = CacheDBMolder.getInstance().getUserToken();
        return OkGo.<String>post(url).tag(ApplicationContext.getInstance().context).upRequestBody(requestBody)
                .headers("Authorization", "Bearer " + token);
    }

    private static String getToken(){
        String userToken = CacheDBMolder.getInstance().getUserToken();
        return userToken;
    }



  /*  public static GetRequest<String> get(String url, ProjectBaseActivity ac) {
        ac.showLoading();
        return get(url);
    }

    public static GetRequest<String> get(String url, BaseFragment fm) {
        fm.showLoading();
        return get(url);
    }


    public static PostRequest<String> post(String url, ProjectBaseActivity ac) {
        ac.showLoading();
        return post(url);
    }

    public static PostRequest<String> post(String url, BaseFragment fm) {
        fm.showLoading();
        return post(url);
    }*/
}
