package com.escort.carriage.android.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.escort.carriage.android.R;
import com.escort.carriage.android.config.AppConfig;
import com.escort.carriage.android.ui.activity.ContextMenuActivity;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.widget.chatrow.ChatRowEvaluation;
import com.escort.carriage.android.ui.widget.chatrow.ChatRowForm;
import com.escort.carriage.android.ui.widget.chatrow.ChatRowLocation;
import com.escort.carriage.android.ui.widget.chatrow.ChatRowOrder;
import com.escort.carriage.android.ui.widget.chatrow.ChatRowTrack;
import com.escort.carriage.android.utils.Preferences;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.ChatManager;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.callback.ValueCallBack;
import com.hyphenate.helpdesk.easeui.provider.CustomChatRowProvider;
import com.hyphenate.helpdesk.easeui.recorder.MediaManager;
import com.hyphenate.helpdesk.easeui.ui.ChatFragment;
import com.hyphenate.helpdesk.easeui.util.CommonUtils;
import com.hyphenate.helpdesk.easeui.widget.AlertDialogFragment;
import com.hyphenate.helpdesk.easeui.widget.MessageList;
import com.hyphenate.helpdesk.easeui.widget.ToastHelper;
import com.hyphenate.helpdesk.easeui.widget.chatrow.ChatRow;
import com.hyphenate.helpdesk.model.MessageHelper;
import com.hyphenate.util.EMLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.UUID;

public class CustomChatFragment extends ChatFragment implements ChatFragment.EaseChatFragmentListener {

    //避免和基类定义的常量可能发生冲突,常量从11开始定义
    private static final int ITEM_MAP = 11;
    private static final int ITEM_LEAVE_MSG = 12;//ITEM_SHORTCUT = 12;
    private static final int ITEM_VIDEO = 13;
    private static final int ITEM_EVALUATION = 14;

    private static final int REQUEST_CODE_SELECT_MAP = 11;
    private static final int REQUEST_CODE_SHORTCUT = 12;

    public static final int REQUEST_CODE_CONTEXT_MENU = 13;

    //message type 需要从1开始
    public static final int MESSAGE_TYPE_SENT_MAP = 1;
    public static final int MESSAGE_TYPE_RECV_MAP = 2;
    public static final int MESSAGE_TYPE_SENT_ORDER = 3;
    public static final int MESSAGE_TYPE_RECV_ORDER = 4;
    public static final int MESSAGE_TYPE_SENT_EVAL = 5;
    public static final int MESSAGE_TYPE_RECV_EVAL = 6;
    public static final int MESSAGE_TYPE_SENT_TRACK = 7;
    public static final int MESSAGE_TYPE_RECV_TRACK = 8;
    public static final int MESSAGE_TYPE_SENT_FORM = 9;
    public static final int MESSAGE_TYPE_RECV_FORM = 10;

