package com.escort.carriage.android.ui.view.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.bumptech.glide.Glide;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.ResponseIntegerBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.activity.mes.HistoryOrderListActivity;
import com.escort.carriage.android.ui.activity.mes.MyOrderListActivity;
import com.escort.carriage.android.ui.activity.my.EnterpriseAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.EscortAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.MyBidActivity;
import com.escort.carriage.android.ui.activity.my.PersonageAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.SettingActivity;
import com.escort.carriage.android.ui.activity.my.UserInfoActivity;
import com.escort.carriage.android.ui.activity.play.WalletMenuActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import com.escort.carriage.android.ui.view.imgview.RoundImageView;

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
    private View viewGroup;
    private HomeActivity activity;
    private Unbinder bind;

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
    }

    public void updataUserInfo(UserInfoEntity userInfoEntity) {
        tvName.setText(userInfoEntity.getNickName());
        tvUseDay.setText("使用小二押镖" + userInfoEntity.days + "天");
        GlideManager.getGlideManager().loadImageRoundFitCrop(userInfoEntity.headPictureUrl, iv_head_img, 100, R.mipmap.img_user_head_img_default);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_img:
                activity.startActivityForResult(new Intent(activity, UserInfoActivity.class), 123);
                break;
            case R.id.myBid:
                activity.startActivity(new Intent(activity, MyBidActivity.class));
                break;
            case R.id.myOrderList:
                activity.startActivity(new Intent(activity, MyOrderListActivity.class));
                break;
            case R.id.ll_history_group:
                activity.startActivity(new Intent(activity, HistoryOrderListActivity.class));
                break;
            case R.id.ll_my_money:
                activity.startActivity(new Intent(activity, WalletMenuActivity.class));
                break;

            case R.id.ll_setting:
                activity.startActivity(new Intent(activity, SettingActivity.class));
                break;
            case R.id.llEscortAuthentication:
                //押镖认证
                Intent intent = new Intent(activity, EscortAuthenticationActivity.class);
                activity.startActivity(intent);
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


        }
    }

    /**
     * 获取个人认证状态   1 个人认证 2 企业认证 3 司机认证 4 推广员
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
