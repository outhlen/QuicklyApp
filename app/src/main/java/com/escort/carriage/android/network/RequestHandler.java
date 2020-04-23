package com.escort.carriage.android.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.escort.carriage.android.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hjq.http.EasyLog;
import com.hjq.http.config.IRequestHandler;
import com.hjq.http.exception.DataException;
import com.hjq.http.exception.ResponseException;
import com.hjq.http.exception.ResultException;
import com.hjq.http.exception.TokenException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 请求处理类
 */
public final class RequestHandler implements IRequestHandler {

    private static final Gson GSON = new Gson();

    @Override
    public Object requestSucceed(Context context, Response response, Type type) throws Exception {
        if (!response.isSuccessful()) {
            // 返回响应异常
            throw new ResponseException(context.getString(R.string.http_server_error), response);
        }

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        if (Response.class.equals(type)) {
            return response;
        }

        if (Bitmap.class.equals(type)) {
            // 如果这是一个 Bitmap 对象
            return BitmapFactory.decodeStream(body.byteStream());
        }

        String text;
        try {
            text = body.string();
        } catch (IOException e) {
            // 返回结果读取异常
            throw new DataException(context.getString(R.string.http_data_explain_error), e);
        }

        // 打印这个 Json
        EasyLog.json(text);

        final Object result;
        if (String.class.equals(type)) {
            // 如果这是一个 String 对象
            result = text;
        } else if (JSONObject.class.equals(type)) {
            try {
                // 如果这是一个 JSONObject 对象
                result = new JSONObject(text);
            } catch (JSONException e) {
                throw new DataException(context.getString(R.string.http_data_explain_error), e);
            }
        } else if (JSONArray.class.equals(type)) {
            try {
                // 如果这是一个 JSONArray 对象
                result = new JSONArray(text);
            }catch (JSONException e) {
                throw new DataException(context.getString(R.string.http_data_explain_error), e);
            }
        } else {

            try {
                result = GSON.fromJson(text, type);
            } catch (JsonSyntaxException e) {
                // 返回结果读取异常
                throw new DataException(context.getString(R.string.http_data_explain_error), e);
            }

            if (result instanceof HttpData) {
                HttpData model = (HttpData) result;
                if (model.getCode() == 0) {
                    // 代表执行成功
                    return result;
                } else if (model.getCode() == 1001) {
                    // 代表登录失效，需要重新登录
                    throw new TokenException(context.getString(R.string.http_account_error));
                } else {
                    // 代表执行失败
                    throw new ResultException(model.getMessage(), model);
                }
            }
        }
        return result;
    }

    @Override
    public Exception requestFail(Context context, Exception e) {
        // 判断这个异常是不是自己抛的

        // 打印错误信息
        EasyLog.print(e);
        return e;
    }
}