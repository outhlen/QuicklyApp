package com.androidybp.basics.utils.equipment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.androidybp.basics.R;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.Fileprovider;

import java.io.File;
import java.util.List;

/**
 * Created by yangbp on 2017/12/18.
 *
 * 用于打开外部应用
 */

public class OpenExternalEquipent {

    /**
     * 打开APK 安装页面
     *
     * @param mActivity 当前Activity对象
     * @param path      下载文件的具体路径
     */
    public void openApk(Activity mActivity, String path) {
        try {
            File file = new File(path);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";
            Fileprovider fileprovider = new Fileprovider();
            Uri uri = fileprovider.getUri(mActivity, file, intent);
            intent.setDataAndType(uri, type);
            mActivity.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * @param context        上下文对象
     * @param packageStr     包名
     * @param openActivity   打开Activity类名
     * @param openUrl        需要打开的URL
     */
    public void openPackageAPP(Context context, String packageStr, String openActivity, String openUrl){
        if(TextUtils.isEmpty(packageStr)){
            openUrl(context, openUrl);
        }else{
            //先判断当前应用是否已经按装了
            if(checkApkExist(context, packageStr)){
                try {
                    if(TextUtils.isEmpty(openActivity)){
                        doStartApplicationWithPackageName(context, packageStr, openUrl);
                    }else{
                        Context packageContext = getPackageContext(context, packageStr);
                        openAPP(context, packageStr, openActivity);
                    }
                }catch (Exception e){
                    if(TextUtils.isEmpty(openUrl)){
                        ToastUtil.showToastResources(R.string.no_app_manual_operation);
                    }else{
                        openUrl(context, openUrl);
                    }
                }
            }else{
                if(TextUtils.isEmpty(openUrl)){
                    ToastUtil.showToastResources(R.string.no_app_manual_operation);
                }else{
                    openUrl(context, openUrl);
                }
            }
        }
    }


    public static Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    /**
     * 通过外部浏览器 打开 网页
     * @param context    上下文对象
     * @param url        URl链接
     */
    public void openUrl(Context context, String url){
        if(context != null && !TextUtils.isEmpty(url)){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        }
    }

    /**
     * 通过包名和类名打开指定的APP
     * @param context        上下文对象
     * @param packageName    包名
     * @param className      类名
     */
    public void openAPP(Context context, String packageName, String className){
        if (context != null){
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED| Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }catch (Exception e){
                doStartApplicationWithPackageName(context, packageName, null);
            }
        }



//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        // 设置ComponentName参数1:packagename参数2:MainActivity路径
//        ComponentName cn = new ComponentName(packageName, className);
//
//        intent.setComponent(cn);
//        context.startActivity(intent);
    }

    /**
     * 判断给定包名  判断程序是否存在
     * @param context        上下午对象
     * @param packageName    包名
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void doStartApplicationWithPackageName(Context context, String packagename, String url) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED| Intent.FLAG_ACTIVITY_NEW_TASK);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }else {
            openUrl(context, url);
        }
    }



}
