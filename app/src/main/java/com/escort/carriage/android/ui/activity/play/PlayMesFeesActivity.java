package com.escort.carriage.android.ui.activity.play;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.ResponcePayStatusBean;
import com.escort.carriage.android.entity.bean.UnionPayEntity;
import com.escort.carriage.android.entity.bean.play.PlayMesFeesEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.play.ResponsePlayMesFeesEntity;
import com.escort.carriage.android.entity.view.PlaySelectTypeItemEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.adapter.home.MyOrderListAdapter;
import com.escort.carriage.android.ui.adapter.play.PlaySelectTypeItemAdapter;
import com.escort.carriage.android.ui.view.list.FillListView;
import com.escort.carriage.android.utils.Sh256;
import com.escort.carriage.android.utils.UnionPayUtil;
import com.lzj.pass.dialog.PayPassDialog;
import com.lzj.pass.dialog.PayPassView;
import com.tripartitelib.android.alipay.AlipayUtils;
import com.tripartitelib.android.wechat.WechatUtils;
import com.tripartitelib.android.wechat.bean.WXResponseMembers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 信息缴费 页面
 *
 * 0 微信JSAPI支付（公众号/小程序）,
 * 1 微信H5支付,
 * 2 微信App支付,
 * 3 微信NATIVE支付,
 * 4 支付宝手机端H5支付,
 * 5 通联支付，
 * 6 余额支付，
 * 7 支付宝PC端H5支付，
 * 8 支付宝App端支付
 */
