package com.androidybp.basics.configuration;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.androidybp.basics.ApplicationContext;

/**
 * Created by yangbp
 *
 * 打包配置信息
 */

public class BaseProjectConfiguration {

    public static  boolean  requestIntercept = false;

    public static final String FILEPROVIDER = getMetaDataString("AUTHORITIES");


    // 实名认证 上传图片的 跟目录地址
    public static final String DREDGE_DEAL_CATALOGUE = "share/regFileImag/";


    // =======================================
    // ============ 外部储存路径相关配置 ==============
    // =======================================
    //一级目录 是当前项目中在外部储存的唯一基准文件夹，其他所有储存必须为当前文件夹的子文件或子文件夹下的子文件
    public static final String BASE_FOLDER = getMetaDataString("FILE_HOST");

    // =======================================
    // ============ 数据库相关 ==============
    // =======================================
    public static final int DB_VERSION = 2;//数据库版本
    public static final String DB_NAME = "project.db";//数据库名称

    //外部储存中的数据库版本号
    public static final int EXTERNAL_VERSION = 1;
    //外部储存中的数据库地址
    public static final String EXTERNAL_NAME = "external.lg";



    public static String getMetaDataString(String key) {
        String res = null;
        try {
            Context context = ApplicationContext.getInstance().getContext();
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            res = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }


    // =======================================
    // ============ 权限标识 ==============
    // =======================================

    public final static int OPEN_PROJECT_CODE = 1234;//权限不足是 打开设置页面的 请求code
}
