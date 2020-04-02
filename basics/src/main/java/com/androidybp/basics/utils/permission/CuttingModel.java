package com.androidybp.basics.utils.permission;

import android.Manifest;

/**
 * 获取手机权限的类
 */
public class CuttingModel {
    /**
     * @return 获取设备唯一标识的权限数组
     */
    public String[] getIdentificationCode() {
        return new String[]{Manifest.permission.READ_PHONE_STATE};
    }

    /**
     * @return 获取拨打电话的权限数组
     */
    public String[] getCallPhoneCuttion() {
        return new String[]{Manifest.permission.CALL_PHONE};
    }

    /**
     * @return 获取读取通讯录的权限数组
     */
    public String[] getReadCalendarCuttion() {
        return new String[]{Manifest.permission.READ_CONTACTS};
    }

    /**
     * @return 开机需要权限
     */
    public String[] getSplashCutting() {
        return new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
    }
/**
     * @return 相机和读写内存卡的权限的数组
     */
    public String[] getCameraAndMemoryCutting() {
        return new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    /**
     * @return 相机权限的数组
     */
    public String[] getCameraCutting() {
        return new String[]{Manifest.permission.CAMERA};
    }

    /**
     * @return 读写内存卡的权限的数组
     */
    public String[] getMemoryCutting() {
        return new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    /**
     * 友盟分享使用到的  权限
     * <p>
     * 因 已全部授权 但以下权限还是返回未授权
     * 删除权限 Manifest.permission.READ_LOGS   Manifest.permission.SYSTEM_ALERT_WINDOW,
     * Manifest.permission.SET_DEBUG_APP  Manifest.permission.WRITE_APN_SETTINGS
     *
     * @return
     */
    public String[] getUMCutting() {
        //第一项为 Manifest.permission.WRITE_EXTERNAL_STORAGE,已删除重复
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.GET_ACCOUNTS };

       //以下权限为友盟的配置新加的配置  如果目前分享可以  以下权限自动忽略
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.WRITE_APN_SETTINGS


    }
}
