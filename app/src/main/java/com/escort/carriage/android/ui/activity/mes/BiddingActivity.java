package com.escort.carriage.android.ui.activity.mes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.view.popu.BiddingPopupWindow;
import com.escort.carriage.android.ui.view.text.AutofitTextView;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 竞标 页面
 */
public class BiddingActivity extends Activity {
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.inputText)
    EditText inputText;
    @BindView(R.id.tvEndSite)
    TextView tvEndSite;
    @BindView(R.id.llEndSiteGroup)
    LinearLayout llEndSiteGroup;
    @BindView(R.id.btnNext)
    TextView btnNext;
    @BindView(R.id.tvPageShowText)
    AutofitTextView tvPageShowText;
    @BindView(R.id.switch_btn)
    Switch switchBtn;
    @BindView(R.id.money_et)
    EditText moneyEt;

    private Unbinder bind;
    public Date date;
    private String orderNumber;
    private String intention;

    boolean isPay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.pop_bidding);
        bind = ButterKnife.bind(this);
        orderNumber = getIntent().getStringExtra("orderNumber");
        intention = getIntent().getStringExtra("intention");
        tvPageShowText.setText("投标提示:货主意向" + intention + "优先");

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isPay = true;
                    moneyEt.setEnabled(true);
                } else {
                    isPay = false;
                    moneyEt.setEnabled(false);
                    ToastUtil.showToastString("已选不需要支付押金");
                }
            }
        });
    }

    @OnClick({R.id.ivClose, R.id.tvEndSite, R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                finish();
                break;
            case R.id.tvEndSite:
                initTimePicker();
                break;
            case R.id.btnNext:
                double money = 0;
                double price = 0;
                String inputStr = inputText.getText().toString();
                String priceStr = moneyEt.getText().toString();

                if (date == null) {
                    ToastUtil.showToastString("请选择送达时间");
                    return;
                }
                if (TextUtils.isEmpty(inputStr)) {
                    ToastUtil.showToastString("请填写金额");
                    return;
                }
                if (isPay) {
                    if (TextUtils.isEmpty(priceStr)) {
                        ToastUtil.showToastString("请填写押金金额");
                        return;
                    }
                }
                try {
                    money = Double.valueOf(inputStr);
                    toService(money, priceStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void initTimePicker() {

        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Date currentDate = ProjectDateUtils.getCurrentDate();

                //将日期换算成秒
                if (currentDate.getTime() > date.getTime()) {
                    ToastUtil.showToastString("选择日期不得早于当前时间");
                } else {
                    BiddingActivity.this.date = date;
                    tvEndSite.setText(ProjectDateUtils.transformationDate(date, "yyyy/MM/dd"));
                }

            }


        }).build();
        pvTime.show();
    }


    private void toService(double money, String price) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> data = new HashMap<>();
        data.put("orderNumber", orderNumber);
        data.put("money", money);
        if (TextUtils.isEmpty(price)) {
            data.put("deposit", null);
        }else {
            data.put("deposit", Double.valueOf(price));
        }

        data.put("toTime", date.getTime());
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDERVEHICLE_ADD_VEHICLE, jsonString).execute(new MyStringCallback<ResponseUserEntity>() {
            @Override
            public void onResponse(ResponseUserEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        ToastUtil.showToastString("竞价投出");
                        setResult(666);
                        finish();
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseUserEntity> getClazz() {
                return ResponseUserEntity.class;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
