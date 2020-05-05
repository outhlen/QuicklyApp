package com.escort.carriage.android.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.escort.carriage.android.entity.bean.push.PushEntity;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.activity.mes.MyOrderListActivity;
import com.escort.carriage.android.ui.activity.my.MyBidActivity;
import com.escort.carriage.android.ui.activity.play.WalletMenuActivity;
import com.tripartitelib.android.iflytek.IflytekUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者： Lance
 * 日期:  2018/3/27 11:04
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.showE(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.showE(TAG, "接受到推送下来的通知");

            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.showE(TAG, "用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            LogUtils.showE(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        LogUtils.showE(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        LogUtils.showE(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtils.showE(TAG, "extras : " + extras);
        try {
            PushEntity jsonBean = JsonManager.getJsonBean(extras, PushEntity.class);
            if(jsonBean.getIsVoice() == 1){
                String speakCheck = CacheDBMolder.getInstance().isNotificationSpeak();
                Log.d("测试",speakCheck);
                // 初始化设置语音开关
                if(speakCheck == null || speakCheck.equals("") || speakCheck.equals("0")){
                    IflytekUtils.getIflytekUtils().startSpeaking(context, message);
                }
            }
            if(jsonBean.page == 1001){
                EventBus.getDefault().post("homeAct_updat_data");
            }

            JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
            Intent intent_into_message = new Intent();
            //  intent_into_message.setClass(context, MesListActivity.class);
            intent_into_message.putExtras(bundle);
            intent_into_message.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (json.optString("name")) {
                case "cancelBind"://解绑
                    break;
            }
        } catch (Exception e) {
            LogUtils.showE(TAG, "Unexpected: extras is not a valid json");
        }
    }

    private void openNotification(Context context, Bundle bundle) {
        try {
            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
            PushEntity jsonBean = JsonManager.getJsonBean(string, PushEntity.class);
            Intent intent_into_message = new Intent();
            intent_into_message.putExtras(bundle);
            intent_into_message.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            货源大厅车主app	page	String	1001	无
//            议价列表货主app	page	String	1002	orderNumber
//            中标列表车主app	page	String	1003	orderNumber
//            订单详情货主app	page	String	1004	orderNumber
//            个人资产货主app/车主app	page	String	1005	orderNumber
//            订单列表货主app	page	String	1006	orderStatus
//            评论列表车主app	page	String	1007	orderStatus
//            订单列表车主app	page	String	1008	orderStatus
            switch (jsonBean.getPage()){
                case 1001://打开APP
                    intent_into_message.setClass(context, HomeActivity.class);
                    intent_into_message.putExtra("showListInfo", true);
                    context.startActivity(intent_into_message);
                    break;
                case 1003://中标列表车主app
                    intent_into_message.setClass(context, MyBidActivity.class);
                    context.startActivity(intent_into_message);
                    break;
                case 1005://分润
                    context.startActivity(new Intent(context, WalletMenuActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case 1007://评论列表车主app
                    break;
                case 1008://订单列表车主app
                    intent_into_message.setClass(context, MyOrderListActivity.class);
                    context.startActivity(intent_into_message);
                    break;

            }
        } catch (Exception e) {
            LogUtils.showE(TAG, "Unexpected: extras is not a valid json");
        }
    }

    private void setIsRead(String id) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("msgId", id);
//        HttpUtils.post(Urls.MSGINFO_ISREAD)
//                .params("params", new Gson().toJson(map))
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//
//                    }
//
//                });
    }
}