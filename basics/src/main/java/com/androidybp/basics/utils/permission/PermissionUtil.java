package com.androidybp.basics.utils.permission;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidybp.basics.configuration.BaseProjectConfiguration;
import com.androidybp.basics.ui.manager.activity.ProjectActivityManager;
import com.androidybp.basics.utils.hint.SystemCodeUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    private int frag = 1;
    private int acty = 2;
    public CuttingModel mCuttingModel;
    public final String PACKAGE_URL_SCHEME = "package:";//权限方案


    /**
     * 当前手机在6.0上   判断是否获取到了某个权限    6.0以下系统默认获取到当前权限
     *
     * @param permission 要查询的权限
     * @param context    上下文对象
     * @return 是否获取到了权限  true 是有    false 是没有
     */
    public boolean hasPermission(Context context, String permission) {

        if (canMakeSmores()) {
            return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;

    }


    /**
     * 弹出对话框请求权限
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, requestCode);
        }
    }

    /**
     * 返回缺失的权限
     * @param context
     * @param permissions
     * @return 返回缺少的权限，null 意味着没有缺少权限
     */
    public static String[] getDeniedPermissions(Context context, String[] permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            for(String permission : permissions){
                if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                    deniedPermissionList.add(permission);
                }
            }
            int size = deniedPermissionList.size();
            if(size > 0){
                return deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
            }
        }
        return null;
    }





    /**
     * 批量判断给定的权限是否都已经开通  如果有未开通的  放回false   都开通或者是在6.0系统以下的版本上返回true
     *
     * @param context    当前上下文
     * @param permission 权限数组
     * @return 当前是否获取到了权限   6.0以下默认返回true
     */
    public boolean hasPermission(Context context, String[] permission) {
        if (context == null || permission == null || permission.length < 1)
            return false;
        if (canMakeSmores()) {
            for (String str : permission) {
                boolean b = ContextCompat.checkSelfPermission(context, str) == PackageManager.PERMISSION_GRANTED;
                if (!b)
                    return b;
            }
        }
        return true;
    }

    /**
     * 批量判断给定的权限是否都已经开通  如果有未开通的  放回false   都开通或者是在6.0系统以下的版本上返回true
     *
     * @param context    当前上下文
     * @param permission 权限数组
     * @return 当前是否获取到了权限   不去考虑是否是6.0以上的手机
     */
    public boolean hasPermissioNoVersions(Context context, String[] permission) {
        if (context == null || permission == null || permission.length < 1)
            return false;

        for (String str : permission) {
            boolean b = ContextCompat.checkSelfPermission(context, str) == PackageManager.PERMISSION_GRANTED;
            if (!b)
                return b;
        }
        return true;
    }



    /**
     * 获得没有授权的权限
     * author LH
     * data 2016/7/27 11:46
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public List<String> findDeniedPermissions(Activity activity, List<String> permissions) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permissions) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    /**
     * 获取所有权限  这个方法 自带 过滤 已经获取到的权限
     *
     * @param activity       activity对象
     * @param permissionsArr 要申请权限的List集合
     * @param requestCode    请求code
     * @return true --  说明去打开了申请权限页面      false -- 已经获取到全部权限
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public boolean requestPermissions(Activity activity, List<String> permissionsArr, int requestCode) {
        if (permissionsArr == null || permissionsArr.size() < 1)
            return false;
        if (!canMakeSmores()) {
            return false;
        }
        List<String> mListPermissions = findDeniedPermissions(activity, permissionsArr);
        if (mListPermissions != null && mListPermissions.size() > 0) {
            activity.requestPermissions(mListPermissions.toArray(new String[mListPermissions.size()]),
                    requestCode);
            return true;
        }
        return false;

    }
    /**
     * 获取所有权限  这个方法 自带 过滤 已经获取到的权限
     *
     * @param fragment       activity对象
     * @param permissionsArr 要申请权限的List集合
     * @param requestCode    请求code
     * @return true --  说明去打开了申请权限页面      false -- 已经获取到全部权限
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public boolean requestPermissions(Fragment fragment, List<String> permissionsArr, int requestCode) {
        if (permissionsArr == null || permissionsArr.size() < 1)
            return false;
        if (!canMakeSmores()) {
            return false;
        }
        List<String> mListPermissions = findDeniedPermissions(fragment.getActivity(), permissionsArr);
        if (mListPermissions != null && mListPermissions.size() > 0) {
            fragment.requestPermissions(mListPermissions.toArray(new String[mListPermissions.size()]),
                    requestCode);
            return true;
        }
        return false;

    }

    /**
     * 在Activity中响应  获取权限回调
     * <p>
     * 重写onRequestPermissionsResult（）方法
     *
     * @param act         Activity对象
     * @param strs        要申请的权限
     * @param requestCode 请求的code
     */
    @SuppressLint("NewApi")
    public void openIdentificationAct(Activity act, String[] strs, int requestCode) {
        if (act != null && strs != null && strs.length > 0 && canMakeSmores()) {
            act.requestPermissions(strs, requestCode);
        }
    }

    /**
     * 在Fragment中响应  获取权限回调
     * <p>
     * 重写onRequestPermissionsResult（）方法
     *
     * @param mFragment   V4包下的Fragment对象
     * @param strs        要申请的权限
     * @param requestCode 请求的code
     */
    @SuppressLint("NewApi")
    public void openIdentificationV4Frag(Fragment mFragment, String[] strs, int requestCode) {
        if (mFragment != null && strs != null && strs.length > 0 && canMakeSmores()) {
            mFragment.requestPermissions(strs, requestCode);
        }
    }


    /**
     * 调用打开获取手机唯一标识的权限
     * <p>
     * 调用这个方法的时候，返回false时，要么参数不对要么用户已经禁止到了当前的权限。 返回true时，成功拉起了请求权限页面，要在当前的Activity
     * 或者是Fragment重写onRequestPermissionsResult（）方法，请求带的请求码是 SystemCodeUtils.MOBILE_IDENTIFICATION。
     *
     * @param obj Activity  或者是 Fragment
     * @return 是否成功的调用起了 申请权限页面
     */
    public boolean openIdentificationCode(Object obj) {
        if (obj == null)
            return false;
        Activity act = null;
        Fragment fra = null;
        switch (judgeActivityAndFragment(obj)) {
            case 1:
                fra = (Fragment) obj;
                act = fra.getActivity();
                break;
            case 2:
                act = (Activity) obj;
                break;
        }
        if (act == null && fra == null)
            return false;
        //ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS); 这个方法 在一开始打开APP时 为false  如果用户只是单纯拒绝  返回为true
        //如果用户 选择中了 永远记住选择的时候  拒绝   返回为false   这个方法不安全 暂时不用
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.READ_PHONE_STATE)) {
//            //如果感觉提示用户体验不是太好   可以将下面这句话注释掉
////            ToastUtil.showToastResources(R.string.you_for_obtaining_the_identity_permissions_is_banned);
//            return false;
//        }
        if (fra != null)
            openIdentificationV4Frag(fra, mCuttingModel.getIdentificationCode(), SystemCodeUtils.MOBILE_IDENTIFICATION);
        else
            openIdentificationAct(act, mCuttingModel.getIdentificationCode(), SystemCodeUtils.MOBILE_IDENTIFICATION);
        return true;
    }

    /**
     * 调用打开相机请求权限
     * <p>
     * 调用这个方法的时候，返回false时，要么参数不对要么用户已经禁止到了当前的权限。 返回true时，成功拉起了请求权限页面，要在当前的Activity
     * 或者是Fragment重写onRequestPermissionsResult（）方法，请求带的请求码是 SystemCodeUtils.OPEN_CAMERA_PERMISSION。 默认开通三个权限 相机、读写
     * 外部储存空间的权限
     *
     * @param obj Activity  或者是 Fragment
     * @return 是否成功的调用起了 申请权限页面
     */

    public boolean applyOpenCamera(Object obj) {
        if (obj == null)
            return false;
        Activity act = null;
        Fragment fra = null;
        switch (judgeActivityAndFragment(obj)) {
            case 1:
                fra = (Fragment) obj;
                act = fra.getActivity();
                break;
            case 2:
                act = (Activity) obj;
                break;
        }
        if (act == null && fra == null)
            return false;
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.CAMERA)) {
//                ToastUtil.showToastResources(R.string.the_failure_of_opening_the_camera_no_permission);
//                return false;
//            }
        if (fra != null)
            openIdentificationV4Frag(fra, mCuttingModel.getCameraAndMemoryCutting(), SystemCodeUtils.OPEN_CAMERA_PERMISSION);
        else
            openIdentificationAct(act, mCuttingModel.getCameraAndMemoryCutting(), SystemCodeUtils.OPEN_CAMERA_PERMISSION);
        return true;
    }

    /**
     * 去拉起申请读写外部储存空间的权限
     * <p>
     * 调用这个方法的时候，返回false时，要么参数不对要么用户已经禁止到了当前的权限。 返回true时，成功拉起了请求权限页面，要在当前的Activity
     * 或者是Fragment重写onRequestPermissionsResult（）方法，请求带的请求码是 SystemCodeUtils.READ_WRITE_MEMORY_CARDS。
     * 检测是否获取到了  内存卡的读写权限
     *
     * @param obj Activity  或者是 Fragment
     * @return true 已经去请求获取权限了   false 参数有问题 没能去请求权限
     */

    public boolean applyReadWriteMemory(Object obj) {
        if (obj == null)
            return false;
        Activity act = null;
        Fragment fra = null;
        switch (judgeActivityAndFragment(obj)) {
            case 1:
                fra = (Fragment) obj;
                act = fra.getActivity();
                break;
            case 2:
                act = (Activity) obj;
                break;
        }
        if (act == null && fra == null)
            return false;
        String[] sdCard = mCuttingModel.getMemoryCutting();
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(act, sdCard[0]) || !ActivityCompat.shouldShowRequestPermissionRationale(act, sdCard[1])) {
//            ToastUtil.showToastResources(R.string.you_have_been_banned_store_read_permissions);
//            return false;
//        }
        if (fra != null)
            openIdentificationV4Frag(fra, sdCard, SystemCodeUtils.READ_WRITE_MEMORY_CARDS);
        else
            openIdentificationAct(act, sdCard, SystemCodeUtils.READ_WRITE_MEMORY_CARDS);
        return true;
    }

    /**
     * 去拉起申请拨打电话的权限
     * <p>
     * 调用这个方法的时候，返回false时，要么参数不对要么用户已经禁止到了当前的权限。 返回true时，成功拉起了请求权限页面，要在当前的Activity
     * 或者是Fragment重写onRequestPermissionsResult（）方法，请求带的请求码是 SystemCodeUtils.CALL_PHONE。
     *
     * @param obj Activity  或者是 Fragment
     * @return
     */
    public boolean applyOpenCallPhoneNumber(Object obj) {
        if (obj == null)
            return false;
        Activity act = null;
        Fragment fra = null;
        switch (judgeActivityAndFragment(obj)) {
            case 1:
                fra = (Fragment) obj;
                act = fra.getActivity();
                break;
            case 2:
                act = (Activity) obj;
                break;
        }
        if (act == null && fra == null)
            return false;
        String[] callPhone = mCuttingModel.getCallPhoneCuttion();
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(act, callPhone[0]) ) {
//           //用户禁用掉了权限
//            return false;
//        }
        if (fra != null)
            openIdentificationV4Frag(fra, callPhone, SystemCodeUtils.CALL_PHONE);
        else
            openIdentificationAct(act, callPhone, SystemCodeUtils.CALL_PHONE);
        return true;
    }

    /**
     * 去拉起申请读取通讯录的权限
     * <p>
     * 调用这个方法的时候，返回false时，要么参数不对要么用户已经禁止到了当前的权限。 返回true时，成功拉起了请求权限页面，要在当前的Activity
     * 或者是Fragment重写onRequestPermissionsResult（）方法，请求带的请求码是 SystemCodeUtils.CALL_PHONE。
     *
     * @param obj Activity  或者是 Fragment
     * @return
     */
    public boolean applyReadCalendar(Object obj) {
        if (obj == null)
            return false;
        Activity act = null;
        Fragment fra = null;
        switch (judgeActivityAndFragment(obj)) {
            case 1:
                fra = (Fragment) obj;
                act = fra.getActivity();
                break;
            case 2:
                act = (Activity) obj;
                break;
        }
        if (act == null && fra == null)
            return false;
        String[] readCalendar = mCuttingModel.getReadCalendarCuttion();
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(act, callPhone[0]) ) {
//           //用户禁用掉了权限
//            return false;
//        }
        if (fra != null)
            openIdentificationV4Frag(fra, readCalendar, SystemCodeUtils.READ_CONTACTS);
        else
            openIdentificationAct(act, readCalendar, SystemCodeUtils.READ_CONTACTS);
        return true;
    }

    /**
     * 判断当前的对象是否是Fragment或者是Activity的子类
     *
     * @param obj Activity  或者是 Fragment
     * @return 是否申请成功
     */
    private int judgeActivityAndFragment(Object obj) {

        if (obj instanceof Activity) {
            return acty;
        } else if (obj instanceof Fragment) {
            return frag;
        }
        return -1;
    }

    /*
     * 判断当前系统是否是  6.0及6.0以上 版本
     *
     * @return 是 true       不是  false
     */
    public boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * 验证 用户权限方法  申请权限后 回调方法 onRequestPermissionsResult 中使用
     *
     * @param act            Activity对象
     * @param permissions    申请权限列表
     * @param grantResults  申请权限回调参数
     * @param title          Dialog标题
     * @param textContext    Dialog显示内容
     * @param abolish        Dialog取消按钮上的文字
     * @param setting        Dialog设置按钮上的文字提示
     * @return   false -- 说明用户有没有给的权限     true -- 已经给了全部的权限
     */
    public boolean verifyPermissions(Activity act, String[] permissions, int[] grantResults, String title, String textContext, String abolish, String setting){
        if(grantResults == null || grantResults.length < 1)
            return true;
        for(int permis : grantResults){
            if(permis != PackageManager.PERMISSION_GRANTED){
                showTipsDialog(act, title, textContext, abolish, setting);
                return false;
            }

        }
        return true;
    }

    /**
     * 显示提示对话框
     *
     * @param context        Activity对象
     * @param title          标题    不设置 为    “提示信息”
     * @param textContext    显示内容  不设置为   “当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【设置】按钮前往设置中心进行权限授权。”
     * @param abolish        取消按钮上的文字    不设置 为 “取消”
     * @param setting        设置按钮上的文字提示  不设置 为 “设置”
     */
    public void showTipsDialog(final Activity context, String title, String textContext, String abolish, String setting) {
        String titleText = "提示信息";
        String text = "当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【设置】按钮前往设置中心进行权限授权。";
        String abolishText = "退出APP";
        String settingText = "设置";
        if(!TextUtils.isEmpty(title))
            titleText = title;
        if(!TextUtils.isEmpty(textContext))
            text = textContext;
        if(!TextUtils.isEmpty(abolish))
            abolishText = abolish;
        if(!TextUtils.isEmpty(setting))
            settingText = setting;
        new AlertDialog.Builder(context)
                .setTitle(titleText)
                .setMessage(text)
                .setNegativeButton(abolishText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProjectActivityManager.getManager().finishAll();
                    }
                })
                .setPositiveButton(settingText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * 打开设置页面中 当前程序 页面
     * @param context
     */
    //打开系统应用设置(ACTION_APPLICATION_DETAILS_SETTINGS:系统设置权限)
    public void startAppSettings(Activity context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        context.startActivityForResult(intent, BaseProjectConfiguration.OPEN_PROJECT_CODE);
    }
}
