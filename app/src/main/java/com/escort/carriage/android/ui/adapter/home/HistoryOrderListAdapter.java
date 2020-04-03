package com.escort.carriage.android.ui.adapter.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.home.AddrBean;
import com.escort.carriage.android.entity.bean.home.OrderInfoEntity;
import com.escort.carriage.android.entity.bean.home.MyOrderListItemEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 作者： 历史订单 列表数据
 */

public class HistoryOrderListAdapter extends BaseQuickAdapter<OrderInfoEntity, HistoryOrderListAdapter.OrderHolder> {

    public HistoryOrderListAdapter(@Nullable List<OrderInfoEntity> data) {
        super(R.layout.item_history_order_list_layout, data);

    }

    @Override
    protected void convert(OrderHolder helper, OrderInfoEntity item) {

        GlideManager.getGlideManager().loadImage(item.imgUrl1, helper.ivImage, R.drawable.live_placeholder);
        if(item.addr != null && item.addr.size() > 0){
            AddrBean addrBean = item.addr.get(0);
            helper.tvStartLocation.setText(addrBean.startCityName);
            helper.tvEndtLocation.setText(addrBean.endCityName);
        }
//        helper.tvStartLocation.setText(item.startCityName);
//        helper.tvEndtLocation.setText(item.endCityName);

        String content = "";
        if (!TextUtils.isEmpty(item.cargoName)) {
            content += item.cargoName + ",";
        }

        content += item.cargoCount + "件,";

        content += item.cargoVolume + "方/" + item.cargoWeight + "吨";

        if (!TextUtils.isEmpty(item.loadNumAndDischargeNum)) {
            content += "," + item.loadNumAndDischargeNum;
        }

        helper.tvContent.setText(content);
        helper.tvMileage.setText("总里程约：" + item.distance + "kM");
        helper.tvTime.setText(ProjectDateUtils.getTimeDay("yyyy-MM-dd HH:mm", item.orderPlaceTime));
        helper.tvMoney.setText("￥" + item.paramValue);
    }
    

    public class OrderHolder extends BaseViewHolder {
        ImageView ivImage;
        TextView tvStartLocation;
        ImageView ivLocationImage;
        TextView tvEndtLocation;
        TextView tvContent;
        ImageView ivLocation;
        TextView tvMileage;
        TextView tvMoney;
        TextView tvTime;


        public OrderHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            tvTime = view.findViewById(R.id.tvTime);
            tvStartLocation = view.findViewById(R.id.tvStartLocation);
            ivLocationImage = view.findViewById(R.id.ivLocationImage);
            tvEndtLocation = view.findViewById(R.id.tvEndtLocation);
            tvContent = view.findViewById(R.id.tvContent);
            ivLocation = view.findViewById(R.id.ivLocation);
            tvMoney = view.findViewById(R.id.tvMoney);
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
