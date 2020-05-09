package com.escort.carriage.android.ui.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.verification.VerificationUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.view.text.CheckSmsFourCodeView;
import com.escort.carriage.android.utils.AmapUtils;

import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-25 17:35
 */
public class TransfeOrderActivity extends ProjectBaseActivity {
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tv_pwd_title)
    TextView tvGetCheckCode;
    @BindView(R.id.checkSmsCode)
    CheckSmsFourCodeView checkSmsCode;
    @BindView(R.id.btn_commit_evalute)
    Button btnCommitEvalute;
    @BindView(R.id.etCarNum)
    EditText etCarNum;
    private String orderNumber;
    private String code;

    private double longitude;//经度
    private double latitude;//纬度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfe_order_layout);
        ButterKnife.bind(this);
        orderNumber = getIntent().getStringExtra("orderNumber");
        setPageActionBar();
        getLocation();
        checkSmsCode.setOnInputListener(new CheckSmsFourCodeView.OnInputListener() {

            @Override
            public void onInput(String str) {
                code = str;
            }
        });

    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("中转验证");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.tv_pwd_title, R.id.btn_commit_evalute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pwd_title:
                getSmsCode();
                break;
            case R.id.btn_commit_evalute:
                String phone = etPhone.getText().toString();
                String carNum = etCarNum.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToastString("请输入手机号");
                } else if (TextUtils.isEmpty(carNum)) {
                    ToastUtil.showToastString("请输入对方司机车牌号");
                } else if (!VerificationUtil.verificationText(phone, VerificationUtil.MOBILE_NUM)) {
                    ToastUtil.showToastString("请输入正确的手机号");
                } else if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToastString("请输入验证码");
                } else {
                    toSubmitDataInfo(phone, carNum);
                }
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getSmsCode() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToastString("请输入手机号");
        } else if (!VerificationUtil.verificationText(phone, VerificationUtil.MOBILE_NUM)) {
            ToastUtil.showToastString("请输入正确的手机号");
        } else {
            UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
            RequestEntity requestEntity = new RequestEntity(0);
            requestEntity.setData(phone);
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.ORDERVEHICLE_SENDSMSAPPLYTURN, jsonString).execute(new MyStringCallback<ResponceBean>() {
                @Override
                public void onResponse(ResponceBean s) {
                    UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                    if (s != null) {
                        if (s.success) {
                            ToastUtil.showToastString("发送成功");
                            delayed();
                        } else {
                            ToastUtil.showToastString(s.message);
                        }
                    }
                }

                @Override
                public Class<ResponceBean> getClazz() {
                    return ResponceBean.class;
                }
            });
        }
    }

    /**
     * 获取经纬度
     */
    private void getLocation() {
        AmapUtils.getAmapUtils().getLocation(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        latitude = aMapLocation.getLatitude();
                        longitude = aMapLocation.getLongitude();
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtils.showI("MainActivity", "AmapError   location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
    }

    /**
     * 延时验证码
     */
    private void delayed() {
        //一分钟以后重试
        tvGetCheckCode.setClickable(false);//把TV设置为不可点击
        long time = System.currentTimeMillis() / 1000;
        tvGetCheckCode.setText(time + "秒后重试");
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                if (tvGetCheckCode != null) {
                    tvGetCheckCode.setText(time + "秒后重试");
                }
            }

            @Override
            public void onFinish() {
                if (tvGetCheckCode != null) {
                    tvGetCheckCode.setClickable(true);//把TV设置为可点击
                    tvGetCheckCode.setText("获取验证码");
                }
            }
        }.start();

    }


    private void finishPage() {
        setResult(456);
        finish();
    }

    /**
     * 请求数据
     *
     * @param phone
     */
    private void toSubmitDataInfo(String phone, String license) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("id", "1");
        hashMap.put("phone", phone);
        hashMap.put("code", code);
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("longitude", longitude);
        if(longitude != 0){
            hashMap.put("longitude", longitude);
        }
        if(latitude != 0){
            hashMap.put("latitude", latitude);
        }
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDERVEHICLE_ADD_EARNESTTURN, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        ToastUtil.showToastString("中转成功");
                        finishPage();
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }
}
