package com.escort.carriage.android.ui.view.holder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.config.AppConfig;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.bean.ResponseHXEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.ResponseIntegerBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.activity.OrderTraceActivty;
import com.escort.carriage.android.ui.activity.my.EnterpriseAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.PersonageAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.SettingActivity;
import com.escort.carriage.android.ui.activity.my.UserInfoActivity;
import com.escort.carriage.android.ui.activity.play.WalletMenuActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import com.escort.carriage.android.ui.view.imgview.RoundImageView;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;

import org.apache.http.util.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeLeftHolder implements View.OnClickListener {

    @BindView(R.id.iv_head_img)
    RoundImageView iv_head_img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_use_day)
    TextView tvUseDay;
    @BindView(R.id.llInfoGroup)
    LinearLayout llInfoGroup;
    @BindView(R.id.headerBl)
    View headerBl;
    @BindView(R.id.ll_user_info)
    RelativeLayout llUserInfo;
    @BindView(R.id.ll_history_group)
    LinearLayout llHistoryGroup;
    @BindView(R.id.ll_my_money)
    LinearLayout llMyMoney;
    @BindView(R.id.ll_dl_manager)
    LinearLayout llDlManager;
    @BindView(R.id.ll_yq_hy)
    LinearLayout llYqHy;
    @BindView(R.id.ll_fp_manager)
    LinearLayout llFpManager;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.llPersonageAuthentication)
    LinearLayout llPersonageAuthentication;
    @BindView(R.id.llEnterpriseAuthentication)
    LinearLayout llEnterpriseAuthentication;
    @BindView(R.id.ll_my_trice_order)
    LinearLayout traceLayout;
    private View viewGroup;
    private HomeActivity activity;
    private Unbinder bind;
    private String defautName="小二";
    public HomeLeftHolder(HomeActivity activity, FrameLayout leftFrameLayout) {
        this.activity = activity;
        viewGroup = View.inflate(activity, R.layout.view_slide_menu, null);
        bind = ButterKnife.bind(this, viewGroup);
        leftFrameLayout.removeAllViews();
        leftFrameLayout.addView(viewGroup);
        initView();
    }

    private void initView() {
        viewGroup.findViewById(R.id.iv_head_img).setOnClickListener(this);
        viewGroup.findViewById(R.id.myBid).setOnClickListener(this);
        viewGroup.findViewById(R.id.myOrderList).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_history_group).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_my_money).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_setting).setOnClickListener(this);
        viewGroup.findViewById(R.id.llPersonageAuthentication).setOnClickListener(this);
        viewGroup.findViewById(R.id.llEnterpriseAuthentication).setOnClickListener(this);
        viewGroup.findViewById(R.id.llEscortAuthentication).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_feedback).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_my_transfer_order).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_dl_manager).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_yq_hy).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_fp_manager).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_my_trice_order).setOnClickListener(this);
        viewGroup.findViewById(R.id.ll_kefu).setOnClickListener(this);
    }

    public void updataUserInfo(UserInfoEntity userInfoEntity) {
        tvName.setText(userInfoEntity.getNickName());
        tvUseDay.setText("使用公共服务" + userInfoEntity.days + "天");
        GlideManager.getGlideManager().loadImageRoundFitCrop(userInfoEntity.headPictureUrl, iv_head_img, 100, R.mipmap.ic_launcher_round);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_img:
                activity.startActivityForResult(new Intent(activity, UserInfoActivity.class), 123);
                break;
            case R.id.myBid:
               // activity.startActivity(new Intent(activity, MyBidActivity.class));
                break;
            case R.id.myOrderList:
                //activity.startActivity(new Intent(activity, MyOrderListActivity.class));
                break;
            case R.id.ll_history_group:
               // activity.startActivity(new Intent(activity, HistoryOrderListActivity.class));
                break;
            case R.id.ll_my_money:
                activity.startActivity(new Intent(activity, WalletMenuActivity.class));
                break;
            case R.id.ll_setting:
                activity.startActivity(new Intent(activity, SettingActivity.class));
                break;
            case R.id.llEscortAuthentication:
                //押镖认证
               // Intent intent = new Intent(activity, EscortAuthenticationActivity.class);
               // activity.startActivity(intent);
                break;
            case R.id.llPersonageAuthentication:
                //获取认证状态
                getPersonageAuthentication(1);//个人认证
                break;
            case R.id.llEnterpriseAuthentication://企业认证
                getPersonageAuthentication(2);
                break;
            case R.id.ll_feedback://意见反馈
                Intent intentFeedback = new Intent(activity, VueActivity.class);
                intentFeedback.putExtra("url", VueUrl.feedback);
                activity.startActivity(intentFeedback);
                break;
            case R.id.ll_my_transfer_order://我的转单
                Intent intenttransferOrder = new Intent(activity, VueActivity.class);
                intenttransferOrder.putExtra("url", VueUrl.transferOrderList);
                activity.startActivity(intenttransferOrder);
                break;
            case R.id.ll_dl_manager://代理管理
                Intent intentDaiLi = new Intent(activity, VueActivity.class);
                intentDaiLi.putExtra("url", VueUrl.theAdministrativeAgent);
                activity.startActivity(intentDaiLi);
                break;
            case R.id.ll_yq_hy://邀请好友
                Intent intentYqhy = new Intent(activity, VueActivity.class);
                intentYqhy.putExtra("url", VueUrl.inviteFriends);
                activity.startActivity(intentYqhy);
                break;
            case R.id.ll_fp_manager://发票管理
                Intent intentFpgl = new Intent(activity, VueActivity.class);
                intentFpgl.putExtra("url", VueUrl.invoiceManagement);
                activity.startActivity(intentFpgl);
                break;
            case R.id.ll_my_trice_order://订单追踪
                Intent intentOrder = new Intent(activity, OrderTraceActivty.class);
                activity.startActivity(intentOrder);
                break;
            case R.id.ll_kefu://客服
                if (ChatClient.getInstance().isLoggedInBefore()) {
                    toChatActivity();
                } else { //未登录
                    //创建一个用户并登录环信服务器
                    initHuanXinToken();
                }
                break;
        }
    }

    private void toChatActivity() {
//        Bundle bundle = new Bundle();
//        bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, 0);
//        Intent intent = new IntentBuilder(activity)
//                .setTargetClass(ChatActivity.class)
//                .setVisitorInfo(DemoMessageHelper.createVisitorInfo())
//                .setServiceIMNumber(AppConfig.HX_IMNUMBER)
//                .setScheduleQueue(MessageHelper.createQueueIdentity("客服服务"))
//                .setShowUserNick(true)
//                .setBundle(bundle)
//                .setDefautName(defautName)
//                .build();
//        activity.startActivity(intent);
    }

