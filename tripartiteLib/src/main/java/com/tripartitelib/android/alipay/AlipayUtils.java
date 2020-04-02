package com.tripartitelib.android.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.tripartitelib.android.R;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Yangbp
 * @description: 支付宝支付 两种方式  1中是 原生调用  一种是H5调用
 * @date :2020-03-23 15:15
 */
public class AlipayUtils {
    private static final int SDK_PAY_FLAG = 1;

    private AliplayOpenPlayCallback callback;

    /**
     * H5 支付
     * @param activity
     * @param url
     */
    public void aliPlayH5(Activity activity, String url){
        Intent intent = new Intent(activity, H5PayActivity.class);
        Bundle extras = new Bundle();

        /*
         * URL 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
         *
         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
         * 进行测试。
         *
         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
         * 可以参考它实现自定义的 URL 拦截逻辑。
         */
        extras.putString("url", url);
        intent.putExtras(extras);
        activity.startActivity(intent);
    }

    /**
     * 支付宝支付  原生
     */
    public void aliPlay(String info, final Activity activity, AliplayOpenPlayCallback callback) {
        final String orderInfo = info;   // 订单信息
        this.callback = callback;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                String result = alipay.pay(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult(stringToMap((String) msg.obj));
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if(callback != null){
                            callback.playSeccess(true);
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        ToastUtil.showToastString("支付失败");
                        if(callback != null){
                            callback.playSeccess(false);
                        }
                    }
                    break;
                }
            }
        }
    };




    private Map<String, String> stringToMap(String obj) {
        obj = obj.replace("{", "");
        obj = obj.replace("}", "");
        String[] strs = obj.split(";");
        Map<String, String> m = new HashMap<String, String>();
        for (String s : strs) {
            String[] ms = s.split("=");
            if (ms.length > 1)
                m.put(ms[0], ms[1]);
            else if (ms.length == 1)
                m.put(ms[0], "");
        }
        return m;
    }

    public interface AliplayOpenPlayCallback{
        void playSeccess(boolean flag);
    }
}
