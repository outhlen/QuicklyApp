package com.androidybp.basics.cache;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.androidybp.basics.ApplicationContext;


/**
 * Resources资源转换工具类    dp和px转换
 */
public class ResourcesTransformUtil {

    /**
     * 获取Resource对象
     *
     * @return
     */
    public static Resources getResources() {
        return ApplicationContext.getInstance().getContext().getResources();
    }

    /**
     * 获取drawable资源
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取字符串资源
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }
    /**
     * 获取字符串资源
     *
     * @param resId
     * @return
     */
    public static String getStrings(int... resId) {
        if(resId.length == 0){
            return "";
        }else if(resId.length == 1){
            return getString(resId[0]);
        }else{
            StringBuilder sb = new StringBuilder();
            for(int str : resId){
                sb.append(getString(str));
            }
            return sb.toString();
        }
    }

    /**
     * 获取color资源
     *
     * @param resId
     * @return
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取dimens资源,就是dp值
     *
     * @param resId
     * @return
     */
    public static float getDimens(int resId) {
        return getResources().getDimension(resId);
    }
    /**
     * 获取字符串数组资源
     * @param resId
     * @return
     */
    public static String[] getStringArray(int resId){
        return getResources().getStringArray(resId);
    }
    /**
     * 将指定的child从它 的父View中移除
     * @param child
     */
    public static void removeSelfFromParent(View child){
        if(child!=null){
            ViewParent parent = child.getParent();
            if(parent instanceof ViewGroup){
                ViewGroup group = (ViewGroup) parent;
                group.removeView(child);//将子View从父View中移除
            }
        }
    }
    /**********************************************************************************************
     *                                         dp和px转换
    **********************************************************************************************/
    /**
     *  dp to px
     * @param dpVal
     * @return
     */
    public static int dp2px( float dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, ResourcesTransformUtil.getResources().getDisplayMetrics());
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
//        float scale = context.getResources().getDisplayMetrics().density;
        float scale = getDensity();
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
//        float scale = context.getResources().getDisplayMetrics().density;
        float scale = getDensity();
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
//        float scale = context.getResources().getDisplayMetrics().density;
        float scale = getDensity();
        return (int) (pxValue / scale + 0.5f);
    }

    public static float getDensity(){
        return getResources().getDisplayMetrics().density;
    }
}
