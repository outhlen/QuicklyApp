package com.escort.carriage.android.ui.activity.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.home.AddrBean;
import com.escort.carriage.android.entity.bean.home.OrderInfoEntity;
import com.escort.carriage.android.ui.activity.TraceDetailActivity;

import java.util.List;

public class MyTraceOrderAdapter extends BaseQuickAdapter<OrderInfoEntity, MyTraceOrderAdapter.OrderHolder> {

    private Activity context;
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码

    public MyTraceOrderAdapter(@Nullable List<OrderInfoEntity> data, Activity fragment) {
        super(R.layout.item_trace_order_list, data);
        this.context = fragment;
    }

    @Override
    protected void convert(OrderHolder helper, OrderInfoEntity item) {
        GlideManager.getGlideManager().loadImage(item.imgUrl1, helper.ivImage, R.drawable.live_placeholder);
        if (item.addr != null && item.addr.size() > 0) {
            AddrBean addrBean = item.addr.get(0);
            helper.tvStartLocation.setText(addrBean.startCityName);
            helper.tvEndtLocation.setText(addrBean.endCityName);
        }
        helper.tvOrderNum.setText("订单号：" + item.orderNumber);
        helper.tvTime.setText(ProjectDateUtils.getTimeDay("MM-dd HH:mm", item.orderPlaceTime));
        helper.tvCargoName.setText(item.cargoName);
        helper.cargoCount.setText(item.cargoCount + "件");
        helper.tvCargoWeightVolume.setText(item.cargoWeight + "吨/" + item.cargoVolume + "方");
        helper.tvPackingManner.setText(item.loadNumAndDischargeNum);
        if(item.orderStatus==1){
            helper.zhuxieTv.setText("待接单");
        }else if(item.orderStatus==2){
            helper.zhuxieTv.setText("已接单");
        }else if(item.orderStatus==3){
            helper.zhuxieTv.setText("前往载货地");
        }else if(item.orderStatus==4){
            helper.zhuxieTv.setText("抵达载货地");
        }else if(item.orderStatus==5){
            helper.zhuxieTv.setText("载货完成");
        }else if(item.orderStatus==6){
            helper.zhuxieTv.setText("出发目的地");
        }else if(item.orderStatus==7){
            helper.zhuxieTv.setText("抵达目的地");
        }else if(item.orderStatus==8){
            helper.zhuxieTv.setText("卸货完成");
        }
        helper.linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + item.endCellphone);
                intent.setData(data);
                context.startActivity(intent);
            }
        });

        helper.traceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //追踪详情
                Intent intent = new Intent(context, TraceDetailActivity.class);
                intent.putExtra("orderId",item.getOrderNumber());
                context.startActivity(intent);
            }
        });
    }

    /**
     * 判断是否有某项权限
     * @param string_permission 权限
     * @param request_code 请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(context, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    private void makeTelphone(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }

    public class OrderHolder extends BaseViewHolder {
        ImageView ivImage;
        TextView tvStartLocation;
        TextView tvEndtLocation;
        TextView tvTime;
        TextView tvOrderNum;
        TextView tvCargoName;
        TextView cargoCount;
        TextView tvCargoWeightVolume;
        TextView tvPackingManner;
        TextView zhuxieTv;
        TextView linkButton;
        TextView traceButton;

        public OrderHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            tvStartLocation = view.findViewById(R.id.tvStartLocation);
            tvEndtLocation = view.findViewById(R.id.tvEndtLocation);
            tvTime = view.findViewById(R.id.order_time_tv);
            tvOrderNum = view.findViewById(R.id.order_num_tv);
            tvCargoName = view.findViewById(R.id.tvCargoName);
            cargoCount = view.findViewById(R.id.cargoCount);
            tvCargoWeightVolume = view.findViewById(R.id.tvCargoWeightVolume);
            tvPackingManner = view.findViewById(R.id.tvPackingManner);
            zhuxieTv = view.findViewById(R.id.zhuxie_tv);
            linkButton = view.findViewById(R.id.tel_driver);
            traceButton = view.findViewById(R.id.trace_tv);

        }

        /**
         * Will set the text of a TextView.
         *
         * @param viewId The view id.
         * @param value  The text to put in the text view.
         * @return The BaseViewHolder for chaining.
         */
        @Override
        public BaseViewHolder setText(int viewId, CharSequence value) {
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
            return super.setText(viewId, value);
        }
    }

}
