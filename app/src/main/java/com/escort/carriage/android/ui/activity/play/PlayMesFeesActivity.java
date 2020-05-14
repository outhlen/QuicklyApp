package com.escort.carriage.android.ui.activity.play;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
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
        arr.add(item001);
        arr.add(item002);
        arr.add(item003);
        arr.add(item004);
        return arr;
    }

    @OnClick(R.id.tvToPlay)
    public void onViewClicked() {
        PlaySelectTypeItemAdapter adapter = (PlaySelectTypeItemAdapter)(filllist.getAdapter());
        int selectPosition = adapter.selectPosition;
        switch (selectPosition){
            case 0://ORDER_ADD_EARNEST
                //微信
                toService(wechatType, null);
                break;
            case 1:
                //支付宝
                toService(aliplayType, null);
                break;
            case 2:
                //余额支付
                payDialog();
                break;
            case 3:
                //云闪付支付
                toService(quickType, null);
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
                       // PlayMesFeesEntity jsonBean = JsonManager.getJsonBean(s.data, PlayMesFeesEntity.class);
                        UnionPayUtil payUtil =  UnionPayUtil.getUnionPayUtil(PlayMesFeesActivity.this);
                        if(type==10){ //微信
                            payUtil.payWX(s.data);
                        }else if(type==11){ //支付宝
                            payUtil.payAliPay(s.data);
                        }else if(type == 13){ //云闪付
                            payUtil.payCloudQuickPay(s.data);
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

    private void getPayService(int type,String orderId) {
        String userId  = CacheDBMolder.getInstance().getUserInfoEntity(null).getUserLoginId();
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userUUID", userId);
        hashMap.put("orderNo", orderId);
        hashMap.put("lkAppId", "5edcea89a67931192cffb3dc0c66c9e7");
        hashMap.put("lkAppSecret", "38520db5a00b30013f909a5a906860b208e0ec88");
        hashMap.put("payType", type);
        hashMap.put("bizType", "2");
        // hashMap.put("attachParam", "");
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.CHINAUNION_PAY_URL, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                       // String json  = s.data.toString();

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

}
