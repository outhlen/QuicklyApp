package com.escort.carriage.android.ui.activity.mes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.AddrBean;
import com.escort.carriage.android.entity.bean.home.OrderInfoEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.ResponseOrderBeanEntity;
import com.escort.carriage.android.entity.response.home.HuoYunDanBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.download.DownLoadManager;
import com.escort.carriage.android.http.download.DownloadListener;
import com.escort.carriage.android.ui.ImageLookActivity;
import com.escort.carriage.android.ui.activity.adapter.PhotoListAdapter;
import com.escort.carriage.android.ui.activity.bean.PhotoBean;
import com.escort.carriage.android.ui.view.text.DrawableTextView;
import com.escort.carriage.android.utils.AmapUtils;
import com.escort.carriage.android.utils.ChineseNumUtill;
import com.escort.carriage.android.utils.ImageLoader;
import com.escort.carriage.android.utils.OpenFileUtils;
import com.escort.carriage.android.utils.mes.MesNumUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OrderInfoActivity extends ProjectBaseActivity implements View.OnClickListener {
    @BindView(R.id.item_head_bar_iv_back)
    ImageView itemHeadBarIvBack;
    @BindView(R.id.item_head_bar_tv_title)
    TextView itemHeadBarTvTitle;
    @BindView(R.id.item_head_bar_tv_red)
    TextView itemHeadBarTvRed;
    @BindView(R.id.item_head_bar_iv_right)
    ImageView itemHeadBarIvRight;
    @BindView(R.id.tvStartLocation)
    TextView tvStartLocation;
    @BindView(R.id.ivLocationImage)
    ImageView ivLocationImage;
    @BindView(R.id.tvEndtLocation)
    TextView tvEndtLocation;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvMileage)
    TextView tvMileage;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvOrderType)
    TextView tvOrderType;
    @BindView(R.id.tvOderName)
    TextView tvOderName;
    @BindView(R.id.orderNum)
    TextView orderNum;
    @BindView(R.id.tvOderBulk)
    TextView tvOderBulk;
    @BindView(R.id.orderWeight)
    TextView orderWeight;
    @BindView(R.id.tvThreeExceedTitle)
    TextView tvThreeExceedTitle;
    @BindView(R.id.tvLoadingDischargeInfo)
    TextView tvLoadingDischargeInfo;
    @BindView(R.id.tvThreeExceed)
    TextView tvThreeExceed;
    @BindView(R.id.tvRemarkInfo)
    TextView tvRemarkInfo;
    @BindView(R.id.gvImageGroup)
    GridView gvImageGroup;
    @BindView(R.id.tvCarriageMoney)
    TextView tvCarriageMoney;
    @BindView(R.id.tvCarriageTime)
    TextView tvCarriageTime;
    @BindView(R.id.llLocationGroup)
    LinearLayout llLocationGroup;
    @BindView(R.id.iv_head_img)
    ImageView ivHeadImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_use_day)
    TextView tvUseDay;
    @BindView(R.id.ivCallPhone)
    ImageView ivCallPhone;
    @BindView(R.id.btnBidding)
    TextView btnBidding;
    //    @BindView(R.id.imageOne)
