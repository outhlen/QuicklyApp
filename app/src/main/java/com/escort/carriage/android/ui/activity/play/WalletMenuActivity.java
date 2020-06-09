package com.escort.carriage.android.ui.activity.play;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.chinaums.pppay.unify.UnifyPayListener;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.bean.ReponseDictMenuEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.play.ResponseWalletMenuEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.web.VueActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-26 16:38
 */
public class WalletMenuActivity extends ProjectBaseActivity  {

    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.wallet_menu_recharge_group)
    LinearLayout walllayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_menu);
        ButterKnife.bind(this);
        setPageActionBar();
        getUserMoney();
        getDictnary();
    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("我的钱包");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getDictnary() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> data = new HashMap<>();
        data.put("param","rechargeSwitch");
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.QUERY_DICT_INFO, jsonString).execute(new MyStringCallback<ReponseDictMenuEntity>() {
            @Override
            public void onResponse(ReponseDictMenuEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if(s.data!=null){
                        String isExit  =  s.data.toString();
                        if(isExit.equals("false")){
                            walllayout.setVisibility(View.GONE);
                        }else{
                            walllayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public Class<ReponseDictMenuEntity> getClazz() {
                return ReponseDictMenuEntity.class;
            }
        });

    }


    /**
     * 提交认证信息
     */
    private void getUserMoney() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ASSETS_PAYUSER_GETUSERASSETS, jsonString).execute(new MyStringCallback<ResponseWalletMenuEntity>() {
            @Override
            public void onResponse(ResponseWalletMenuEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success && s.data != null) {
                        tvMoney.setText(String.valueOf(new BigDecimal((s.data.remaining + s.data.remainingFrozen + s.data.remainingDonation)).setScale(2,BigDecimal.ROUND_HALF_UP)));
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseWalletMenuEntity> getClazz() {
                return ResponseWalletMenuEntity.class;
            }
        });
    }

    @OnClick({R.id.llBankNumGroup, R.id.wallet_menu_coupon_group, R.id.wallet_menu_recharge_group,
            R.id.iv_wallet_menu_discounts, R.id.wallet_menu_purse, R.id.wallet_menu_serve,
            R.id.wallet_menu_car, R.id.wallet_menu_bonus, R.id.wallet_menu_performance, R.id.llMyMoneyGroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llMyMoneyGroup://我的资产
                Intent intentWalletAssets = new Intent(this, VueActivity.class);
                intentWalletAssets.putExtra("url", VueUrl.theWalletAssets);
                startActivity(intentWalletAssets);
                break;
            case R.id.llBankNumGroup://银行卡
                Intent intentBankNum = new Intent(this, VueActivity.class);
                intentBankNum.putExtra("url", VueUrl.mybankcard);
                startActivity(intentBankNum);
                break;

            case R.id.wallet_menu_coupon_group://优惠卷
                Intent intentCoupon = new Intent(this, VueActivity.class);
                intentCoupon.putExtra("url", VueUrl.MyCoupon);
                startActivity(intentCoupon);
                break;
            case R.id.wallet_menu_recharge_group:
                //特惠充值
                Intent intent = new Intent(this, ChargeMoneyActivity.class);
                startActivityForResult(intent, 123);
                break;
            case R.id.iv_wallet_menu_discounts://中间优惠卷
                Intent intentDiscounts = new Intent(this, VueActivity.class);
                intentDiscounts.putExtra("url", VueUrl.getCoupons);
                startActivity(intentDiscounts);
                break;
            case R.id.wallet_menu_purse://押金记录
                Intent intentPurse = new Intent(this, VueActivity.class);
                intentPurse.putExtra("url", VueUrl.depositRecord);
                startActivity(intentPurse);
                break;
            case R.id.wallet_menu_serve://信息服务记录
                Intent intentServe = new Intent(this, VueActivity.class);
                intentServe.putExtra("url", VueUrl.informationServiceFee);
                startActivity(intentServe);
                break;
            case R.id.wallet_menu_car://运费记录
                Intent intentCar = new Intent(this, VueActivity.class);
                intentCar.putExtra("url", VueUrl.freightRecord);
                startActivity(intentCar);
                break;
            case R.id.wallet_menu_bonus://奖金记录
                Intent intentBonus = new Intent(this, VueActivity.class);
                intentBonus.putExtra("url", VueUrl.userRewardRecord);
                startActivity(intentBonus);
                break;
            case R.id.wallet_menu_performance://业绩记录
                Intent intentPerformance = new Intent(this, VueActivity.class);
                intentPerformance.putExtra("url", VueUrl.userAchivementRecord);
                startActivity(intentPerformance);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123){
            getUserMoney();
        }
    }

}
