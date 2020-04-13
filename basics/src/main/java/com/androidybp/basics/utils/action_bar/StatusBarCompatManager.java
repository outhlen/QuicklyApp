package com.androidybp.basics.utils.action_bar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.androidybp.basics.R;
import com.androidybp.basics.utils.equipment.ScreenUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;


/**
 * 设置标题颜色的管理工具类
 *
 * 如果只是设置一下状态栏的颜色  支持到19
 * MD效果 从21（5.0）开始
 *
 */
public class StatusBarCompatManager {
    private static final int INVALID_VAL = ResourcesTransformUtil.getColor(R.color.actionbar_color);
    private static final int COLOR_DEFAULT = Color.parseColor("#0F97FD");

    /**
     * 给状态栏上颜色
     * @param activity      activity对象
     * @param statusColor   要设置的颜色值
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != -1) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            int color = COLOR_DEFAULT;
//            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
//            if (statusColor != INVALID_VAL) {
//                color = statusColor;
//            }
//            View statusBarView = new View(activity);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ScreenUtil.getStatusHeight(activity));
//            statusBarView.setBackgroundColor(color);
//            contentView.addView(statusBarView, lp);
//
//        }
    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }

    /**
     * Android 6.0 以上设置状态栏颜色
     * Color.parseColor("#FFFFFF")
     * ResourcesTransformUtil.getColor(R.color.actionbar_color)
     *
     */
    public static void setStatusBar(@ColorInt int color, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 设置状态栏底色颜色
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 判断颜色是不是亮色
     */
    private static boolean isLightColor(@ColorInt int color){
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    /**
     * 显示状态栏
     * @param act activity对象
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showTitleStatus(Activity act){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = act.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_VISIBLE;
            if(decorView.getSystemUiVisibility() == View.SYSTEM_UI_FLAG_VISIBLE){
                return;
            }
            decorView.setSystemUiVisibility(option);
            ViewGroup rootView = ((ViewGroup) act.findViewById(android.R.id.content));
            int height = rootView.getHeight();
            if(height >= ScreenUtil.getNoHasVirtualKey(act)){
                ViewGroup.LayoutParams linearParams =rootView.getLayoutParams();
                linearParams.height = height - ScreenUtil.getStatusHeight(act);
                rootView.setLayoutParams(linearParams);
            }


        }
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}
