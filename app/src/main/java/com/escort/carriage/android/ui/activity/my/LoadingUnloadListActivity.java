package com.escort.carriage.android.ui.activity.my;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.adapter.ArrayListAdapter;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.my.LoadingUnloadItemEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.home.ResponseLoadingUnloadItemEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.dialog.AutoBottomUIDialog;
import com.escort.carriage.android.ui.view.list.FillListView;
import com.escort.carriage.android.utils.AmapUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 装卸车 列表 页面
 */
public class LoadingUnloadListActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvShowText)
    TextView tvShowText;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.fillList)
    FillListView fillList;
    @BindView(R.id.btnNext)
    TextView btnNext;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.finishView)
    View finishView;
    @BindView(R.id.ivItemTopImage)
    ImageView ivItemTopImage;
    @BindView(R.id.tvItemTopContent)
    TextView tvItemTopContent;
    @BindView(R.id.tvItemTopButton)
    TextView tvItemTopButton;
    @BindView(R.id.llTopGroup)
    LinearLayout llTopGroup;
    @BindView(R.id.ivItemBottomImage)
    ImageView ivItemBottomImage;
    @BindView(R.id.tvBottomContent)
    TextView tvItemBottomContent;
    @BindView(R.id.tvItemBottomButton)
    TextView tvItemBottomButton;
    @BindView(R.id.llBottomGroup)
    LinearLayout llBottomGroup;
    private String orderNumber;
    private int orderStatus;//订单状态(-1取消 0 待接单 1 已接单 2 前往载货地 3 抵达载货地 4 载货完成 5 出发目的地 6 抵达目的地 7 卸货完成 8 已结单)
    private int pageType;//0: 装车列表   1： 卸车列表
    private boolean isRefreshList = false;

    private PageListAdatper adatper;
    private double longitude;//经度
    private double latitude;//纬度
   // AutoHideBottomUIDialog ICCardDialog;
    AutoBottomUIDialog ICCardDialog;
    Button ivRFIDAnim;
    TextView tvRecordRFIDResult;
    Float dialogHeight = 460f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_loading_unload_list);
        ButterKnife.bind(this);
        orderNumber = getIntent().getStringExtra("orderNumber");
        orderStatus = getIntent().getIntExtra("orderStatus", 0);
        pageType = getIntent().getIntExtra("pageType", 0);
        setTextView();
        setList();
        getPageList();
        getLocation();
        //initICCardDialog();
    }

    private void initICCardDialog() {
        ICCardDialog = new AutoBottomUIDialog(this);
        View view  = getLayoutInflater().inflate(R.layout.dialog_entry_ic_card,null);
        ICCardDialog.setContentView(view);
        ICCardDialog.setDialogHeight(dialogHeight);
        ivRFIDAnim = view.findViewById(R.id.tvRecordResult);
        tvRecordRFIDResult = view.findViewById(R.id.title_tv);
        ICCardDialog.setCancelable(false);
        ICCardDialog.setCanceledOnTouchOutside(false);
       // ivRFIDAnim.setOnClickListener(this);
    }

    private void setList() {
        adatper = new PageListAdatper(this);
        fillList.setAdapter(adatper);
    }

    private void setTextView() {
        String titleStr = "";
        String showTextStr = "";
        if (pageType == 0) {
            titleStr = "装货确认";
            showTextStr = "及时进行装货确认，让订单进行更顺畅";
        } else {
            titleStr = "卸货确认";
            showTextStr = "及时进行卸货确认，让订单进行更顺畅";
        }
        tvTitle.setText(titleStr);
        tvShowText.setText(showTextStr);

        setTopItemGroup();

        setBottomItemGroup();
    }

    private void setBottomItemGroup() {
        if (pageType == 0) {
            //装货
            ivItemBottomImage.setImageResource(R.drawable.icon_text_zhuang);
            tvItemBottomContent.setText("确认全部装货完成");
            tvItemBottomButton.setText("全部完成");
//            if(orderStatus == 3){
//                tvItemBottomButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
//                tvItemBottomButton.setOnClickListener(null);
//            } else {
            tvItemBottomButton.setBackgroundResource(R.drawable.bg_b_067dff_circular);
            tvItemBottomButton.setOnClickListener(this);
//            }
        } else if (pageType == 1) {
            llBottomGroup.setVisibility(View.GONE);
        }
    }

    /**
     * 设置顶部条目
     */
    private void setTopItemGroup() {
        if (pageType == 0) {
            //装货
            ivItemTopImage.setImageResource(R.drawable.icon_text_zhuang);
            tvItemTopContent.setText("抵达装货地");
            tvItemTopButton.setText("我已抵达");
            if (orderStatus == 3) {
                tvItemTopButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                tvItemTopButton.setOnClickListener(null);
            } else {
                tvItemTopButton.setBackgroundResource(R.drawable.bg_b_067dff_circular);
                tvItemTopButton.setOnClickListener(this);
            }
        } else if (pageType == 1) {
            //卸货
            ivItemTopImage.setImageResource(R.drawable.icon_text_xie);
            tvItemTopContent.setText("抵达目的地");
            tvItemTopButton.setText("我已抵达");
            if (orderStatus == 6) {
                tvItemTopButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                tvItemTopButton.setOnClickListener(null);
            } else {
                tvItemTopButton.setBackgroundResource(R.drawable.bg_b_067dff_circular);
                tvItemTopButton.setOnClickListener(this);
            }
        }

    }


    private void getPageList() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page", RequestEntityUtils.getPageBean(1, 100));
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("installAndUnload", pageType);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDER_SELECTORDERRELATIONSTATUS, jsonString).execute(new MyStringCallback<ResponseLoadingUnloadItemEntity>() {
            @Override
            public void onResponse(ResponseLoadingUnloadItemEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        if (s.data != null && s.data.size() > 0) {
                            adatper.setList(s.data);
                        } else {
                            ToastUtil.showToastString("无数据");
                        }
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseLoadingUnloadItemEntity> getClazz() {
                return ResponseLoadingUnloadItemEntity.class;
            }
        });
    }

    @OnClick({R.id.ivClose, R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ivClose:
            case R.id.btnNext:
                if (isRefreshList) {
                    setResult(456);
                }
                finish();
                break;

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvItemTopButton:
                new AlertDialog.Builder(LoadingUnloadListActivity.this)
                .setMessage("是否确定抵达目的地?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("orderNumber", orderNumber);
                                toService(ProjectUrl.ORDER_UPDATAORDERDETAILSTATUS, 0, hashMap, new ServiceCallback() {
                                    @Override
                                    public void callback(int type) {
                                        //让控件不可点击
                                        setResult(456);
                                        tvItemTopButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                                        tvItemTopButton.setOnClickListener(null);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.tvItemBottomButton:
               new AlertDialog.Builder(LoadingUnloadListActivity.this)
              .setMessage("是否全部完成?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("orderNumber", orderNumber);
                                toService(ProjectUrl.ORDER_UPDATAORDERDETAILSTATUS, 0, hashMap, new ServiceCallback() {
                                    @Override
                                    public void callback(int type) {
                                        //让控件不可点击
                                        setResult(456);
                                        tvItemBottomButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                                        tvItemBottomButton.setOnClickListener(null);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        }).show();

                break;


        }
    }

    private void toService(String url, int type, Object object, ServiceCallback callback) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(object);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(url, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        if (callback != null) {
                            callback.callback(type);
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

    interface ServiceCallback {
        void callback(int type);
    }

    private class PageListAdatper extends ArrayListAdapter<LoadingUnloadItemEntity> {

        public PageListAdatper(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AdapterHolder holder;
            //利用缓存
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_loading_unload_list_layout, null);
                holder = new AdapterHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (AdapterHolder) convertView.getTag();
            }
            LoadingUnloadItemEntity entity = mList.get(position);

            if (pageType == 0) {
                //装车
                holder.tvContent.setText(entity.startAddr);
                holder.ivImage.setImageResource(R.drawable.icon_text_zhuang);
                holder.tvButton.setText("我已装货");
                if (entity.installType == 0) {
                    //未完成
                    holder.tvButton.setBackgroundResource(R.drawable.bg_b_067dff_circular);
                    holder.tvButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             new AlertDialog.Builder(LoadingUnloadListActivity.this)
                             .setMessage("是否确认装货?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("id", entity.getId());
                                            hashMap.put("installType", 1);
                                            hashMap.put("orderNumber", orderNumber);
                                            if (longitude != 0) {
                                                hashMap.put("longitude", longitude);
                                            }
                                            if (latitude != 0) {
                                                hashMap.put("latitude", latitude);
                                            }
                                            toService(ProjectUrl.ORDERVEHICLE_ADD_STATUS, 2, hashMap, new ServiceCallback() {
                                                @Override
                                                public void callback(int type) {
                                                    //让控件不可点击
                                                    setResult(456);
                                                    holder.tvButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                                                    holder.tvButton.setOnClickListener(null);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    });
                } else {
                    //已完成
                    holder.tvButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                }
            } else {
                //卸车
                holder.tvContent.setText(entity.endAddr);
                holder.ivImage.setImageResource(R.drawable.icon_text_xie);
                holder.tvButton.setText("我已卸货");
                if (entity.unloadType == 0) {
                    //未完成
                    holder.tvButton.setBackgroundResource(R.drawable.bg_b_067dff_circular);
                    holder.tvButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(LoadingUnloadListActivity.this)
                                    .setMessage("是否确认卸货?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("id", entity.getId());
                                            hashMap.put("unloadType", 1);
                                            hashMap.put("orderNumber", orderNumber);
                                            hashMap.put("longitude", longitude);
                                            hashMap.put("latitude", latitude);

                                            toService(ProjectUrl.ORDERVEHICLE_ADD_STATUS, 2, hashMap, new ServiceCallback() {
                                                @Override
                                                public void callback(int type) {
                                                    //让控件不可点击
                                                    setResult(456);
                                                    holder.tvButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                                                    holder.tvButton.setOnClickListener(null);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                        }
                                    }).show();



                        }
                    });
                } else {
                    //已完成
                    holder.tvButton.setBackgroundResource(R.drawable.bg_b_999999_circular);
                }
            }


            return convertView;
        }


        private class AdapterHolder {
            ImageView ivImage;
            TextView tvContent;
            TextView tvButton;

            public AdapterHolder(View view) {
                ivImage = view.findViewById(R.id.ivImage);
                tvContent = view.findViewById(R.id.tvContent);
                tvButton = view.findViewById(R.id.tvButton);
            }
        }
    }
}
