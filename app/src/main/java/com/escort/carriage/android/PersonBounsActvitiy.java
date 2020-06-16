package com.escort.carriage.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.play.ResponseWalletMenuEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.play.ChargeMoneyActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonBounsActvitiy extends ProjectBaseActivity {

    @BindView(R.id.wx_add)
    LinearLayout wxLogin;
    @BindView(R.id.wx_tx)
    LinearLayout wxShare;
    @BindView(R.id.wx_quan)
    LinearLayout wxChat;
    @BindView(R.id.wx_jifen)
    LinearLayout jifen;
    @BindView(R.id.wx_mingxi)
    LinearLayout mingxi;
    @BindView(R.id.wx_yee)
    LinearLayout yue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_bouns);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.wx_add, R.id.wx_tx, R.id.wx_quan,R.id.wx_jifen,R.id.wx_mingxi,R.id.wx_yee})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wx_add:
                Intent intent = new Intent(this, ChargeMoneyActivity.class);
                startActivityForResult(intent, 123);
                break;
            case R.id.wx_tx://分享
                Intent intentWalletAssets = new Intent(this, VueActivity.class);
                intentWalletAssets.putExtra("url", VueUrl.theWalletAssets);
                startActivity(intentWalletAssets);
                break;
            case R.id.wx_quan://朋友圈
                Intent intentCoupon = new Intent(this, VueActivity.class);
                intentCoupon.putExtra("url", VueUrl.MyCoupon);
                startActivity(intentCoupon);
                break;
            case R.id.wx_jifen://朋友圈
                Intent jifenIntent = new Intent(this, VueActivity.class);
                jifenIntent.putExtra("url", VueUrl.theWalletAssets);
                startActivity(jifenIntent);
                break;
            case R.id.wx_mingxi://朋友圈
                Intent  mingxiIntent = new Intent(this, VueActivity.class);
                mingxiIntent.putExtra("url", VueUrl.theWalletAssets);
                startActivity(mingxiIntent);
                break;
            case R.id.wx_yee://朋友圈
                Intent yueIntent = new Intent(this, VueActivity.class);
                yueIntent.putExtra("url", VueUrl.theWalletAssets);
                startActivity(yueIntent);
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
                        //tvMoney.setText(String.valueOf(new BigDecimal((s.data.remaining + s.data.remainingFrozen + s.data.remainingDonation)).setScale(2,BigDecimal.ROUND_HALF_UP)));
                       // ToastUtil.showToastString("支付成功");
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

}