public class PlayMesFeesActivity extends ProjectBaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvYjMoney)
    TextView tvYjMoney;
    @BindView(R.id.tvBxMoney)
    TextView tvBxMoney;
    @BindView(R.id.tvFpMoney)
    TextView tvFpMoney;
    @BindView(R.id.list)
    FillListView filllist;
    @BindView(R.id.tvToPlay)
    TextView tvToPlay;

    public String money = "0";
    public String deposit;
    //保险费
    public String innsuranceFee;
    //发票金额
    public String invoiceFee;
    public String orderNumber;

    private final int balanceType = 6;//余额支付
    private final int wechatType = 10;//微信支付
    private final int aliplayType =11;//支付宝支付
    private final int quickType = 12;//支付支付

    private  int payType = 0;//支付类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mes_fees);
        ButterKnife.bind(this);
        orderNumber = getIntent().getStringExtra("orderNumber");
        money = getIntent().getStringExtra("money");
        deposit = getIntent().getStringExtra("deposit");
        innsuranceFee = getIntent().getStringExtra("innsuranceFee");
        invoiceFee = getIntent().getStringExtra("invoiceFee");
        if(TextUtils.isEmpty(money)){
            money = "0.00";
        }
        if(TextUtils.isEmpty(deposit)){
            deposit = "0.00";
        }

        if(TextUtils.isEmpty(innsuranceFee)){
            innsuranceFee = "0.00";
        }

        if(TextUtils.isEmpty(invoiceFee)){
            invoiceFee = "0.00";
        }
        setPageActionBar();
        setList();
        setTextMoney();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(payType==11||payType==10){ //支付宝回调查询支付结果
            getPayStatus();
        }
    }

    private void getPayStatus() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        UnionPayEntity unionPayEntity  = new UnionPayEntity();
        unionPayEntity.setOrderNo(orderNumber);
        unionPayEntity.setBizType(3);
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
        UnionPayEntity unionPayEntity  = new UnionPayEntity();
        unionPayEntity.setOrderNo(orderNumber);
        unionPayEntity.setBizType(3);
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

    private void setTextMoney() {
        SpannableString spannableString = new SpannableString("￥" + money);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
        spannableString.setSpan(sizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvMoney.setText(spannableString);
        SpannableString spannableStringTwo = new SpannableString("￥" + deposit);
        RelativeSizeSpan sizeSpanTwo = new RelativeSizeSpan(0.5f);
        spannableStringTwo.setSpan(sizeSpanTwo, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvYjMoney.setText(spannableStringTwo);

        SpannableString spannableStringThree = new SpannableString("￥" + innsuranceFee);
        RelativeSizeSpan sizeSpanThree = new RelativeSizeSpan(0.5f);
        spannableStringThree.setSpan(sizeSpanThree, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvBxMoney.setText(spannableStringThree);

        SpannableString spannableStringFour = new SpannableString("￥" + invoiceFee);
        RelativeSizeSpan sizeSpanFour = new RelativeSizeSpan(0.5f);
        spannableStringFour.setSpan(sizeSpanFour, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvFpMoney.setText(spannableStringFour);
    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("信息缴费");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        PlaySelectTypeItemEntity item004 = new PlaySelectTypeItemEntity();

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

        item003.setTitle("余额支付");
        item003.setTextNormal(R.color.color_cfcfcf);
        item003.setTextSelect(R.color.color_00bffe);
        item003.setImageResNormal(R.drawable.balanceicon);
        item003.setImageResSelect(R.drawable.balanceicon);
        item003.setBgNormal(R.drawable.bg_bx_cfcfcf_bj_10dp);
        item003.setBgSelect(R.drawable.bg_bx_00bffe_bj_10dp);
        item003.setNormalTypeImageRes(R.drawable.not_selected);
        item003.setSelectTypeImageRes(R.drawable.pitch_on);

        item004.setTitle("云闪付支付");
        item004.setTextNormal(R.color.color_cfcfcf);
        item004.setTextSelect(R.color.color_00bffe);
        item004.setImageResNormal(R.mipmap.quick_pay_ic);
        item004.setImageResSelect(R.mipmap.quick_pay_ic);
        item004.setBgNormal(R.drawable.bg_bx_cfcfcf_bj_10dp);
        item004.setBgSelect(R.drawable.bg_bx_00bffe_bj_10dp);
        item004.setNormalTypeImageRes(R.drawable.not_selected);
        item004.setSelectTypeImageRes(R.drawable.pitch_on);

        arr.add(item004);
        arr.add(item001);
        arr.add(item002);
        arr.add(item003);

        return arr;
    }

    @OnClick(R.id.tvToPlay)
    public void onViewClicked() {
        PlaySelectTypeItemAdapter adapter = (PlaySelectTypeItemAdapter)(filllist.getAdapter());
        int selectPosition = adapter.selectPosition;
        switch (selectPosition){
            case 0://ORDER_ADD_EARNEST
                //云闪付支付
                payType  = quickType;
                toService(quickType, null);
                break;
            case 1:
                //微信
                payType  = wechatType;
                toService(wechatType, null);
                break;
            case 2:
                //支付宝
                payType  = aliplayType;
                toService(aliplayType, null);
                break;
            case 3:
                //余额支付
                payType  = balanceType;
                payDialog();
                break;
        }
    }

    private void payDialog() {
        final PayPassDialog dialog=new PayPassDialog(this);
        PayPassView payViewPass = dialog.getPayViewPass();
        payViewPass.setForgetText("");
        payViewPass.setPayClickListener(new PayPassView.OnPayClickListener() {
            @Override
            public void onPassFinish(String passContent) {
                dialog.dismiss();
                //6位输入完成回调
                toService(balanceType, Sh256.getSHA256(passContent));
            }
            @Override
            public void onPayClose() {
                dialog.dismiss();
                //关闭回调
            }
            @Override
            public void onPayForget() {
//                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaySelectTypeItemAdapter adapter = (PlaySelectTypeItemAdapter)(parent.getAdapter());
        adapter.setSelectPosition(position);
    }

    private void toService(int type, String payPwd){
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("payType", type);
        if(!TextUtils.isEmpty(payPwd)){
            hashMap.put("payPwd", payPwd);
        }
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDER_ADD_EARNEST, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        if(type == balanceType){
                            finishPage();
                        }else{
                            try {
                                UnionPayUtil payUtil =  UnionPayUtil.getUnionPayUtil(PlayMesFeesActivity.this);
                                JSONObject  object = new JSONObject(s.data);
                                JSONObject obj   = object.getJSONObject("appPayRequest");
//                                if(type==10){ //微信
//                                    payUtil.payWX(obj.toString());
//                                }else if(type==11){ //支付宝
//                                    payUtil.payAliPay(obj.toString());
//                                }else if(type == 12){ //云闪付
//                                    payUtil.payCloudQuickPay(obj.toString());
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


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

    private void finishPage(){
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
//    }
}
