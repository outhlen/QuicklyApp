package com.escort.carriage.android.ui.adapter.play;

import android.text.TextUtils;
import android.widget.ImageView;

import com.androidybp.basics.ApplicationContext;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.play.ChargeMoneyEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 作者： Lance
 * 日期:  2018/9/6 9:00
 */

public class SendGodosDialogAdapter extends BaseQuickAdapter<ChargeMoneyEntity, BaseViewHolder> {

    private String defaultName = "";

    public SendGodosDialogAdapter(@Nullable List<ChargeMoneyEntity> data, String defaultName) {
        super(R.layout.item_dropdown, data);
        this.defaultName = defaultName;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChargeMoneyEntity item) {
        ImageView iv_checked = helper.findView(R.id.iv_checked);

            if (TextUtils.equals(defaultName, item.title)) {
                Glide.with(ApplicationContext.getInstance().application).load(R.mipmap.fuwudating_selected_selected).into(iv_checked);
            } else {
                Glide.with(ApplicationContext.getInstance().application).load(R.mipmap.fuwudating_selected_noemal).into(iv_checked);
            }
            helper.setText(R.id.drop_down_name, item.title);

    }
}
