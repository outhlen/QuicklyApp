package com.escort.carriage.android.ui.adapter.home;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.home.MyOrderListItemEntity;
import com.escort.carriage.android.entity.bean.my.MyBidListEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 我的竞标
 */

public class MyBidListAdapter extends BaseQuickAdapter<MyBidListEntity.ListBean, MyBidListAdapter.OrderHolder> {

    public MyBidListAdapter(@Nullable List<MyBidListEntity.ListBean> data) {
        super(R.layout.item_my_bid_list_layout, data);

    }

    @Override
    protected void convert(OrderHolder helper, MyBidListEntity.ListBean item) {

        GlideManager.getGlideManager().loadImage(item.imgUrl, helper.ivImage, R.drawable.live_placeholder);
        helper.tvStartLocation.setText(item.startCity);
        helper.tvEndtLocation.setText(item.endCity);
        helper.tvTime.setText(ProjectDateUtils.getTimeDay("yyyy-MM-dd HH:mm", item.createDate));
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

        helper.tvContent.setText(content);
        helper.tvMileage.setText("总里程约：" + item.distance + "km");

        SpannableString spannableString = new SpannableString("￥" + item.money);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.6f);
        spannableString.setSpan(sizeSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        helper.tvMoney.setText(spannableString);
    }


    public class OrderHolder extends BaseViewHolder {
        ImageView ivImage;
        TextView tvStartLocation;
        TextView tvEndtLocation;
        TextView tvContent;
        TextView tvMileage;
        TextView tvTime;
        TextView tvMoney;


        public OrderHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            tvStartLocation = view.findViewById(R.id.tvStartLocation);
            tvEndtLocation = view.findViewById(R.id.tvEndtLocation);
            tvContent = view.findViewById(R.id.tvContent);
            tvMileage = view.findViewById(R.id.tvMileage);
            tvTime = view.findViewById(R.id.tvTime);
            tvMoney = view.findViewById(R.id.tvMoney);
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