    Preferences preferences = null;
    //message type 最大值
    public static final int MESSAGE_TYPE_COUNT = 13;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNewRobotWelcome();
    }

    @Override
    protected void setUpView() {
        //这是新添加的扩展点击事件
        setChatFragmentListener(this);
        super.setUpView();
        //可以在此处设置titleBar(标题栏)的属性
//        titleBar.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        titleBar.setLeftImageResource(R.drawable.hd_icon_title_back);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
                getActivity().finish();
            }
        });
        titleBar.setTitle("客服服务");
        titleBar.setRightImageResource(R.drawable.hd_chat_delete_icon);
        titleBar.setRightLayoutClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

    }


    public void getNewRobotWelcome(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String tenantId = AppConfig.HX_TENANTID;
                String orgName = "1460200428068026";
                String appName = "kefuchannelapp80531";
                //username IM服务叿
                 String userName = AppConfig.HX_IMNUMBER;
                //当前登录人的用户token
                String userToken = ChatClient.getInstance().accessToken();
                String url  = "http://kefu.easemob.com/v1/webimplugin/tenants/robots/welcome?channelType=easemob&originType=app&tenantId="+tenantId+"&orgName="+orgName+"&appName="+appName+"&userName="+userName+"&token="+userToken;
                HttpGet httpGet = new HttpGet(url);
                Log.e("getNewRobotWelcome>>","url=="+url);
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    int code = response.getStatusLine().getStatusCode();
                    if (code == 200) {
                        final String rev  = EntityUtils.toString(response.getEntity());
                        JSONObject obj = new JSONObject(rev);
                        Log.e("数据1----〿",obj.getJSONObject("entity").getString("greetingText"));
                        int type = obj.getJSONObject("entity").getInt("greetingTextType");
                        final String rob_welcome = obj.getJSONObject("entity").getString("greetingText");
                        // type丿代表是文字消息的机器人欢迎语
                        //type丿代表是菜单消息的机器人欢迎语
                        if(type == 0){
                            //把解析拿到的string保存在本圿
                            Preferences.getInstance(getActivity()).setRobotText(rob_welcome);
                        }else if(type == 1){
                            final String str = rob_welcome.replaceAll("&amp;quot;","\"");
                            JSONObject json = new JSONObject(str);
                            JSONObject ext = json.getJSONObject("ext");
                            final JSONObject msgtype = ext.getJSONObject("msgtype");
                            //把解析拿到的string保存在本圿
                            Preferences.getInstance(getActivity()).setRobotText(msgtype.toString());
                            Log.e("TAG","菜单消息" +"rob_welcome= "+ msgtype );
                        }
                        ChatClient.getInstance().chatManager().getCurrentSessionId(AppConfig.HX_IMNUMBER, new ValueCallBack<String>() {
                            @Override
                            public void onSuccess(String value) {
                                Log.e("TAG  value:",value + "  当返回value不为空时，则返回的当前会话的会话ID，也就是说会话正在咨询中，不需要发送欢迎语" );
                                if (!TextUtils.isEmpty(value)){//
                                    saveMessage();
                                }
                            }

                            @Override
                            public void onError(int error, String errorMsg) {
                                Log.e("onError=","errorMsg=="+errorMsg);
                            }
                        });
                    }
                }catch(final Exception e){
                    Log.e("TAG","错误" +"rob_welcome="+ e.getMessage() );
                }
            }
        }).start();
    }


    /**
     * 保存欢迎语到本地
     */
    public void saveMessage(){
        Message message = Message.createReceiveMessage(Message.Type.TXT);
        //从本地获取保存的string
        String str =   Preferences.getInstance(getActivity()).getRobotText();
        EMTextMessageBody body = null;
        //判断是否是菜单消息的string，这是需要自己实现的一个方泿
        if(!isRobotMenu(str)){
            //文字消息直接去设置给消息
            body = new EMTextMessageBody(str);
        }else{
            //菜单消息需要设置给消息扩展
            try{
                body = new EMTextMessageBody("");
                JSONObject msgtype = new JSONObject(str);
                message.setAttribute("msgtype",msgtype);
            }catch (Exception e){
                Log.e("RobotMenu","onError:"+e.getMessage());
            }
        }
        message.setFrom(AppConfig.HX_IMNUMBER);
        message.addBody(body);
        message.setMsgTime(System.currentTimeMillis());
        message.setStatus(Message.Status.SUCCESS);
        message.setMsgId(UUID.randomUUID().toString());
        ChatClient.getInstance().chatManager().saveMessage(message);
        messageList.refresh();
        Log.e("Robot Message","saveMessage : "+ message.toString() + "  ext: "  + message.ext() );
    }

    private boolean isRobotMenu(String str){
        try {
            JSONObject json = new JSONObject(str);
            JSONObject obj = json.getJSONObject("choice");
        }catch (Exception e){
            return false;
        }
        return true;
    }
    private void showAlertDialog() {
        FragmentTransaction mFragTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        String fragmentTag = "dialogFragment";
        Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if(fragment!=null){
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }
        final AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.setTitleText(getString(R.string.prompt));
        dialogFragment.setContentText(getString(R.string.Whether_to_empty_all_chats));
        dialogFragment.setupLeftButton(null, null);
        dialogFragment.setupRightBtn(null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatClient.getInstance().chatManager().clearConversation(toChatUsername);
                messageList.refresh();
                dialogFragment.dismiss();
                MediaManager.release();
            }
        });
        dialogFragment.show(mFragTransaction, fragmentTag);
    }

    @Override
    public void onAvatarClick(String username) {
        //头像点击事情
//        startActivity(new Intent(getActivity(), ...class));
    }

    @Override
    public boolean onMessageBubbleClick(Message message) {
        //消息框点击事件,return true
        if (message.getType() == Message.Type.LOCATION) {
//            EMLocationMessageBody locBody = (EMLocationMessageBody) message.body();
//            Intent intent = new Intent(getActivity(), BaiduMapActivity.class);
//            intent.putExtra("latitude", locBody.getLatitude());
//            intent.putExtra("longitude", locBody.getLongitude());
//            intent.putExtra("address", locBody.getAddress());
//            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(Message message) {
        //消息框长按
        startActivityForResult(new Intent(getActivity(), ContextMenuActivity.class).putExtra("message", message), REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public  void onMessageItemClick(Message message, MessageList.ItemAction action)
    {
        switch (action) {
            case ITEM_TO_NOTE:
//                Intent intent = new Intent(getActivity(), NewLeaveMessageActivity.class);
//                startActivity(intent);
                break;
            case ITEM_RESOLVED:
                ChatManager.getInstance().postRobotQuality(message, true, null, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        EMLog.d(TAG, "robot comment sucess");
                        MessageHelper.createCommentSuccessMsg(message,"");
                        messageList.refresh();
                    }

                    @Override
                    public void onError(int i, String s) {
                        EMLog.e(TAG, "robot comment fail: " + i + "reason: " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
            case ITEM_UNSOLVED:
//                Intent tagsIntent = new Intent(getActivity(), RobotCommentTagsActivity.class);
//                tagsIntent.putExtra("msgId", message.messageId());
//                startActivity(tagsIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_MAP: //地图
               // startActivityForResult(new Intent(getActivity(), BaiduMapActivity.class), REQUEST_CODE_SELECT_MAP);
                break;
            case ITEM_LEAVE_MSG://ITEM_SHORTCUT:
//                Intent intent = new Intent(getActivity(), NewLeaveMessageActivity.class);
//                startActivity(intent);
//                getActivity().finish();
                break;

            case ITEM_VIDEO:
                startVideoCall();
                break;
            case ITEM_EVALUATION:
                ChatClient.getInstance().chatManager().asyncSendInviteEvaluationMessage(toChatUsername, null);
                break;
//            case ITEM_FILE:
//                //如果需要覆盖内部的,可以return true
//                //demo中通过系统API选择文件,实际app中最好是做成qq那种选择发送文件
//                return true;
            default:
                break;
        }
        //不覆盖已有的点击事件
        return false;
    }


    private void startVideoCall(){
        inputMenu.hideExtendMenuContainer();

        Message message = Message.createVideoInviteSendMessage(getString(R.string.em_chat_invite_video_call), toChatUsername);
        ChatClient.getInstance().chatManager().sendMessage(message);
    }



    @Override
    public CustomChatRowProvider onSetCustomChatRowProvider() {
        return new DemoCustomChatRowProvider();
    }

    @Override
    protected void registerExtendMenuItem() {
        //demo 这里不覆盖基类已经注册的item, item点击listener沿用基类的
        super.registerExtendMenuItem();
        //增加扩展的item
        inputMenu.registerExtendMenuItem(R.string.attach_location, R.drawable.hd_chat_location_selector, ITEM_MAP, R.id.chat_menu_map, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.leave_title, R.drawable.em_chat_phrase_selector, ITEM_LEAVE_MSG, R.id.chat_menu_leave_msg, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_call_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, R.id.chat_menu_video_call, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_evaluation, R.drawable.em_chat_evaluation_selector, ITEM_EVALUATION, R.id.chat_menu_evaluation, extendMenuItemClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // 复制消息
                    String string = ((EMTextMessageBody) contextMenuMessage.body()).getMessage();
                    clipboard.setText(string);
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // 删除消息
                    if (contextMenuMessage.getType() == Message.Type.VOICE){
                        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) contextMenuMessage.body();
                        String voicePath = voiceBody.getLocalUrl();
                        MediaManager.release(voicePath);
                    }
                    conversation.removeMessage(contextMenuMessage.messageId());
                    messageList.refresh();
                    break;
                default:
                    break;
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_MAP) {
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress, toChatUsername);
                } else {
                    ToastHelper.show(getActivity(), "定位失败");
                }
            } else if (requestCode == REQUEST_CODE_SHORTCUT) {
                String content = data.getStringExtra("content");
                if (!TextUtils.isEmpty(content)) {
                    inputMenu.setInputMessage(content);
                }
            } else if (requestCode == REQUEST_CODE_EVAL) {
                messageList.refresh();
            }
        }

    }

    @Override
    public void onMessageSent() {
        messageList.refreshSelectLast();
    }

    /**
     * chat row provider
     */
    private final class DemoCustomChatRowProvider implements CustomChatRowProvider {

        @Override
        public int getCustomChatRowTypeCount() {
            //地图 和 满意度 发送接收 共4种
            //订单 和 轨迹 发送接收共4种
            // form 发送接收2种
            return MESSAGE_TYPE_COUNT;
        }

        @Override
        public int getCustomChatRowType(Message message) {
            //此处内部有用到,必须写否则可能会出现错位
            if (message.getType() == Message.Type.LOCATION){
                return message.direct() == Message.Direct.RECEIVE ? MESSAGE_TYPE_RECV_MAP : MESSAGE_TYPE_SENT_MAP;
            }else if (message.getType() == Message.Type.TXT){
                switch (MessageHelper.getMessageExtType(message)) {
                    case EvaluationMsg:
                        return message.direct() == Message.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EVAL : MESSAGE_TYPE_SENT_EVAL;
                    case OrderMsg:
                        return message.direct() == Message.Direct.RECEIVE ? MESSAGE_TYPE_RECV_ORDER : MESSAGE_TYPE_SENT_ORDER;
                    case TrackMsg:
                        return message.direct() == Message.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TRACK : MESSAGE_TYPE_SENT_TRACK;
                    case FormMsg:
                        return message.direct() == Message.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FORM : MESSAGE_TYPE_SENT_FORM;
                }
            }

            return -1;
        }

        @Override
        public ChatRow getCustomChatRow(Message message, int position, BaseAdapter adapter) {
            if (message.getType() == Message.Type.LOCATION) {
                return new ChatRowLocation(getActivity(), message, position, adapter);
            } else if (message.getType() == Message.Type.TXT) {
                switch (MessageHelper.getMessageExtType(message)) {
                    case EvaluationMsg:
                        return new ChatRowEvaluation(getActivity(), message, position, adapter);
                    case OrderMsg:
                        return new ChatRowOrder(getActivity(), message, position, adapter);
                    case TrackMsg:
                        return new ChatRowTrack(getActivity(), message, position, adapter);
                    case FormMsg:
                        return new ChatRowForm(getActivity(), message, position, adapter);
                }
            }
            return null;
        }
    }



}
