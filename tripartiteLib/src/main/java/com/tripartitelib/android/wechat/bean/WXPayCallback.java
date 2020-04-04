package com.tripartitelib.android.wechat.bean;

/**
 * 微信支付回调  使用类
 */

public class WXPayCallback {
    public String responseClassName;
    public boolean success;
    public int type;
    public Object obj;
    public String showText;
}
