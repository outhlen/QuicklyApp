package com.androidybp.basics.utils.hint;

import android.util.Log;

import com.androidybp.basics.BuildConfig;


/**
 *打印日志工具类
 */
public class LogUtils {
    public static boolean isShow = BuildConfig.LOG_DEBUG;

    /**
     * @param className TAG参数
     * @param value 要打印的文本
     */
    public static void showE(String className, String value) {
        if (isShow)
            Log.e(className, value);
    }
    /**
     * @param className TAG参数
     * @param value 要打印的文本
     */
    public static void showI(String className, String value) {
        if (isShow)
            Log.i(className, value);
    }
    /**
     * @param className TAG参数
     * @param value 要打印的文本
     */
    public static void showD(String className, String value) {
        if (isShow)
            Log.d(className, value);
    }
    /**
     * @param className TAG参数
     * @param value 要打印的文本
     */
    public static void showV(String className, String value) {
        if (isShow)
            Log.v(className, value);
    }
    /**
     * @param className TAG参数
     * @param value 要打印的文本
     */
    public static void showW(String className, String value) {
        if (isShow)
            Log.w(className, value);
    }


}
