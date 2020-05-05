package com.escort.carriage.android.http;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;

import okhttp3.Response;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-16 11:00
 */

public abstract class MyStringCallback<T extends ResponceJsonEntity> extends StringCallback {
    private StringConvert convert;
    public MyStringCallback() {
        convert = new StringConvert();
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        String s1 = response.request().url().toString();
        LogUtils.showE("响应的Json", "url = " + s1 + "      json = " + s);
        return s;
    }

    public abstract void onResponse(T s);

    public abstract Class<T> getClazz();

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
        try {
            String body = response.body();
            T jsonBean = (T) JsonManager.getJsonBean(body, getClazz());
            final int interceptor = AppResponseInterceptor.interceptor(jsonBean);
            if (interceptor == -1) {
                onResponse(jsonBean);
            }else {

                ToastUtil.showToastString("账号在其他手机登录，请重新登录");
            }

        }catch (Exception e){
            UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
//            ToastUtil.showToastString("数据解析异常");
        }

    }

    @Override
    public void onError(com.lzy.okgo.model.Response<String> response) {
        super.onError(response);
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
        ToastUtil.showToastString("数据获取失败");
    }
}
