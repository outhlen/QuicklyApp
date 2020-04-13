package com.escort.carriage.android.ui.adapter.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.my.AppraiseInfoEntity;
import com.escort.carriage.android.entity.bean.my.OrderAppraiseListItemEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 评价 列表
 */

public class MyOrderAppraiseRightListAdapter extends BaseQuickAdapter<AppraiseInfoEntity, MyOrderAppraiseRightListAdapter.OrderHolder> {

    public MyOrderAppraiseRightListAdapter(@Nullable List<AppraiseInfoEntity> data) {
        super(R.layout.item_my_order_appraise_right_list_layout, data);

    }

    @Override
    protected void convert(OrderHolder helper, AppraiseInfoEntity item) {

//        helper.tvStartLocation.setText(item.startCityCode);
//        helper.tvEndtLocation.setText(item.endCityCode);
        helper.tvContent.setText(item.evaluationContent);

        helper.evaluationRatingbar.setRating(item.score);
        helper.evaluationRatingbar.setOnRatingBarChangeListener(null);
        helper.evaluationRatingbar.setIsIndicator(true);
    }
    

    public class OrderHolder extends BaseViewHolder {
        TextView tvStartLocation;
        ImageView ivLocationImage;
        TextView tvEndtLocation;
        TextView tvContent;
        RatingBar evaluationRatingbar;

        public OrderHolder(View view) {
            super(view);
            tvStartLocation = view.findViewById(R.id.tvStartLocation);
            ivLocationImage = view.findViewById(R.id.ivLocationImage);
            tvEndtLocation = view.findViewById(R.id.tvEndtLocation);
            tvContent = view.findViewById(R.id.tvContent);
            evaluationRatingbar = view.findViewById(R.id.evaluation_ratingbar);
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
