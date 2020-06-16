package com.escort.carriage.android.ui.activity.play;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.OrderListBean;
import com.escort.carriage.android.entity.bean.ResponcePayStatusBean;
import com.escort.carriage.android.entity.bean.UnionPayEntity;
import com.escort.carriage.android.entity.bean.play.ChargeMoneyEntity;
import com.escort.carriage.android.entity.bean.play.PlayMesFeesEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.play.ResponseChargeMoneyEntity;
import com.escort.carriage.android.entity.response.play.ResponseChargeMoneyPlayEntity;
import com.escort.carriage.android.entity.view.PlaySelectTypeItemEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.adapter.play.PlaySelectTypeItemAdapter;
import com.escort.carriage.android.ui.adapter.play.SendGodosDialogAdapter;
import com.escort.carriage.android.ui.view.list.FillListView;
import com.escort.carriage.android.utils.UnionPayUtil;
import com.google.gson.Gson;
import com.tripartitelib.android.alipay.AlipayUtils;
import com.tripartitelib.android.wechat.WechatUtils;
import com.tripartitelib.android.wechat.bean.WXResponseMembers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 充值特惠
 */
public class ChargeMoneyActivity extends ProjectBaseEditActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.list)
    FillListView filllist;
    @BindView(R.id.tvPreferenceMoney)
    TextView tvPreferenceMoney;
    @BindView(R.id.edtInputMoney)
    EditText edtInputMoney;

    private List<ChargeMoneyEntity> arrList;//特惠充值 选择使用类型
    private ChargeMoneyEntity chargeMoneyEntity;//当前选择的类型
    private String defaultStr;//当前选择的类型 的名称
    private final int aliplayType = 11;//支付宝支付
    private final int wechatType = 10;//微信支付
    private final int quickType = 12;//云支付
    private  int payType = 0;//支付类型
    private String orderNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_money_layout);
        ButterKnife.bind(this);
        setPageActionBar();
        setList();
        getServiceList();
       // UnifyPayPlugin.getInstance(this).setListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(payType==11||payType == 10){ //支付宝 微信回调查询支付结果
            getPayStatus();
        }
    }

    private void getPayStatus() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        UnionPayEntity unionPayEntity  = new UnionPayEntity();
        unionPayEntity.setOrderNo(orderNumber);
        unionPayEntity.setBizType(0);
        unionPayEntity.setPayType(payType);
        requestEntity.setData(unionPayEntity);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.GETPAYSTATUS_URL, jsonString).execute(new MyStringCallback<ResponcePayStatusBean>() {
            @Override
            public void onResponse(ResponcePayStatusBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    int status = s.data.getPayStatus();
                    if(status>0){ //支付成功
                        if(payType==11){
                            ToastUtil.showToastString("支付宝支付成功");
                        }else if(payType == 10){
                            ToastUtil.showToastString("微信支付成功");
                        }
                        finishPage();
                    }else{ //支付失败 发起撤单
                           deletePayOrder();
                    }
                    ToastUtil.showToastString(s.message);
                }
            }

            @Override
            public Class<ResponcePayStatusBean> getClazz() {
                return ResponcePayStatusBean.class;
            }
        });
    }

    private void deletePayOrder() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        UnionPayEntity unionPayEntity    = new UnionPayEntity();
        unionPayEntity.setOrderNo(orderNumber);
        unionPayEntity.setBizType(0);
        unionPayEntity.setPayType(payType);
        requestEntity.setData(unionPayEntity);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.DELETEORDERPAY_URL, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null && s.success) {
                    ToastUtil.showToastString("支付已取消");
                }
            }
            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
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
        tvTitle.setText("特惠充值");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getServiceList() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.PAYRECORD_PREFERENTIALLIST, jsonString).execute(new MyStringCallback<ResponseChargeMoneyEntity>() {
            @Override
            public void onResponse(ResponseChargeMoneyEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        arrList = new ArrayList<>();
                        arrList.add(createItem());
                        if(s.data != null && s.data.size() > 0){
                            arrList.addAll(s.data);
                        }
                        tvPreferenceMoney.setText(arrList.get(0).title);
                        defaultStr = arrList.get(0).title;
                        chargeMoneyEntity = arrList.get(0);
                        edtInputMoney.setEnabled(true);
                        edtInputMoney.setText("");
                        edtInputMoney.setHint("请输入充值金额");
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseChargeMoneyEntity> getClazz() {
                return ResponseChargeMoneyEntity.class;
            }
        });
    }

    private ChargeMoneyEntity createItem() {
        ChargeMoneyEntity entity = new ChargeMoneyEntity();
        entity.title = "不参与优惠";
        entity.isInputType = true;
        return entity;
    }

    private void setList() {
        try {
            PlaySelectTypeItemAdapter playSelectTypeItemAdapter = new PlaySelectTypeItemAdapter(this);
            playSelectTypeItemAdapter.mList = getListData();
            //默认设置微信支付
            filllist.setTag(0);
            filllist.setAdapter(playSelectTypeItemAdapter);
            filllist.setOnItemClickListener(this);
        } catch (Exception e) {

        }
    }

    private List<PlaySelectTypeItemEntity> getListData() {
        List<PlaySelectTypeItemEntity> arr = new ArrayList<PlaySelectTypeItemEntity>();
        PlaySelectTypeItemEntity item001 = new PlaySelectTypeItemEntity();
        PlaySelectTypeItemEntity item002 = new PlaySelectTypeItemEntity();
        PlaySelectTypeItemEntity item003 = new PlaySelectTypeItemEntity();

        item001.setTitle("微信支付");
        item001.setTextNormal(R.color.color_cfcfcf);
        item001.setTextSelect(R.color.color_00bffe);
        item001.setImageResNormal(R.mipmap.img_login_wx);
        item001.setImageResSelect(R.mipmap.img_login_wx);
        item001.setBgNormal(R.drawable.bg_bx_cfcfcf_bj_10dp);
        item001.setBgSelect(R.drawable.bg_bx_00bffe_bj_10dp);
        item001.setNormalTypeImageRes(R.drawable.not_selected);
        item001.setSelectTypeImageRes(R.drawable.pitch_on);

        item002.setTitle("支付宝支付");
        item002.setTextNormal(R.color.color_cfcfcf);
        item002.setTextSelect(R.color.color_00bffe);
        item002.setImageResNormal(R.mipmap.zhifubao);
        item002.setImageResSelect(R.mipmap.zhifubao);
        item002.setBgNormal(R.drawable.bg_bx_cfcfcf_bj_10dp);
        item002.setBgSelect(R.drawable.bg_bx_00bffe_bj_10dp);
        item002.setNormalTypeImageRes(R.drawable.not_selected);
        item002.setSelectTypeImageRes(R.drawable.pitch_on);

        item003.setTitle("云闪付支付");
        item003.setTextNormal(R.color.color_cfcfcf);
        item003.setTextSelect(R.color.color_00bffe);
        item003.setImageResNormal(R.mipmap.quick_pay_ic);
        item003.setImageResSelect(R.mipmap.quick_pay_ic);
        item003.setBgNormal(R.drawable.bg_bx_cfcfcf_bj_10dp);
        item003.setBgSelect(R.drawable.bg_bx_00bffe_bj_10dp);
        item003.setNormalTypeImageRes(R.drawable.not_selected);
        item003.setSelectTypeImageRes(R.drawable.pitch_on);
        arr.add(item003);
        arr.add(item001);
        arr.add(item002);
        return arr;
    }

    @OnClick(R.id.tvToPlay)
    public void onViewClicked() {
        PlaySelectTypeItemAdapter adapter = (PlaySelectTypeItemAdapter) (filllist.getAdapter());
        int selectPosition = adapter.selectPosition;
        switch (selectPosition) {
            case 0://ORDER_ADD_EARNEST
                //云闪付
                double inputQuick = isInputNum();
                payType  = quickType;
                if(inputQuick > 0){
                    toService(quickType, inputQuick);
                }
                break;
            case 1:
                //微信
                double inputNum = isInputNum();
                payType  = wechatType;
                if(inputNum > 0){
                    toService(wechatType, inputNum);
                }
                break;
            case 2:
                //支付宝
                double inputNumAli = isInputNum();
                payType  = aliplayType;
                if(inputNumAli > 0){
                    toService(aliplayType, inputNumAli);
                }
                break;
        }
    }

    private double isInputNum() {
        double flag = -1;
        String intupText = edtInputMoney.getText().toString();
        if(TextUtils.isEmpty(intupText)){
            ToastUtil.showToastString("请输入充值金额");
        }
        try {
            double aDouble = Double.valueOf(intupText);
            if(aDouble <= 0){
                ToastUtil.showToastString("请输入合法金额");
            } else {
                flag = aDouble;
            }
        }catch (Exception e){
            ToastUtil.showToastString("请输入合法金额");
        }
        return flag;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaySelectTypeItemAdapter adapter = (PlaySelectTypeItemAdapter) (parent.getAdapter());
        adapter.setSelectPosition(position);
    }

    @OnClick({R.id.tvPreferenceMoney, R.id.tvPreferenceImg, R.id.tvToPlay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPreferenceMoney:
            case R.id.tvPreferenceImg:
                if(arrList == null || arrList.size() < 1){
                    ToastUtil.showToastString("无可用优惠");
                } else {
                    showDropDialog(arrList);
                }
                break;
            case R.id.tvToPlay:
                break;
        }
    }

    AlertDialog alertDialog;

    private void showDropDialog(final List<ChargeMoneyEntity> objs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_sendgoods_rv, null);
        RecyclerView rv = view.findViewById(R.id.rv_item);
        rv.setLayoutManager(new LinearLayoutManager(this));
        final SendGodosDialogAdapter sendGodosDialogAdapter = new SendGodosDialogAdapter(objs, defaultStr);
        sendGodosDialogAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChargeMoneyEntity entity = arrList.get(position);
                if(entity.isInputType){
                    //输入框可以数据
                    edtInputMoney.setEnabled(true);
                    edtInputMoney.setText("");
                    edtInputMoney.setHint("请输入充值金额");
                } else {
                    //不可输入
                    edtInputMoney.setEnabled(false);
                    edtInputMoney.setText(String.valueOf(entity.payAmout));
                    edtInputMoney.setHint("");
                }
                tvPreferenceMoney.setText(entity.title);
                defaultStr = entity.title;
                chargeMoneyEntity = entity;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                    alertDialog = null;
                }
            }
        });
        rv.setAdapter(sendGodosDialogAdapter);
        builder.setView(view);
        alertDialog = builder.show();
    }

    private void toService(int type, double money){
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("payWay", type);
        if(chargeMoneyEntity != null && !TextUtils.isEmpty(chargeMoneyEntity.id)){
            hashMap.put("rechargePreferentialId", chargeMoneyEntity.id);
        }
        if(money > 0){
            hashMap.put("money", money);
        }
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.PAYRECORD_RECHARGE, jsonString).execute(new MyStringCallback<ResponseChargeMoneyPlayEntity>() {
            @Override
            public void onResponse(ResponseChargeMoneyPlayEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        UnionPayUtil payUtil =  UnionPayUtil.getUnionPayUtil(ChargeMoneyActivity.this);
                        orderNumber  = s.data.orderNo;
                        try {
                            String json = new Gson().toJson(s.data);
                            JSONObject object = new JSONObject(json);
                            String tn_code  = object.getString("payParam");
                            JSONObject object1 = new JSONObject(tn_code);
                            JSONObject obj   = object1.getJSONObject("appPayRequest");
//                            if(type==10){ //微信
//                                payUtil.payWX(obj.toString());
//                            }else if(type==11){ //支付宝
//                                payUtil.payAliPay(obj.toString());
//                            }else if(type == 12){
//                                payUtil.payCloudQuickPay(obj.toString());
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseChargeMoneyPlayEntity> getClazz() {
                return ResponseChargeMoneyPlayEntity.class;
            }
        });
    }

    private void finishPage(){
      //  UnifyPayPlugin.getInstance(this).clean();
        setResult(456);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ddebug","---onActivityResult---");
        /**
         * 处理银联云闪付手机支付控件返回的支付结果
         */
        if (data == null) {
            return;
        }
        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            //如果想对结果数据校验确认，直接去商户后台查询交易结果，
            //校验支付结果需要用到的参数有sign、data、mode(测试或生产)，sign和data可以在result_data获取到
            msg = "云闪付支付成功";
            finishPage();
        } else if (str.equalsIgnoreCase("fail")) {
            deletePayOrder();
            msg = "云闪付支付失败";
        } else if (str.equalsIgnoreCase("cancel")) {
            deletePayOrder();
            msg = "取消云闪付支付";
        }
        ToastUtil.showToastString(msg);
    }

//    @Override
//    public void onResult(String s, String s1) {
//        try {
//            JSONObject json = new JSONObject(s1);
//            String result  = json.getString("resultMsg");
//            ToastUtil.showToastString(result);
//            finishPage();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
}
