package com.escort.carriage.android.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.androidybp.basics.utils.hint.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class PushMsgReceive extends BroadcastReceiver {
    private static final String TAG = "JIGUANG";

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        if (bundle == null) {
            return "bundle is null";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            switch (key) {
                case JPushInterface.EXTRA_NOTIFICATION_ID:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
                    break;
                case JPushInterface.EXTRA_CONNECTION_CHANGE:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
                    break;
                case JPushInterface.EXTRA_EXTRA:
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        LogUtils.showI(TAG, "This message has no Extra data");
                        continue;
                    }
                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();

                        while (it.hasNext()) {
                            String myKey = it.next();
                            sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                        }
                    } catch (JSONException e) {
                        LogUtils.showE(TAG, "Get message extra JSON error!");
                    }
                    break;
                default:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
                    break;
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtils.showE(TAG, "[PushMsgReceive] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                if (bundle == null) {
                    return;
                }
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtils.showE(TAG, "[PushMsgReceive] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                if (bundle == null) {
                    return;
                }
                //收到了自定义消息 Push 。SDK 对自定义消息，只是传递，不会有任何界面上的展示。
                LogUtils.showE(TAG, "[PushMsgReceive] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                //收到了推送通知。
                // 如果通知的内容为空，则在通知栏上不会展示通知。
                // 但是，这个广播 Intent 还是会有。开发者可以取到通知内容外的其他信息。
                if (bundle == null) {
                    return;
                }
                // 通知标题
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                // 通知栏的Notification ID，可以用于清除Notification。如果服务端内容（alert）字段为空，则notification id 为0
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                // 保存服务器推送下来的通知内容。
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                //保存服务器推送下来的附加字段。这是个 JSON 字符串。
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                // 富媒体通知推送下载的HTML的文件路径,用于展现WebView。
                String fileHtml = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH);

                LogUtils.showE(TAG, "[PushMsgReceive] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtils.showE(TAG, "[PushMsgReceive] 用户点击打开了通知");
                // 处理打开通知栏
                openNotification(context, bundle);
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                if (bundle == null) {
                    return;
                }
                LogUtils.showE(TAG, "[PushMsgReceive] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                // JPush 服务的连接状态发生变化。
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtils.showE(TAG, "[PushMsgReceive]" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtils.showE(TAG, "[PushMsgReceive] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            LogUtils.showE(TAG, "exception=" + e.getMessage());
        }
    }


    /**
     * 处理用户打开通知栏
     */
    private void openNotification(Context context, @Nullable Bundle bundle) throws Exception {
        LogUtils.showD("PushMessageReceiver", "处理用户打开通知栏");
//        if (bundle == null) {
//            LogUtils.showE("YCTest", "openNotification bundle=null");
//            JPushUtil.lunchMainActivity(context);
//            return;
//        }
//        // 扩展消息
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        PushData pushData = RJson.fromJson(extras, PushData.class);
//        if (pushData == null || TextUtils.isEmpty(pushData.getPushType())) {
//            JPushUtil.lunchMainActivity(context);
//            return;
//        }
//        if (AppUtils.isAppForeground(context)) {
//            // app在前台,直接跳转对应的页面
//            JPushUtil.jump2Pages(context, pushData);
//        } else {
//            // app在后台，先开启欢迎页，再跳转相对应的页面
//            JPushUtil.lunchMainActivity(context, pushData);
//        }
    }


}