//    ImageView imageOne;
//    @BindView(R.id.imageTwo)
//    ImageView imageTwo;
//    @BindView(R.id.imageThree)
//    ImageView imageThree;
    @BindView(R.id.photo_view)
    RecyclerView photoView;
    @BindView(R.id.tvIsTb)
    DrawableTextView tvIsTb;
    @BindView(R.id.tvCargoValue)
    TextView tvCargoValue;
    @BindView(R.id.tvTurnAllowSingle)
    TextView tvTurnAllowSingle;
    @BindView(R.id.tvInsuranceServicesGroup)
    LinearLayout insuranceServicesGroup;
    @BindView(R.id.tvInsuranceServices)
    TextView tvInsuranceServices;
    @BindView(R.id.tvInsuranceMoneyGroup)
    LinearLayout insuranceMoneyGroup;
    @BindView(R.id.tvInsuranceMoney)
    TextView tvInsuranceMoney;
    @BindView(R.id.tvInvoiceType)
    TextView tvInvoiceType;
    @BindView(R.id.tvDistances)
    TextView tvDistances;
    @BindView(R.id.tvDistancesLabel)
    TextView tvDistancesLabel;
    @BindView(R.id.showReceipt)
    TextView showReceipt;
    @BindView(R.id.order_num_tv)
    TextView orderTv;
    OrderInfoEntity infoEntity;

    private int openType = 1;//打开类型  默认是1   1：货源大厅   2：我的订单 3:历史订单
    private String orderID = "";//获取货源大厅的数据时
    private double longitude;//经度
    private double latitude;//纬度
    private PhotoListAdapter mAdapter;
    List<PhotoBean> list;
    ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>(); // 这个最好定义成成员变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        setContentView(R.layout.activity_oder_info);
        ButterKnife.bind(this);
        setTitleBar();
        ZoomMediaLoader.getInstance().init(new ImageLoader());
        Intent intent = getIntent();
        orderID = intent.getStringExtra("id");
        openType = intent.getIntExtra("openType", 1);
        if (openType == 1) {
            showReceipt.setVisibility(View.GONE);
            getLocation();
        } else {
            if (!TextUtils.isEmpty(orderID)) {
                getInfo(orderID);
            }
        }
        list = new ArrayList<>();
        photoView.setLayoutManager(new GridLayoutManager(this, 4));
        photoView.addItemDecoration(new GridSpacingItemDecoration(4, 25, false));
        mAdapter = new PhotoListAdapter(OrderInfoActivity.this, R.layout.photo_recycler_layout, list);
        photoView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                GPreviewBuilder.from(OrderInfoActivity.this)
                        //是否使用自定义预览界面，当然8.0之后因为配置问题，必须要使用
                        .to(ImageLookActivity.class)
                        .setData(mThumbViewInfoList)
                        .setCurrentIndex(i)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        // 小圆点
                        .start();//启动
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });

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
                        if (!TextUtils.isEmpty(orderID)) {
                            getInfo(orderID);
                        }
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtils.showI("MainActivity", "AmapError   location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        ToastUtil.showToastString("定位失败，请检查是否已开启定位");
                    }
                }
            }
        });
    }

    private void setTitleBar() {
        itemHeadBarTvTitle.setText("订单详情");
        itemHeadBarIvRight.setVisibility(View.VISIBLE);
        itemHeadBarIvRight.setImageResource(R.mipmap.img_index_msg);
//        itemHeadBarTvRed.setVisibility(View.VISIBLE);
//        itemHeadBarTvRed.setText("20");
    }


    /**
     * 查询订单详情
     */
    public void getInfo(String id) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        if (openType == 1) {
            LogUtils.showE("传参longitude", longitude + "");
            data.put("longitude", longitude + "");
            data.put("latitude", latitude + "");
            data.put("isLogistics", "1");
        }
        data.put("orderNumber", id);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDER_GETORDERDETAIL, jsonString).execute(new MyStringCallback<ResponseOrderBeanEntity>() {
            @Override
            public void onResponse(ResponseOrderBeanEntity resp) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (resp != null) {
                    if (resp.success) {
                        infoEntity = resp.getData();
                        intPageViewData();
                    } else {
                        ToastUtil.showToastString(resp.message);
                    }
                }
            }

            @Override
            public Class<ResponseOrderBeanEntity> getClazz() {
                return ResponseOrderBeanEntity.class;
            }
        });

    }


    private void intPageViewData() {
        //设置状态
        setPhoneAndOrderType();
        //设置类型
        setOrderType(tvOrderType, infoEntity.orderType);
        if (infoEntity.addr != null && infoEntity.addr.size() > 0) {
            AddrBean addrBean = infoEntity.addr.get(0);
            tvStartLocation.setText(addrBean.startCityName);
            tvEndtLocation.setText(addrBean.endCityName);
        }
        String contentStr = "";

        if (TextUtils.equals("0", infoEntity.getIsDeposit())) {
            contentStr += "无押金";
        } else {
            contentStr += "押金" + infoEntity.getDeposit();
        }

        if (infoEntity.receipt == 1) {
            contentStr += "     需要回单";
        } else {
            contentStr += "     无需回单";
        }

        contentStr += "     " + infoEntity.getIntention() + "优先";
        orderTv.setText(infoEntity.getOrderNumber());
        tvContent.setText(contentStr);
        tvMileage.setText("运输里程约：" + infoEntity.getDistance() + "km");
        //货物名称
        tvOderName.setText(infoEntity.cargoName);
        //货物数量
        orderNum.setText(infoEntity.cargoCount + "件");
        //货物重量
        tvOderBulk.setText(infoEntity.cargoWeight + "吨");
        //货物体积
        orderWeight.setText(infoEntity.cargoVolume + "m³");
        //装卸信息
        tvLoadingDischargeInfo.setText(infoEntity.loadNumAndDischargeNum);
        //配送方式
        if (TextUtils.equals("1", infoEntity.deliveryWay)) {
            tvThreeExceed.setText("站点提货");
        } else if (TextUtils.equals("2", infoEntity.deliveryWay)) {
            tvThreeExceed.setText("配送到门");
        }

        //备注信息
        tvRemarkInfo.setText(infoEntity.remark);

        if (!TextUtils.isEmpty(infoEntity.imgUrl1)) {
            if (list.size() > 0) {
                list.clear();
            }
            String str[] = infoEntity.imgUrl1.split(",");
            for (String url : str) {
                PhotoBean photoBean = new PhotoBean();
                photoBean.setUrl(url);
                list.add(photoBean);
            }
            mAdapter.notifyDataSetChanged();

            for (int i = 0; i < list.size(); i++) {
                Rect bounds = new Rect();
                //new ThumbViewInfo(图片地址);
                ThumbViewInfo item = new ThumbViewInfo(list.get(i).getUrl());
                item.setBounds(bounds);
                mThumbViewInfoList.add(item);
            }

        }

        //设置图片
//        if (!TextUtils.isEmpty(infoEntity.imgUrl1)) {
//            GlideManager.getGlideManager().loadImage(infoEntity.imgUrl1, imageOne);
//            imageOne.setTag(infoEntity.imgUrl1);
//            imageOne.setOnClickListener(this);
//        }
//        if (!TextUtils.isEmpty(infoEntity.imgUrl2)) {
//            GlideManager.getGlideManager().loadImage(infoEntity.imgUrl2, imageTwo);
//            imageTwo.setTag(infoEntity.imgUrl2);
//            imageTwo.setOnClickListener(this);
//        }
//        if (!TextUtils.isEmpty(infoEntity.imgUrl3)) {
//            GlideManager.getGlideManager().loadImage(infoEntity.imgUrl3, imageThree);
//            imageThree.setTag(infoEntity.imgUrl3);
//            imageThree.setOnClickListener(this);
//        }

        //设置多装多卸的条目
        if (openType == 1) {
            tvDistances.setText(infoEntity.getDistances() + "公里");
        } else {
            tvDistances.setVisibility(View.GONE);
            tvDistancesLabel.setVisibility(View.GONE);
        }
        setLocation();
        //运费金额
        if (infoEntity.orderStatus == 0) {
            tvCarriageMoney.setText("未竞价");
        } else {
            tvCarriageMoney.setText(infoEntity.expectFreightRate + "元");
        }

        //订单时间
        tvCarriageTime.setText(ProjectDateUtils.getTimeDay("yyyy-MM-dd HH:mm:ss", infoEntity.orderPlaceTime));
        //货值单价
        tvCargoValue.setText(infoEntity.cargoValue == 0d ? "0元" : infoEntity.cargoValue + "元");
        //允许转单
        if (infoEntity.isAllowTurn == 1) {
            tvTurnAllowSingle.setText("是");
        } else {
            tvTurnAllowSingle.setText("否");
        }
        //设置是否需要保单
        if (TextUtils.isEmpty(infoEntity.insuranceComName)) {
            tvIsTb.setVisibility(View.INVISIBLE);
            insuranceServicesGroup.setVisibility(View.GONE);
            insuranceMoneyGroup.setVisibility(View.GONE);
        } else {
            tvIsTb.setVisibility(View.VISIBLE);
            //保险服务
            tvInsuranceServices.setText(infoEntity.insuranceComName);
            //保险费用
            tvInsuranceMoney.setText(TextUtils.isEmpty(infoEntity.insurancePay) ? "0元" : infoEntity.insurancePay + "元");
        }

        //发票类型
        if (TextUtils.equals("s", infoEntity.taxType)) {
            tvInvoiceType.setText("专业发票");
        } else if (TextUtils.equals("p", infoEntity.taxType)) {
            tvInvoiceType.setText("普通发票");
        } else {
            tvInvoiceType.setText("不需要发票");
        }


        //货主姓名
        tvName.setText(infoEntity.shipperName);
        //货主 公司名称
        tvUseDay.setText(infoEntity.startComp);
        //货主头像
        if (!TextUtils.isEmpty(infoEntity.shipperImg)) {
            GlideManager.getGlideManager().loadImage(infoEntity.shipperImg, ivHeadImg);
        }

    }

    public void setOrderType(TextView textView, String type) {
        if (TextUtils.isEmpty(type)) {
            type = "0";
        }
        switch (type) {
            case "0"://快速货运
                textView.setTextColor(ResourcesTransformUtil.getColor(R.color.color_1285fd));
                textView.setBackgroundResource(R.drawable.bg_b_d8effc_bj_3dp);
                textView.setText("快速货运");
                break;
            case "1"://专线直达
                textView.setTextColor(ResourcesTransformUtil.getColor(R.color.color_fe5d1f));
                textView.setBackgroundResource(R.drawable.bg_b_fee7e4_bj_3dp);
                textView.setText("指定专线");
                break;
            case "2"://城市配送
                textView.setTextColor(ResourcesTransformUtil.getColor(R.color.color_67c23a));
                textView.setBackgroundResource(R.drawable.bg_b_e9f5e4_bj_3dp);
                textView.setText("同城配送");
                break;


        }
    }

    private void setPhoneAndOrderType() {
        if (openType == 1) {
            //显示底部按钮
            btnBidding.setVisibility(View.VISIBLE);
            ivCallPhone.setVisibility(View.INVISIBLE);
            tvStatus.setVisibility(View.INVISIBLE);
        } else if (openType == 2) {
            //显示状态 跟电话号码
            btnBidding.setVisibility(View.INVISIBLE);
            ivCallPhone.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.VISIBLE);
            String statusName = "";

            switch (infoEntity.orderStatus) {//-1取消 0 待接单 1 已接单 2 前往载货地 3 抵达载货地 4 载货完成 5 出发目的地 6 抵达目的地 7 卸货完成 8 已结单
                case -1:
                    statusName = "取消";
                    break;
                case 0:
                    statusName = "待接单";
                    break;

                case 1:
                    statusName = "已接单";
                    break;

                case 2:
                    statusName = "前往装货地";
                    break;
                case 3:
                    statusName = "抵达装货地";
                    break;
                case 4:
                    statusName = "装货完成";
                    break;
                case 5:
                    statusName = "出发目的地";
                    break;
                case 6:
                    statusName = "抵达目的地";
                    break;
                case 7:
                    statusName = "卸货完成";
                    break;
                case 8:
                    statusName = "已结单";
                    break;
            }
            tvStatus.setText(statusName);
        } else if (openType == 3) {
            //显示底部按钮
            btnBidding.setVisibility(View.INVISIBLE);
            ivCallPhone.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.INVISIBLE);
        }
    }

    private void setLocation() {
        int truckLoadingNum = getTruckLoadingNum();
        int unloadNum = getUnloadNum();
        if (infoEntity.addr != null && infoEntity.addr.size() > 0) {
            llLocationGroup.removeAllViews();
            ////设置 多条装车数据
            setTruckLoadingView(truckLoadingNum);
            //设置多条卸车数据
            setUnloadNum(unloadNum);
        }
    }

    private void setUnloadNum(int unloadNum) {
        if (unloadNum > infoEntity.addr.size()) {
            unloadNum = infoEntity.addr.size();
        }
        for (int x = 0; x < unloadNum; x++) {
            View inflate = View.inflate(this, R.layout.item_order_loading_unload_list_layout, null);
            AddrBean addrBean = infoEntity.addr.get(x);
            String addrStr = null;
            if(openType == 1){//竞价
                addrStr = addrBean.endProvinceName + addrBean.endCityName + addrBean.endAreaName ;
            }else{
                addrStr = addrBean.endProvinceName + addrBean.endCityName + addrBean.endAreaName + addrBean.endAddr;
            }
            String end_num = TextUtils.isEmpty(addrBean.getEndCellphone()) ? "****" : addrBean.getEndCellphone();
            String end_name = TextUtils.isEmpty(addrBean.getEndLinkman()) ? "****" : addrBean.getEndLinkman();
            String endStr = end_name + " " + end_num;
            setTruckLoadingAndUnloadView(1, x, inflate, addrStr, endStr);
        }
    }

    private void setTruckLoadingView(int truckLoadingNum) {
        if (truckLoadingNum > infoEntity.addr.size()) {
            truckLoadingNum = infoEntity.addr.size();
        }
        for (int x = 0; x < truckLoadingNum; x++) {
            View inflate = View.inflate(this, R.layout.item_order_loading_unload_list_layout, null);
            AddrBean addrBean = infoEntity.addr.get(x);
            String addrStr  = null;
            if(openType == 1){ //竞价
                addrStr = addrBean.startProvinceName + addrBean.startCityName + addrBean.startAreaName;
            }else{
                addrStr = addrBean.startProvinceName + addrBean.startCityName + addrBean.startAreaName + addrBean.startAddr;
            }
            String start_num = TextUtils.isEmpty(addrBean.getStartCellphone()) ? "****" : addrBean.getStartCellphone();
            String start_name = TextUtils.isEmpty(addrBean.getStartLinkman()) ? "****" : addrBean.getStartLinkman();
            String startStr = start_name + "  " + start_num;
            setTruckLoadingAndUnloadView(0, x, inflate, addrStr, startStr);
        }
    }

    private void setTruckLoadingAndUnloadView(int type, int position, View viewGroup, String addrStr, String startStr) {
        ImageView iv = viewGroup.findViewById(R.id.ivItem);
        TextView tv = viewGroup.findViewById(R.id.tvItem);
        TextView textTv = viewGroup.findViewById(R.id.tvName);
        String str;
        if (type == 0) {
            textTv.setText(startStr);
            str = "(第" + ChineseNumUtill.numberToChinese(position + 1) + "装货地)";
            if (position < 1) {
                iv.setImageResource(R.drawable.icon_text_zhuang);
            } else {
                iv.setImageResource(R.drawable.bg_b_ffdd29_circular_size);
            }
        } else {
            textTv.setText(startStr);
            str = "(第" + ChineseNumUtill.numberToChinese(position + 1) + "卸货地)";
            if (position < 1) {
                iv.setImageResource(R.drawable.icon_text_xie);
            } else {
                iv.setImageResource(R.drawable.bg_b_3699ff_circular_size);
            }
        }
        SpannableString spannableString = new SpannableString(addrStr + str);
        //设置字体前景色
        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), addrStr.length(), spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        tv.setText(spannableString);
        llLocationGroup.addView(viewGroup);
    }

    /**
     * 装车地点数
     *
     * @return
     */
    private int getTruckLoadingNum() {
        int num = 1;
        try {
            String substring = infoEntity.loadNumAndDischargeNum.substring(0, 1);
            num = Integer.valueOf(substring);
        } catch (Exception e) {

        }
        return num;
    }

    /**
     * 卸车地点数
     *
     * @return
     */
    private int getUnloadNum() {

        int num = 1;
        try {
            String substring = infoEntity.loadNumAndDischargeNum.substring(2, 3);
            num = Integer.valueOf(substring);
        } catch (Exception e) {

        }
        return num;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.imageOne:
//            case R.id.imageTwo:
//            case R.id.imageThree:
//                Object tag = v.getTag();
//                if (tag instanceof String) {
//                    String url = (String) tag;
//                    Intent intent = new Intent(this, ImageActivity.class);
//                    intent.putExtra("url", url);
//                    startActivity(intent);
//                }
//                break;
        }
    }

    @OnClick({R.id.item_head_bar_iv_back, R.id.item_head_bar_iv_right, R.id.ivCallPhone, R.id.btnBidding, R.id.showReceipt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_head_bar_iv_back:
                finish();
                break;
            case R.id.item_head_bar_iv_right:
                MesNumUtils.getMesNumUtils().openMesAct(this);
                break;
            case R.id.ivCallPhone:
                callPhone(infoEntity.shipperPhone);
                break;
            case R.id.btnBidding:
                Intent intent = new Intent(this, BiddingActivity.class);
                intent.putExtra("orderNumber", infoEntity.orderNumber);
                intent.putExtra("intention", infoEntity.intention);
                startActivityForResult(intent, 333);
                break;
            case R.id.showReceipt:
                // 点击查看货运单，调接口调详情页面 todo:
                getAssumeRoleByOrderNo(infoEntity.orderNumber);
                break;
        }
    }

    /**
     * 通过订单号生成货运单
     */
    public void getAssumeRoleByOrderNo(String orderNumber) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("orderNumber", orderNumber);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.GOODS_TRANSPORT_RECEIPT, jsonString).execute(new MyStringCallback<HuoYunDanBean>() {
            @Override
            public void onResponse(HuoYunDanBean resp) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (resp != null) {
                    if (resp.success) {
                        //1.弹出提示下载对话框
                        if (TextUtils.isEmpty(resp.getData())) {
                            ToastUtil.showToastString("货运单获取失败!");
                        } else {
                            showDownLoadDialog(resp.getData());
                        }
                    } else {
                        ToastUtil.showToastString(resp.message);
                    }
                }
            }

            @Override
            public Class<HuoYunDanBean> getClazz() {
                return HuoYunDanBean.class;
            }
        });
    }

    /**
     * 1. 提示下载
     */
    private void showDownLoadDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示:");
        builder.setMessage("下载后查看货运单");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //2.开始下载
                downloadFile(url);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    ProgressDialog downloadDialog;

    /**
     * 2. 开始下载
     */
    private void downloadFile(String url) {

        downloadDialog = new ProgressDialog(this);
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        LogUtils.showE("LHF", "下载开始");
        DownLoadManager.getInstance().download(url,
                System.currentTimeMillis() + ".pdf" // 文件下载名称
                , new DownloadListener() {
                    /**
                     * 下载开始
                     */
                    @Override
                    public void onStart() {

                    }

                    /**
                     * 下载进度
                     * @param progress - 下载进度百分比
                     */
                    @Override
                    public void onProgress(int progress) {
                        downloadDialog.setProgress(progress);
                    }

                    /**
                     * 下载成功
                     * @param filePath - 文件下载路径
                     */
                    @Override
                    public void onSuccess(String filePath) {
                        downloadDialog.dismiss();
                        ToastUtil.showToastString("下载成功! 文件保存在: " + filePath);
                        File file = new File(filePath);
                        if (file.exists()) {
                            OpenFileUtils.openFile(getApplicationContext(), file);
                        }
                    }

                    /**
                     * 下载失败
                     */
                    @Override
                    public void onFailed(String message) {
                        downloadDialog.dismiss();
                        ToastUtil.showToastString(message);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333 && resultCode == 666) {
            finish();
        }
    }

    public void biddingFinishpage() {
        setResult(666);
        finish();
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

}
