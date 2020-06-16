package com.escort.carriage.android.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.config.AppConfig;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.bean.ResponseHXEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.ResponseIntegerBean;
import com.escort.carriage.android.http.MyStringCallback;
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
import butterknife.OnClick;

public class UserInfoManagerActivity extends ProjectBaseActivity {

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
    @BindView(R.id.personLayout)
    LinearLayout personLayout;
    String defautName  = "公共服务部分";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_man_info);
        ButterKnife.bind(this);
        setPageActionBar();
        initView();
    }

    private void initView() {

    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("公共服务Demo");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.iv_head_img, R.id.ll_history_group, R.id.ll_my_money, R.id.ll_setting,
           R.id.llPersonageAuthentication,R.id.llEnterpriseAuthentication,R.id.personLayout,R.id.ll_yq_hy,R.id.ll_kefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head_img:
                startActivityForResult(new Intent(this, UserInfoActivity.class), 123);
                break;
//            case R.id.myBid:
//                startActivity(new Intent(this, MyBidActivity.class));
//                break;
//            case R.id.myOrderList:
//                startActivity(new Intent(this, MyOrderListActivity.class));
//                break;
//            case R.id.ll_history_group:
//                startActivity(new Intent(this, HistoryOrderListActivity.class));
//                break;
            case R.id.ll_my_money:
                startActivity(new Intent(this, WalletMenuActivity.class));
                break;
            case R.id.ll_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.llEscortAuthentication:
                //押镖认证
//                Intent intent = new Intent(this, EscortAuthenticationActivity.class);
//                startActivity(intent);
                break;
            case R.id.llPersonageAuthentication:
                //获取认证状态
                getPersonageAuthentication(1);//个人认证
                break;
            case R.id.llEnterpriseAuthentication://企业认证
                getPersonageAuthentication(2);
                break;
            case R.id.personLayout:
                startActivityForResult(new Intent(this, UserInfoActivity.class), 123);
                break;

            case R.id.ll_yq_hy://邀请好友
                Intent intentYqhy = new Intent(this, VueActivity.class);
                intentYqhy.putExtra("url", VueUrl.inviteFriends);
                startActivity(intentYqhy);
                break;

            case R.id.ll_kefu://客服
                if (ChatClient.getInstance().isLoggedInBefore()) {
                 //   toChatActivity();
                } else { //未登录
                    //创建一个用户并登录环信服务器
                    initHuanXinToken();
                }
                break;
        }
    }


    private void initHuanXinToken() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
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
                         //   toChatActivity();
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

//    private void toChatActivity() {
//        Bundle bundle = new Bundle();
//        bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, 0);
//        Intent intent = new IntentBuilder(this)
//                .setTargetClass(ChatActivity.class)
//                .setVisitorInfo(DemoMessageHelper.createVisitorInfo())
//                .setServiceIMNumber(AppConfig.HX_IMNUMBER)
//                .setScheduleQueue(MessageHelper.createQueueIdentity("客服服务"))
//                .setShowUserNick(true)
//                .setBundle(bundle)
//                .setDefautName(defautName)
//                .build();
//         startActivity(intent);
//    }

    private void getPersonageAuthentication(int type) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
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
                                Intent intent = new Intent(UserInfoManagerActivity.this, PersonageAuthenticationActivity.class);
                                intent.putExtra("status", status);
                                startActivity(intent);
                            } else if (type == 2) {
                                Intent intent = new Intent(UserInfoManagerActivity.this, EnterpriseAuthenticationActivity.class);
                                intent.putExtra("status", status);
                                startActivity(intent);
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

}
