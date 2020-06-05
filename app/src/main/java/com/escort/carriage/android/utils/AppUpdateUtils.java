package com.escort.carriage.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.androidybp.basics.utils.hint.ToastUtil;

import java.io.File;

import cn.jpush.android.helper.Logger;

public class AppUpdateUtils {

    private static final String DATA_AND_TYPE = "application/vnd.android.package-archive";
    private static String mAuthoritiesPrefix;
    /**
     * 安装软件
     *
     * @param context - 文件
     * @param apkPath - 路径path
     */
    public static void installApk(Context context, String apkPath) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String authoritiesPrefix = getAuthoritiesPrefix();
            if (TextUtils.isEmpty(authoritiesPrefix)) {
                authoritiesPrefix = context.getPackageName();
            }
            String fileProviderAuthority = authoritiesPrefix ;
            File file = new File(apkPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //大于7.0 判断是否有安装权限
                Uri apkUri = FileProvider.getUriForFile(context, fileProviderAuthority, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, DATA_AND_TYPE);
            } else {
                Uri apkUri = Uri.fromFile(file);
                intent.setDataAndType(apkUri, DATA_AND_TYPE);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.d("错误: ",  e.getMessage());
            e.printStackTrace();
            ToastUtil.showToastString("安装失败");
        }
    }


    public static String getAuthoritiesPrefix() {
        return mAuthoritiesPrefix;
    }


    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }
    /**
     * 获取App版本号
     */
    private static String getAppVersionName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return "";
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
