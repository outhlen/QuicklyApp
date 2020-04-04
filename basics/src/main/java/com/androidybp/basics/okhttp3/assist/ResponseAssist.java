package com.androidybp.basics.okhttp3.assist;

import android.text.TextUtils;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.cache.db.model.CacheDataEntity;
import com.androidybp.basics.utils.hint.SystemCodeUtils;
import com.androidybp.basics.utils.hint.ToastUtil;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by yangbp on 2017/12/16.
 * 响应数据拦截工具类
 */

public class ResponseAssist {

    /**
     * @param mes     系统返回回来的  提示
     * @param code    code
     * @param showStr 默认显示的提示语
     */
    public static void showServiceIsSuccessFalse(String mes, int code, String showStr) {
        if (TextUtils.isEmpty(mes)) {
            String codeString = SystemCodeUtils.getCodeString(code);
            ToastUtil.showToastString(TextUtils.isEmpty(codeString) ? showStr : codeString);
        } else
            ToastUtil.showToastString(mes);
    }

    /**
     * --------------------------------------------------------------------------登录页面 解析Cookie----
     */
    /**
     * 直接从 Response中解析
     *
     * @param response
     * @return
     */
    public static boolean getCookieHeader(Response response) {
        boolean flag = false;
        String cookie = analysisCookie(response);
        flag = getCookieHeader(cookie);

        return flag;
    }

    /**
     * 给定值去保存session
     *
     * @param cookie
     * @return
     */
    public static boolean getCookieHeader(String cookie) {
        boolean flag = false;
        if (!TextUtils.isEmpty(cookie)) {
            String jsessionID = null;
            CacheDBMolder cacheDBMolder = CacheDBMolder.getInstance();
            CacheDataEntity cacheDataEntity = cacheDBMolder.getCacheDataEntity(null);
            if(cacheDataEntity != null)
                jsessionID = cacheDataEntity.getJsessionID();
            if (TextUtils.isEmpty(jsessionID)) {
                cacheDataEntity.setJsessionID(cookie);
                flag = true;
            } else if (!cookie.equals(jsessionID)) {
                cacheDataEntity.setJsessionID(cookie);
                flag = true;
            }
            //目前使用flag 标识来判断  是否需要重新更新 缓存数据
            if(flag){
                cacheDBMolder.setCacheDataEntity(cacheDataEntity, null, null);
            }
        }

        return flag;
    }

    /**
     * 单纯的 解析cookie的方法
     *
     * @param response
     * @return
     */
    public static String analysisCookie(Response response) {
        Headers headers = response.headers();
        List<Cookie> mCookie = Cookie.parseAll(response.request().url(), headers);
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = mCookie.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = mCookie.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString().trim();
    }


}
