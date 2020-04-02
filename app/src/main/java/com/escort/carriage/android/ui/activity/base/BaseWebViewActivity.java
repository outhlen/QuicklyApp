package com.escort.carriage.android.ui.activity.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.escort.carriage.android.ui.view.ProgressView;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;


public abstract class BaseWebViewActivity extends ProjectBaseActivity {

    protected ProgressView mProgressView;
    protected WebView mWebView;

    private int REQUEST_CODE = 123;

    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private OnShouldOverrideUrlLoading mOnShouldOverrideUrlLoading;
    private OnLoadFinishCallback mOnLoadFinishCallback;
    /**
     * 定位权限
     */
    private String[] LOCATION_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    abstract public int getLayoutResId();

    abstract public void initData(@Nullable Bundle savedInstanceState);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getLayoutResId() != 0) {
            setContentView(getLayoutResId());
            initData(savedInstanceState);
        }
    }

    /**
     * 加载url
     */
    protected void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 加载网页片段
     */
    protected void loadHtmlData(String htmlData) {
        mWebView.loadData(htmlData, "text/html; charset=UTF-8", null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void initWebView(WebView webView, ProgressView progressView) {
        mWebView = webView;
        initProgressView(progressView);
        WebSettings mSettings = webView.getSettings();
        //启用地理定位，默认为true
        mSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        mSettings.setGeolocationDatabasePath(dir);
        // 支持获取手势焦点
        webView.requestFocusFromTouch();
        webView.setHorizontalFadingEdgeEnabled(true);
        webView.setVerticalFadingEdgeEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        // 支持JS
        mSettings.setJavaScriptEnabled(true);
        mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mSettings.setBuiltInZoomControls(true);
        mSettings.setDisplayZoomControls(true);
        mSettings.setLoadWithOverviewMode(true);
        // 支持插件
        mSettings.setPluginState(WebSettings.PluginState.ON);
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 自适应屏幕
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
        // 支持缩放
        mSettings.setSupportZoom(false);//就是这个属性把我搞惨了，
        // 隐藏原声缩放控件
        mSettings.setDisplayZoomControls(false);
        // 支持内容重新布局
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING/*.SINGLE_COLUMN*/);
        mSettings.supportMultipleWindows();
        mSettings.setSupportMultipleWindows(true);
        // 设置缓存模式
        mSettings.setDomStorageEnabled(true);
        mSettings.setDatabaseEnabled(true);
        mSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mSettings.setAppCacheEnabled(true);
        mSettings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        // 设置可访问文件
        mSettings.setAllowFileAccess(true);
        mSettings.setNeedInitialFocus(true);
        mSettings.setAllowFileAccessFromFileURLs(true);
        mSettings.setAllowUniversalAccessFromFileURLs(true);
        // 支持自定加载图片
        mSettings.setLoadsImagesAutomatically(true);
        mSettings.setNeedInitialFocus(true);
        // 设定编码格式
        mSettings.setDefaultTextEncodingName("UTF-8");


        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDefaultTextEncodingName("utf-8");

//        webSetting.setAllowContentAccess(true);
//        webSetting.setAllowFileAccess(true);
//        webSetting.setAllowFileAccessFromFileURLs(true);
//        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                //                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.showD("YCTest", url);
                if (mOnShouldOverrideUrlLoading != null){
                    return mOnShouldOverrideUrlLoading.onShouldOverrideUrlLoading(view, url);
                }else {
                    WebView.HitTestResult hit = view.getHitTestResult();
                    //hit.getExtra()为null或者hit.getType() == 0都表示即将加载的URL会发生重定向，需要做拦截处理
                    if (TextUtils.isEmpty(hit.getExtra()) || hit.getType() == 0) {
                        //通过判断开头协议就可解决大部分重定向问题了，有另外的需求可以在此判断下操作
                        LogUtils.showE("重定向", "重定向: " + hit.getType() + " && EXTRA（）" + hit.getExtra() +
                                "------");
                        LogUtils.showE("重定向",
                                "GetURL: " + view.getUrl() + "\n" +"getOriginalUrl()"+ view.getOriginalUrl());
                        LogUtils.showD("重定向", "URL: " + url);
                        return true;
                    }

                    // 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                    if (url.startsWith("tel")) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        } catch (Exception e) {
//                            LogUtils.showE(TAG, e.getMessage());
                        }
                        return true;
                    }
                    view.loadUrl(url); // 在本WebView打开新的url请求
                    return true;  // 标记新请求已经被处理笑话
                    // 上边2行合起来，标识所有新链接都在本页面处理，不跳转别的浏览器
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mOnLoadFinishCallback != null)
                    mOnLoadFinishCallback.onLoadFinishCallback(true);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (mOnLoadFinishCallback != null)
                    mOnLoadFinishCallback.onLoadFinishCallback(false);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             * 定位
             */
            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                try {
//                    showToast("走到了请求定位");
                    LogUtils.showE("YCTest", "webView request location permission origin is " + origin);
                } catch (Exception ignored) {
                }
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
            /**
             * API >= 21(Android 5.0.1)回调此方法
             */
            @Override
            public boolean onShowFileChooser(WebView mWebView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                LogUtils.showE("WangJ", "运行方法 onShowFileChooser");
                // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
                mUploadCallbackAboveL = filePathCallback;
                takePhoto();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 加载进度条
                if (newProgress == 100) {
                    //加载完毕进度条消失
                    if (mProgressView != null)
                    mProgressView.setVisibility(View.GONE);
                } else {
                    //更新进度
                    if (mProgressView != null){
                        mProgressView.setProgress(newProgress);
                        mProgressView.setVisibility(View.INVISIBLE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        // 监听WebView下载
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimeType, long contentLength) {
                try {
                    // H5中包含下载链接的话让外部浏览器去处理
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception ignored) {
                }
            }
        });
//        mWebView.addJavascriptInterface(new JSCallBack(), "android");
    }

    protected void initProgressView(ProgressView progressView){
        mProgressView = progressView;
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath =
                Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CODE);


        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        LogUtils.showE("WangJ", "返回调用方法--chooseBelow");

        if (AppCompatActivity.RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    LogUtils.showE("WangJ", "系统返回URI：" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                LogUtils.showE("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
            if (mUploadCallbackBelow != null) {
                chooseBelow(resultCode, data);
            } else if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            } else {
                Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        LogUtils.showE("WangJ", "返回调用方法--chooseAbove");

        if (AppCompatActivity.RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        LogUtils.showE("WangJ", "系统返回URI：" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                LogUtils.showE("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
//        sendBroadcast(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView
        // .canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            String params = "goBack";
            String js = "javascript:goBack('" + params + "')";
            mWebView.loadUrl(js);
            return true;
        }
        LogUtils.showD("YCTest", "onKeyDown");
        return super.onKeyDown(keyCode, event);
    }

    public void setOnLoadFinishCallback(OnLoadFinishCallback callback) {
        mOnLoadFinishCallback = callback;
    }

    /**
     * 加载完成的回调
     */
    protected interface OnLoadFinishCallback {
        void onLoadFinishCallback(boolean success);
    }


    public void  setOnShouldOverrideUrlLoading( OnShouldOverrideUrlLoading callback) {
        mOnShouldOverrideUrlLoading = callback;
    }

    interface OnShouldOverrideUrlLoading {
        boolean onShouldOverrideUrlLoading(WebView view, String url);
    }
}
