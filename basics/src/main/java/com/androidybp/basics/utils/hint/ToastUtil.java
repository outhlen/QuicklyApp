package com.androidybp.basics.utils.hint;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.R;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.view.toast.CustomToastUtils;
import com.androidybp.basics.ui.view.toast.ProjectAndroidToastUtils;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;


/**
 * 用户提示的工具类
 */
public class ToastUtil {

    private static ProjectAndroidToastUtils projectAndroidToastUtils;


    public static void init(){
        if(ApplicationContext.getInstance().isNotifications){
            //初始化原生toast组件
            projectAndroidToastUtils = new ProjectAndroidToastUtils();
        } else {
            //初始化自定义toast组件
            CustomToastUtils.init(ApplicationContext.getInstance().application);
        }
    }

    /****************************************************************************************************
     原生Toast模块
     ****************************************************************************************************/

    /**
     * 显示吐司  可以在子线程中调用
     *
     * @param message 吐司要显示的内容
     */
    public static void showToastString(String message) {
        if (TextUtils.isEmpty(message))
            return;
        if(ApplicationContext.getInstance().isNotifications){
            projectAndroidToastUtils.showToastString(message);
        } else {
            CustomToastUtils.show(message);
        }
    }

    /**
     * 显示吐司   可以在子线程中调用
     *
     * @param message 要显示的资源文件id
     */
    public static void showToastResources(int message) {
        String str = ResourcesTransformUtil.getString(message);
        showToastString(str);
    }

    /**
     * 显示吐司
     *
     * @param message 吐司要显示的内容
     */
    public static void showToastInt(int message) {
        String str = String.valueOf(message);
        showToastString(str);
    }

    /**
     * 显示吐司
     *
     * @param message 吐司要显示的内容
     */
    public static void showToastBoolean(boolean message) {
        String str = String.valueOf(message);
        showToastString(str);
    }

    /**
     * 用于 打开 权限授权页面的方法
     * @param activity
     */
    public void openSetting(Activity activity){
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, activity.getApplicationInfo().uid);

            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", activity.getPackageName());
            intent.putExtra("app_uid", activity.getApplicationInfo().uid);

            // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();

            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }

}
