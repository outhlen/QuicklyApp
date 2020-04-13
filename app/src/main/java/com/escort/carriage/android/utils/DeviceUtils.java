/*
 * Copyright (c) 2017.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.escort.carriage.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * @author cisco_luo
 */

public final class DeviceUtils {
    //
    private DeviceUtils() {
    }

    /**
     * get screen's width and height
     */
    public static ScreenWH getScreenWH(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        ScreenWH swh = new ScreenWH(dm.widthPixels, dm.heightPixels);
        return swh;
    }

    public static int getScreenWidth(Activity activity) {
        return getScreenWH(activity).width;
    }

    public static int getScreenHeight(Activity activity) {
        return getScreenWH(activity).height;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 反射手机运行的类：android.R.dimen.status_bar_height.
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String heightStr = clazz.getField("status_bar_height").get(object).toString();
            int height = Integer.parseInt(heightStr);
            //dp--->px
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取手机的品牌
     */
    public static String getPhoneBrand() {
        String brand = Build.BRAND;
        if (brand == null) {
            brand = "";
        }
        return brand;
    }

    /**
     * 获取手机的型号
     */
    public static String getPhoneModel() {
        String model = Build.MODEL;
        if (model == null) {
            model = "";
        }
        return model;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取IMEI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return IMEI码
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) return "";
            return telephonyManager.getDeviceId();
        }catch (Exception e){
            return "";
        }
    }


    public static class ScreenWH {
        public int width;
        public int height;

        ScreenWH(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

}
