package com.androidybp.basics.utils.hint;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;

/**
 * 根据系统返回的code对应的提示语
 */
public class SystemCodeUtils {

    // -------------------  本地部分  -----------------------

    //网络请求部分
    public static final int INITPAGE = 1001;//初始化页面
    public static final int REFRESH_DATA = 1002;//刷新数据
    public static final int LOAD_DATA = 1003;//分页加载

    //本地权限申请部分
    public static final int OPEN_CAMERA_PERMISSION = 2001;//申请相机权限
    public static final int READ_WRITE_MEMORY_CARDS = 2002;//申请读写内存卡权限
    public static final int MOBILE_IDENTIFICATION = 2003;//申请获取手机标识的权限
    public static final int CALL_PHONE = 2004;//申请获取手机标识的权限
    public static final int READ_CONTACTS = 2005;//申请获取手机通讯录的权限



    // --------------------  网络部分  ----------------------

    private static final String baseCodeName = "SYSTEM_CODE_";

    public static final int SYSTEM_100003 = 100003;//登录超时
    public static final int SYSTEM_100005 = 100005;//单点登录

    public static String getCodeString(int code) {

        String codeName = baseCodeName + code;
        int stringId = ResourcesTransformUtil.getResources().getIdentifier(codeName, "string", ApplicationContext.getInstance().getContext().getPackageName());
        return getString(stringId);
    }

    private static String getString(int name) {
        return ResourcesTransformUtil.getString(name);
    }

}
