package com.escort.carriage.android.jpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.escort.carriage.android.entity.bean.push.PushEntity;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.activity.mes.MyOrderListActivity;
import com.escort.carriage.android.ui.activity.my.MyBidActivity;
import com.escort.carriage.android.ui.activity.play.WalletMenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class OpenClickActivity extends Activity {

    private static final String TAG = "OpenClickActivity";
    /**消息Id**/
    private static final String KEY_MSGID = "msg_id";
    /**该通知的下发通道**/
    private static final String KEY_WHICH_PUSH_SDK = "rom_type";
    /**通知标题**/
    private static final String KEY_TITLE = "n_title";
    /**通知内容**/
    private static final String KEY_CONTENT = "n_content";
    /**通知附加字段**/
    private static final String KEY_EXTRAS = "n_extras";
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView = new TextView(this);
        setContentView(mTextView);
        handleOpenClick();
    }


    /**
     * 处理点击事件，当前启动配置的Activity都是使用
     * Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
     * 方式启动，只需要在onCreat中调用此方法进行处理
     */
    private void handleOpenClick() {
        Log.d(TAG, "用户点击打开了通知");
        String data = null;
        //获取华为平台附带的jpush信息
        if (getIntent().getData() != null) {
            data = getIntent().getData().toString();
        }
        //获取fcm、oppo平台附带的jpush信息
        if(TextUtils.isEmpty(data) && getIntent().getExtras() != null){
            data = getIntent().getExtras().getString("JMessageExtra");
        }
        Log.w(TAG, "msg content is " + String.valueOf(data));
        if (TextUtils.isEmpty(data)) return;
        try {
            JSONObject jsonObject = new JSONObject(data);
            String string = jsonObject.getString(JPushInterface.EXTRA_EXTRA);
            PushEntity jsonBean = JsonManager.getJsonBean(string, PushEntity.class);
            Intent intent_into_message = new Intent();
            intent_into_message.putExtras(new Bundle());
            intent_into_message.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (jsonBean.getPage()){
                case 1001://打开APP
                    intent_into_message.setClass(this, HomeActivity.class);
                    intent_into_message.putExtra("showListInfo", true);
                    startActivity(intent_into_message);
                    break;
                case 1003://中标列表车主app
                    intent_into_message.setClass(this, MyBidActivity.class);
                    startActivity(intent_into_message);
                    break;
                case 1005://分润
                    startActivity(new Intent(this, WalletMenuActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case 1007://评论列表车主app
                    break;
                case 1008://订单列表车主app
                    intent_into_message.setClass(this, MyOrderListActivity.class);
                    startActivity(intent_into_message);
                    break;

            }

            //上报点击事件
           // JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);
        } catch (JSONException e) {
            Log.w(TAG, "parse notification error");
        }


    }

    private String getPushSDKName(byte whichPushSDK) {
        String name;
        switch (whichPushSDK){
            case 0:
                name = "jpush";
                break;
            case 1:
                name = "xiaomi";
                break;
            case 2:
                name = "huawei";
                break;
            case 3:
                name = "meizu";
                break;
            case 4:
                name= "oppo";
                break;
            case 5:
                name = "vivo";
                break;
            case 8:
                name = "fcm";
                break;
            default:
                name = "jpush";
        }
        return name;
    }

}
