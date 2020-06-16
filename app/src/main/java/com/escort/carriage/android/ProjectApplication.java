package com.escort.carriage.android;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.security.biometrics.theme.ALBiometricsConfig;
import com.alibaba.security.biometrics.transition.TransitionMode;
import com.alibaba.security.cloud.CloudRealIdentityTrigger;
import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.okhttp3.OkHttpManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.thread.ThreadUtils;
import com.escort.carriage.android.config.AppConfig;
import com.escort.carriage.android.network.RequestHandler;
import com.escort.carriage.android.server.ReleaseServer;
import com.escort.carriage.android.server.TestServer;
import com.escort.carriage.android.utils.DemoHelper;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestServer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.tripartitelib.android.TripartiteLibInitUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

public class ProjectApplication extends MultiDexApplication {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ApplicationContext.getInstance().setApplicationContext(this);
        //环信初始化
//        ChatClient.Options options = new ChatClient.Options();
 //       options.setAppkey(AppConfig.HX_APPKEY);
//        options.setTenantId(AppConfig.HX_TENANTID);
//        if (!ChatClient.getInstance().init(this, options)) {
//            return;
//        }
//        UIProvider.getInstance().init(this);
//        // 设置为true后，将打印日志到logcat, 发布APP时应关闭该选项
//        ChatClient.getInstance().setDebugMode(true);

        DemoHelper.getInstance().init(this);

     //   CrashReport.initCrashReport(getApplicationContext(), "0ac3175ca9", true); //日志上报

        ThreadUtils.openSonThread(new Runnable() {
            @Override
            public void run() {
                initOkgo();
                TripartiteLibInitUtils.getUtils().registerToWX(ProjectApplication.this);
                initJPush();
             //  IflytekUtils.getIflytekUtils().initIflytek(ProjectApplication.this);
                try {
                   CloudRealIdentityTrigger.initialize(ProjectApplication.this, true, buildALBiometricsConfig());//第二个参数是本地日志能力（若打开 会记录问题到本地，方便后期排查线上用户问题）
                } catch (Exception e) {
                    LogUtils.showE("application", "阿里云人脸识别初始失败");
                }

            }
        });

        // 网络请求框架初始化
        IRequestServer server;
        if (AppConfig.isDebug()) {
            server = new TestServer();
        } else {
            server = new ReleaseServer();
        }
        EasyConfig.with(new OkHttpClient())
                // 是否打印日志
                .setLogEnabled(AppConfig.isDebug())
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler())
                // 设置请求重试次数
                .setRetryCount(3)
                // 添加全局请求参数
                //.addParam("token", "6666666")
                // 添加全局请求头
                //.addHeader("time", "20191030")
                // 启用配置
                .into();
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void addService() {

    }
    public static Context getContext() {
        return mContext;
    }
    private void initOkgo() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        //builder.addInterceptor(loggingInterceptor);//正式版不打印日志需注释此行
        //全局的读取超时时间，所有默认60
        //  builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        OkHttpClient.Builder builder = OkHttpManager.getManager().getHttpsClientBuilder();
        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        OkHttpClient okHttpClient = builder.build();
        OkHttpManager.getManager().setOkhttp(okHttpClient);
        OkGo.getInstance()
                .init(this)
                .setRetryCount(0)
                .setOkHttpClient(okHttpClient);
    }


    private void initJPush() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    /**
     * 通过ALBiometricsConfig 自定义您的UI
     *
     * @return
     */
    private ALBiometricsConfig buildALBiometricsConfig() {
        //TODO 详细参数参考UICustom
        ALBiometricsConfig.Builder alBiometricsConfig = new ALBiometricsConfig.Builder();
        alBiometricsConfig.setNeedSound(false);//默认是否开启声音
        alBiometricsConfig.transitionMode = TransitionMode.BOTTOM;//从下弹出
        return alBiometricsConfig.build();
    }


    public String TAG = "application";

    public void hookWebView() {
        int sdkInt = Build.VERSION.SDK_INT;
        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get(null);
            if (sProviderInstance != null) {
                Log.i(TAG, "sProviderInstance isn't null");
                return;
            }

            Method getProviderClassMethod;
            if (sdkInt > 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
            } else if (sdkInt == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
            } else {
                Log.i(TAG, "Don't need to Hook WebView");
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> factoryProviderClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> delegateConstructor = delegateClass.getDeclaredConstructor();
            delegateConstructor.setAccessible(true);
            if (sdkInt < 26) {//低于Android O版本
                Constructor<?> providerConstructor = factoryProviderClass.getConstructor(delegateClass);
                if (providerConstructor != null) {
                    providerConstructor.setAccessible(true);
                    sProviderInstance = providerConstructor.newInstance(delegateConstructor.newInstance());
                }
            } else {
                Field chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD");
                chromiumMethodName.setAccessible(true);
                String chromiumMethodNameStr = (String) chromiumMethodName.get(null);
                if (chromiumMethodNameStr == null) {
                    chromiumMethodNameStr = "create";
                }
                Method staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, delegateClass);
                if (staticFactory != null) {
                    sProviderInstance = staticFactory.invoke(null, delegateConstructor.newInstance());
                }
            }
            if (sProviderInstance != null) {
                field.set("sProviderInstance", sProviderInstance);
                Log.i(TAG, "Hook success!");
            } else {
                Log.i(TAG, "Hook failed!");
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        }
    }

}
