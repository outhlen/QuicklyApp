package com.escort.carriage.android.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.escort.carriage.android.R;

import androidx.annotation.Nullable;

public class AppWebActivity extends Activity {
    private int ErrorCode = 0;
    public String url;

    public String roomId;//页面数据id

    private WebView webView;
    private ProgressBar myProgressBar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarCompatManager.compat(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_web);
        webView = findViewById(R.id.act_browser_web);
        myProgressBar = findViewById(R.id.act_browser_progressBar);
        setDatas();
        initWidget();
    }

    protected void setDatas() {
        url = getIntent().getStringExtra("url");
        roomId = getIntent().getStringExtra("roomId");
//        url = "http://www.baidu.com";
    }


    private void initWidget() {

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    myProgressBar.setVisibility(View.GONE);
                } else {
                    if (myProgressBar.getVisibility() == View.GONE)
                        myProgressBar.setVisibility(View.VISIBLE);
                    myProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                LogUtils.showI("onReceivedError", "===========" + "onReceivedError(WebView view,");
                ErrorCode = errorCode;
                if (errorCode == -2) {
                    //错误处理
                    try {
                        webView.stopLoading();
                    } catch (Exception e) {
                    }
                    try {
                        webView.clearView();
                    } catch (Exception e) {
                    }
                    webView.setVisibility(View.GONE);
                }
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ErrorCode = 0;
                webView.setVisibility(View.VISIBLE);
//                LogUtils.showI("onReceivedError", "===========" + "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (ErrorCode == 0) {
                    webView.setVisibility(View.VISIBLE);
                }
//                    LogUtils.showI("onReceivedError", "===========" + "onPageFinished");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

        });
        WebSettings settings = webView.getSettings();//辅助类WebSettings，控制、设置webview
        settings.setJavaScriptEnabled(true); // 启用javascript的脚本功能
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(false); // 屏幕上显示放大和缩小按钮
        settings.setUseWideViewPort(true);// 设置双击可以放大或者缩小
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("utf-8");
        webView.addJavascriptInterface(new JS2AndroidUtil(this), "jsToApp");
        //加载显示网页的操作
        webView.loadUrl(url);//通过url地址打开网页
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return false;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}