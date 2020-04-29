package com.escort.carriage.android.ui.activity.mes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.home.SelectOrderEntity;
import com.escort.carriage.android.entity.request.home.HomeListRequestEnity;
import com.escort.carriage.android.ui.view.seekbar.DoubleHeadedDragonBar;
import com.escort.carriage.android.ui.view.text.DrawableTextView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectOrderActivity extends Activity {

    @BindView(R.id.edtGoodName)
    EditText edtGoodName;
    @BindView(R.id.seekbarHwzl)
    DoubleHeadedDragonBar seekbarHwzl;
    @BindView(R.id.seekbarHwtj)
    DoubleHeadedDragonBar seekbarHwtj;
    @BindView(R.id.grdioGroup)
    RadioGroup grdioGroup;
    @BindView(R.id.startTime)
    TextView startTime;
    @BindView(R.id.endTime)
    TextView endTime;
    @BindView(R.id.tvReset)
    DrawableTextView tvReset;

    private Unbinder bind;
    @BindView(R.id.finishView)
    View finishView;

    private SelectOrderEntity selectOrderEntity;
    private int openType = 1;//1 首页列表   2：历史订单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_select_order);
        bind = ButterKnife.bind(this);
        setPageActionBar();
        openType = getIntent().getIntExtra("openType", 1);
        String json = getIntent().getStringExtra("json");
        if (!TextUtils.isEmpty(json)) {
            selectOrderEntity = JsonManager.getJsonBean(json, SelectOrderEntity.class);
        }

        initPageData();
        setSeekabar();
        setListener();
    }

    private void setSeekabar() {


        seekbarHwzl.setCallBack(new DoubleHeadedDragonBar.DhdBarCallBack() {

            @Override
            public String getMinMaxString(int value, int value1) {
                selectOrderEntity.setStartWeight(String.valueOf(value));
                selectOrderEntity.setEndWeight(String.valueOf(value1));
                return value + "~" + value1;
            }


            @Override
            public String getMinString(int value) {
                selectOrderEntity.setStartWeight(String.valueOf(value));
                return super.getMinString(value);

            }

            @Override
            public String getMaxString(int value) {
                selectOrderEntity.setEndWeight(String.valueOf(value));
                return super.getMaxString(value);
            }
            @Override
            public void onEndTouch(float minPercentage, float maxPercentage) {
                seekbarHwzl.close();
            }
        });



        seekbarHwtj.setCallBack(new DoubleHeadedDragonBar.DhdBarCallBack() {

            @Override
            public String getMinMaxString(int value, int value1) {
                selectOrderEntity.setStartVolume(String.valueOf(value));
                selectOrderEntity.setEndVolume(String.valueOf(value1));
                return value + "~" + value1;
            }


            @Override
            public String getMinString(int value) {
                selectOrderEntity.setStartVolume(String.valueOf(value));
                return super.getMinString(value);

            }

            @Override
            public String getMaxString(int value) {
                selectOrderEntity.setEndVolume(String.valueOf(value));
                return super.getMaxString(value);
            }
            @Override
            public void onEndTouch(float minPercentage, float maxPercentage) {
                seekbarHwtj.close();
            }
        });

        seekbarHwzl.setToastView2(LayoutInflater.from(this).inflate(R.layout.toast_view, null));
        seekbarHwzl.setToastView(LayoutInflater.from(this).inflate(R.layout.toast_view, null));
        seekbarHwzl.setToastView1(LayoutInflater.from(this).inflate(R.layout.toast_view, null));
        seekbarHwzl.setIsHintView(true);
        seekbarHwzl.hintShowView();

        seekbarHwtj.setToastView2(LayoutInflater.from(this).inflate(R.layout.toast_view, null));
        seekbarHwtj.setToastView(LayoutInflater.from(this).inflate(R.layout.toast_view, null));
        seekbarHwtj.setToastView1(LayoutInflater.from(this).inflate(R.layout.toast_view, null));
        seekbarHwzl.setIsHintView(true);
        seekbarHwtj.hintShowView();


    }

    private void initPageData() {
        initCargoName();
        initTime();
        initSeekBar();
        initRadioGroup();
    }

    private void initRadioGroup() {

        if(TextUtils.equals("0", selectOrderEntity.orderType)){
            grdioGroup.check(R.id.radioButton1);
        } else if(TextUtils.equals("1", selectOrderEntity.orderType)){
            grdioGroup.check(R.id.radioButton2);
        } else if(TextUtils.equals("2", selectOrderEntity.orderType)){
            grdioGroup.check(R.id.radioButton3);
        }


        grdioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        selectOrderEntity.orderType = "0";
                        break;
                    case R.id.radioButton2:
                        selectOrderEntity.orderType = "1";
                        break;
                    case R.id.radioButton3:
                        selectOrderEntity.orderType = "2";
                        break;

                }
            }
        });
    }

    private void initSeekBar() {
        String zlMinNum = selectOrderEntity.startWeight;
        String zlMaxNum = selectOrderEntity.endWeight;

        if (TextUtils.isEmpty(zlMinNum)) {
            seekbarHwzl.setMinValue(0);
        } else {
            seekbarHwzl.setMinValue(Integer.valueOf(zlMinNum));
        }
        if (TextUtils.isEmpty(zlMaxNum)) {
            seekbarHwzl.setMaxValue(100);
        } else {
            seekbarHwzl.setMaxValue(Integer.valueOf(zlMaxNum));
        }

        String tjMinNum = selectOrderEntity.startVolume;
        String tjMaxNum = selectOrderEntity.endVolume;

        if (TextUtils.isEmpty(tjMinNum)) {
            seekbarHwtj.setMinValue(0);
        } else {
            seekbarHwtj.setMinValue(Integer.valueOf(tjMinNum));
        }
        if (TextUtils.isEmpty(tjMaxNum)) {
            seekbarHwtj.setMaxValue(100);
        } else {
            seekbarHwtj.setMaxValue(Integer.valueOf(tjMaxNum));
        }

    }

    private void initCargoName() {
        String cargoName = selectOrderEntity.getCargoName();
        edtGoodName.setText(cargoName);
    }

    /**
     * 初始化时间
     */
    private void initTime() {
        if (!TextUtils.isEmpty(selectOrderEntity.orderPlaceTime)) {
            String startTimeStr = ProjectDateUtils.transformationDate(selectOrderEntity.orderPlaceTime, "yyyy/MM/dd");
            startTime.setText(startTimeStr);
        } else {
            Date endDate = new Date();
            Date starDate = ProjectDateUtils.getDateBefore(endDate, 7);
            setStartTimeText(starDate);
        }

        if (!TextUtils.isEmpty(selectOrderEntity.endDate)) {
            String endTimeStr = ProjectDateUtils.transformationDate(selectOrderEntity.endDate, "yyyy/MM/dd");
            endTime.setText(endTimeStr);
        } else {
            Date endDate = new Date();
            setEndTimeText(endDate);
        }


    }

    private void setListener() {

    }

    private void setPageActionBar() {
        //获取顶部状态栏的高度 给对应View设置高度
        View statusBar = findViewById(R.id.statusBarView);
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        statusBar.setLayoutParams(layoutParams);

    }

    private void initTimePicker(final int i) {

        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (i == 1) {
                    setStartTimeText(date);

                } else if (i == 2) {
                    setEndTimeText(date);

                }
            }


        }).build();
        pvTime.show();
    }

    private void setStartTimeText(Date date) {
        startTime.setText(ProjectDateUtils.transformationDate(date, "yyyy/MM/dd"));
        selectOrderEntity.orderPlaceTime = ProjectDateUtils.transformationDate(date, "yyyy-MM-dd") + " 00:00:00";
    }

    private void setEndTimeText(Date date) {
        endTime.setText(ProjectDateUtils.transformationDate(date, "yyyy/MM/dd"));
        selectOrderEntity.endDate = ProjectDateUtils.transformationDate(date, "yyyy-MM-dd") + " 23:59:59";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
    }

    @OnClick({R.id.startTime, R.id.endTime, R.id.tvSelect, R.id.finishView, R.id.tvReset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.startTime:
                initTimePicker(1);
                break;
            case R.id.endTime:
                initTimePicker(2);
                break;
            case R.id.tvSelect:
                selectOrderEntity.setCargoName(edtGoodName.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("json", JsonManager.createJsonString(selectOrderEntity));
                setResult(666, intent);
                finish();
                break;
            case R.id.tvReset:
                //重置数据
                Intent intentReset = new Intent();
                intentReset.putExtra("json", JsonManager.createJsonString(new SelectOrderEntity()));
                setResult(665, intentReset);

                finish();
                break;
            case R.id.finishView:
                finish();
                break;

        }
    }
}