//    private ProgressDialog getProgressDialog() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(activity);
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    progressShow = false;
//                }
//            });
//        }
//        return progressDialog;
//    }

//    public class MyConnectionListener implements ChatClient.ConnectionListener {
//
//        @Override
//        public void onConnected() {
//
//        }
//
//        @Override
//        public void onDisconnected(final int errorCode) {
//            if (errorCode == Error.USER_NOT_FOUND || errorCode == Error.USER_LOGIN_ANOTHER_DEVICE
//                    || errorCode == Error.USER_AUTHENTICATION_FAILED
//                    || errorCode == Error.USER_REMOVED) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //demo中为了演示当用户被删除或者修改密码后验证失败,跳出会话界面
//                        //正常APP应该跳到登录界面或者其他操作
//                        if (ChatActivity.instance != null) {
//                            ChatActivity.instance.finish();
//                        }
//                        Intent intent  = new Intent(activity, LoginActivity.class);
//                        activity.startActivity(intent);
//                        ChatClient.getInstance().logout(false, null);
//                    }
//                });
//            }
//        }
//
//    }

    private void initHuanXinToken() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(activity, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.HUANXIN_REGISTER_URL, jsonString).execute(new MyStringCallback<ResponseHXEntity>() {
            @Override
            public void onResponse(ResponseHXEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s.isSuccess()) {
                    Log.e("HuanXinData>>","responseData=="+s.getData().getHxUserName().toString());
                    AppConfig.HXUSERNAME  = s.getData().getHxUserName();
                    AppConfig.HXPASSWORD  = s.getData().getHxPassword();
                    if(!TextUtils.isEmpty(s.getData().getHxKefuName())) {
                        defautName = s.getData().getHxKefuName();
                    }
                    ChatClient.getInstance().login(AppConfig.HXUSERNAME, AppConfig.HXPASSWORD, new Callback() {
                        @Override
                        public void onSuccess() {
//                            Intent intentService = new IntentBuilder(activity)
//                                    .setServiceIMNumber(AppConfig.HX_IMNUMBER)
//                                    .build();
//                            activity.startActivity(intentService);
                            toChatActivity();
                        }
                        @Override
                        public void onError(int code, String error) {
                            Log.e("initHuanXinToken>>","error==>>"+error);
                        }
                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });
                }
            }

            @Override
            public Class<ResponseHXEntity> getClazz() {
                return ResponseHXEntity.class;
            }
        });
    }

    /**
     * 获取个人认证状态   1 个人认证 2 企业认证 3 服务认证 4 推广员
     */
    private void getPersonageAuthentication(int type) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(activity, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(type);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.UNDERTAKE_USERAUTH_JUDGEAUTH, jsonString).execute(new MyStringCallback<ResponseIntegerBean>() {
            @Override
            public void onResponse(ResponseIntegerBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    int status = 0;
                    if (s.data != null) {
                        status = s.data;
                    }
                    if (s.success) {
                        if (status == 1) {
                            ToastUtil.showToastString("认证正在审核中");
                        } else {
                            if (type == 1) {
                                Intent intent = new Intent(activity, PersonageAuthenticationActivity.class);
                                intent.putExtra("status", status);
                                activity.startActivity(intent);
                            } else if (type == 2) {
                                Intent intent = new Intent(activity, EnterpriseAuthenticationActivity.class);
                                intent.putExtra("status", status);
                                activity.startActivity(intent);
                            }
                        }

                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseIntegerBean> getClazz() {
                return ResponseIntegerBean.class;
            }
        });
    }

    public View getLeftGroupView() {
        return viewGroup;
    }


    public void clear() {
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
        viewGroup = null;
        activity = null;

    }


}
