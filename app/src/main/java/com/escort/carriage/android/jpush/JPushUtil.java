package com.escort.carriage.android.jpush;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送工具类
 *
 * @author yangchong
 * @author lihongfang  修改
 */
public class JPushUtil {
    public static final String PREFS_NAME = "JPUSH";
    public static final String PREFS_DAYS = "JPUSH_DAYS";
    public static final String PREFS_START_TIME = "PREFS_START_TIME";
    public static final String PREFS_END_TIME = "PREFS_END_TIME";
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";

    public static boolean isEmpty(String s) {
        return null == s || s.length() == 0 || s.trim().length() == 0;
    }

    /**
     * 获取极光推送的tags
     */
    public static void getJPushTags(String userId) {
//        SimpleObserver observer = ApiFactory.getApiSevice().getPushTags("2", userId)
//                .subscribeOn(Schedulers.io())
//                //.observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new SimpleObserver<JPushTagsResp>() {
//                    @Override
//                    public void onSuccess(JPushTagsResp resp) {
//                        JPushTagsResp.ObjBean obj = resp.getObj();
//                        if (obj == null) {
//                            return;
//                        }
//                        String alias = obj.getAlias();
//                        if (!TextUtils.isEmpty(alias) && isValidTagAndAlias(alias)) {
//                            JPushUtil.setAlias(MainApp.getInstance(), ConsValues.JPUSH_SET_ALIAS, alias);
//                        }
//                        // 设置标签
//                        Set<String> tagSet = new HashSet<>();
//                        List<String> tagList = obj.getTagList();
//                        for (String tag : tagList) {
//                            if (!TextUtils.isEmpty(tag) && isValidTagAndAlias(tag)) {
//                                tagSet.add(tag);
//                            }
//                        }
//                        String imei = getImei(MainApp.getInstance());
//                        if (!TextUtils.isEmpty(imei)) {
//                            tagSet.add(imei);
//                            Logger.e("YCTest", "imei=" + imei);
//                        }
//                        if (tagSet.size() > 0) {
//                            JPushUtil.setTags(MainApp.getInstance(), ConsValues.JPUSH_SET_TAGS, tagSet);
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(int errCode, String msg) {
//
//                    }
//                });
    }

    /**
     * 设置极光推送别名
     *
     * @param context  - 上下文
     * @param sequence - 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param alias    - 每次调用设置有效的别名，覆盖之前的设置
     */
    public static void setAlias(Context context, int sequence, String alias) {
        JPushInterface.setAlias(context, sequence, alias);
    }

    /**
     * 设置极光推送标签
     *
     * @param context   - 上下文
     * @param sequence- 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param tags      - 每次调用至少设置一个 tag，覆盖之前的设置，不是新增。
     */
    public static void setTags(Context context, int sequence, Set<String> tags) {
        JPushInterface.setTags(context, sequence, tags);
    }

    /**
     * 清除极光推送的所有Tags
     */
    public static void cleanTags(Context context) {
        JPushInterface.cleanTags(context.getApplicationContext(), JpushConfig.JPUSH_CLEAN_TAGS);
    }

    /**
     * 删除极光推送别名
     */
    public static void deleteAlias(Context context) {
        JPushInterface.deleteAlias(context.getApplicationContext(), JpushConfig.JPUSH_DELETE_ALIAS);
    }

    /**
     * 校验Tag Alias 只能是数字,英文字母和中文
     */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 取得AppKey
     */
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException ignored) {

        }
        return appKey;
    }

    /**
     * 取得版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static void showToast(final String toast, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return telephonyManager.getDeviceId();
        } catch (Exception ignored) {

        }
        return null;
    }

    private static boolean isReadableASCII(CharSequence string) {
        if (TextUtils.isEmpty(string)) return false;
        try {
            Pattern p = Pattern.compile("[\\x20-\\x7E]+");
            return p.matcher(string).matches();
        } catch (Throwable e) {
            return true;
        }
    }

    public static String getDeviceId(Context context) {
        return JPushInterface.getUdid(context);
    }

    /**
     * 跳转指定页面
     */
//    public static void jump2Pages(Context context, @NonNull PushData pushData) throws JSONException {
//        switch ("" + pushData.getPushType()) {
//            case "communityNotice":// 小区公告
//                String obj = pushData.getObj();
//                JSONObject jsonObj = new JSONObject(obj);
//                String noticeId = jsonObj.optString("noticeId");
//                // 跳转公告详情页面
//                String urlDetailsUrl = H5PageUtils.getNoticeDetails(noticeId);
//                // 活动详情页面
//                CommonUtils.go2WebView(context, "", urlDetailsUrl);
//                break;
//            case "communityActivity":// 活动
//                // {"obj":"{\"serviceId\":\"20\"}","pushType":"communityActivity"}
//                String activityObj = pushData.getObj();
//                JSONObject actJsonObj = new JSONObject(activityObj);
//                String serviceId = actJsonObj.optString("serviceId");
//                // 跳转公告详情页面
//                String actDetailUrl = H5PageUtils.getActivityDetails(serviceId);
//                // 活动详情页面
//                CommonUtils.go2WebView(context, "", actDetailUrl);
//                break;
//            case "reggestReply":// 回复
//                // {"pushType":"reggestReply"}
//                String adviceUrl = H5PageUtils.getAdviceUrl();
//                //默认地址
//                CommonUtils.go2WebView(context, "", adviceUrl);
//                break;
//            case "repairRemind":// 报修
//                // {"obj":"{\"topTabId\":\"0\"}","pushType":"repairRemind"}
//                //  {"obj":"{\"topTabId\":\"0\"}","pushType":"repairRemind"}
//                String repairUrl = H5PageUtils.getRepairUrl();
//                //默认地址
//                CommonUtils.go2WebView(context, "", repairUrl);
//                break;
//            default:
//                lunchMainActivity(context);
//                break;
//        }
//    }


//    public static void lunchMainActivity(Context context) {
//        lunchMainActivity(context, null);
//    }

//    public static void lunchMainActivity(Context context, PushData pushData) {
//        boolean isForeground = AppUtils.isAppForeground(context);
//        Logger.e("YCTest", "isForeground=" + isForeground);
//        if (!AppUtils.isAppForeground(context)) {
//            Intent intent = new Intent(context, SplashActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            if (pushData != null) {
//                intent.putExtra(JpushConfig.INTENT_KEY_PUSH_DATA, pushData);
//            }
//            context.startActivity(intent);
//        }
//    }
}
