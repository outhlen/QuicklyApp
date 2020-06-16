package com.escort.carriage.android.ui.view.popu;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
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

import java.util.Date;
import java.util.HashMap;

/**
 * 竞价 弹框
 */
public class BiddingPopupWindow extends PopupWindow implements View.OnClickListener {


    private View mMenuView;
    private Context context;
    public Date date;
    private String orderNumber;
    private Switch mSwitch;
    boolean isChecked;
    EditText inputTv;
    LinearLayout linearLayout;

    public BiddingPopupWindow(Activity context, String orderNumber) {
        super(context);
        this.context = context;
        this.orderNumber = orderNumber;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_bidding, null);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
//        CacheDataEntity cacheDataEntity = CacheDBMolder.getInstance().getCacheDataEntity(null);
//        int phoneHeight = cacheDataEntity.getPhoneHeight();
//        int statusBarHeight = StatusBarUtil.getStatusBarHeight(context);
//        int height = phoneHeight + statusBarHeight;
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimRight);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x60000000);
//        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //这句话，让pop覆盖在输入法上面
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //这句话，让pop自适应输入状态
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mMenuView.setOnTouchListener(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isChecked = true;
                    inputTv.setEnabled(true);
                } else {
                    isChecked = false;
                    inputTv.setText("");
                    inputTv.setEnabled(false);
                }
            }
        });
        setView();

    }



    private void setView() {
        mSwitch = mMenuView.findViewById(R.id.switch_btn);
        inputTv = mMenuView.findViewById(R.id.money_et);
        linearLayout = mMenuView.findViewById(R.id.linearlayout);
        mMenuView.findViewById(R.id.ivClose).setOnClickListener(this);
        mMenuView.findViewById(R.id.tvEndSite).setOnClickListener(this);
        mMenuView.findViewById(R.id.btnNext).setOnClickListener(this);
    }

    private void initTimePicker() {

        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                BiddingPopupWindow.this.date = date;
                TextView tvTime = mMenuView.findViewById(R.id.tvEndSite);
                tvTime.setText(ProjectDateUtils.transformationDate(date, "yyyy/MM/dd"));
            }
        }).build();
        pvTime.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                dismiss();
                break;
            case R.id.tvEndSite:
                initTimePicker();
                break;
            case R.id.btnNext:
                EditText edtView = mMenuView.findViewById(R.id.inputText);
                String inputStr = edtView.getText().toString();
                String priceStr = inputTv.getText().toString();
                if (TextUtils.isEmpty(inputStr)) {
                    ToastUtil.showToastString("请填写金额");
                    return;
                }
                if (isChecked == true) {
                    if (TextUtils.isEmpty(priceStr)) {
                        ToastUtil.showToastString("请填写正确押金金额");
                        return;
                    }
                }
                if (date == null) {
                    ToastUtil.showToastString("请选择送达时间");
                    return;
                }
                toService(inputStr, priceStr);
                break;

        }
    }

    private void toService(String money,String price) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(context, "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> data = new HashMap<>();
        data.put("orderNumber", orderNumber);
        data.put("money", money);
        data.put("deposit", price);
        data.put("toTime", date.getTime());
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDERVEHICLE_ADD_VEHICLE, jsonString).execute(new MyStringCallback<ResponseUserEntity>() {
            @Override
            public void onResponse(ResponseUserEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
//                        ToastUtil.showToastString("竞价成功");
//                        OrderInfoActivity context = (OrderInfoActivity) BiddingPopupWindow.this.context;
//                        context.biddingFinishpage();
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
}