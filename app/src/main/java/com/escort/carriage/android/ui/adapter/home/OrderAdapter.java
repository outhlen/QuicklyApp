package com.escort.carriage.android.ui.adapter.home;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.home.GoodsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： Lance
 * 日期:  2018/9/6 9:00
 */

public class OrderAdapter extends BaseQuickAdapter<GoodsBean, OrderAdapter.OrderHolder> {

    public OrderAdapter(@Nullable List<GoodsBean> data) {
        super(R.layout.item_order, data);

    }

    @Override
    protected void convert(OrderHolder helper, GoodsBean item) {

        GlideManager.getGlideManager().loadImage(item.imgUrl1, helper.ivImage, R.drawable.live_placeholder);
        helper.tvStartLocation.setText(item.startCityName);
        helper.tvEndtLocation.setText(item.endCityName);
        helper.tvTime.setText(item.duration);
        String content = "";
        if (!TextUtils.isEmpty(item.cargoName)) {
            content += item.cargoName + ",";
        }
        if (!TextUtils.isEmpty(item.cargoCount)) {
            content += item.cargoCount + "件,";
        }
        if (!TextUtils.isEmpty(item.cargoVolume) && !TextUtils.isEmpty(item.cargoWeight)) {
            content += item.cargoVolume + "方/" + item.cargoWeight + "吨";
        }
        if (!TextUtils.isEmpty(item.loadNumAndDischarge)) {
            content += "," + item.loadNumAndDischarge;
        }

        setOrderType(helper.tvOrderType, item.orderType);
        helper.tvContent.setText(content);
        if(TextUtils.isEmpty(item.distance)){
            helper.ivLocation.setVisibility(View.INVISIBLE);
            helper.tvMileage.setVisibility(View.INVISIBLE);

        } else {
            helper.ivLocation.setVisibility(View.VISIBLE);
            helper.tvMileage.setVisibility(View.VISIBLE);
            helper.tvMileage.setText("总里程约：" + item.distance + "kM");
        }

    }

    public void setOrderType(TextView textView, String type) {
        if(TextUtils.isEmpty(type)){
            type = "0";
        }
        switch (type) {
            case "0"://快速货运
                textView.setTextColor(ResourcesTransformUtil.getColor(R.color.color_1285fd));
                textView.setBackgroundResource(R.color.color_d8effc);
                textView.setText("快速货运");
                break;
            case "1"://专线直达
                textView.setTextColor(ResourcesTransformUtil.getColor(R.color.color_fe5d1f));
                textView.setBackgroundResource(R.color.color_fee7e4);
                textView.setText("专线直达");
                break;
            case "2"://城市配送
                textView.setTextColor(ResourcesTransformUtil.getColor(R.color.color_67c23a));
                textView.setBackgroundResource(R.color.color_e9f5e4);
                textView.setText("城市配送");
                break;


        }
    }

    public class OrderHolder extends BaseViewHolder {
        ImageView ivImage;
        TextView tvTime;
        TextView tvOrderType;
        TextView tvStartLocation;
        ImageView ivLocationImage;
        TextView tvEndtLocation;
        TextView tvContent;
        ImageView ivLocation;
        TextView tvBidding;
        TextView tvMileage;

        public OrderHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            tvTime = view.findViewById(R.id.tvTime);
            tvOrderType = view.findViewById(R.id.tvOrderType);
            tvStartLocation = view.findViewById(R.id.tvStartLocation);
            ivLocationImage = view.findViewById(R.id.ivLocationImage);
            tvEndtLocation = view.findViewById(R.id.tvEndtLocation);
            tvContent = view.findViewById(R.id.tvContent);
            ivLocation = view.findViewById(R.id.ivLocation);
            tvBidding = view.findViewById(R.id.tvBidding);
            tvMileage = view.findViewById(R.id.tvMileage);
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
