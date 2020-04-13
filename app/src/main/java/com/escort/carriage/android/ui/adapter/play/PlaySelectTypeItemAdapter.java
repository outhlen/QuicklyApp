package com.escort.carriage.android.ui.adapter.play;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidybp.basics.ui.mvc.adapter.ArrayListAdapter;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.view.PlaySelectTypeItemEntity;

/**
 * 直播 支付    list列表工具类
 */

public class PlaySelectTypeItemAdapter extends ArrayListAdapter<PlaySelectTypeItemEntity> {

    public int selectPosition = 0;

    public PlaySelectTypeItemAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PaymentExceptionalArticleHolder holder;
        //利用缓存
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_play_select_type_layout, null);
            holder = new PaymentExceptionalArticleHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PaymentExceptionalArticleHolder) convertView.getTag();
        }
        PlaySelectTypeItemEntity bean = mList.get(position);

        holder.title.setText(bean.title);
        holder.text.setText(bean.content);

        if(position == selectPosition){
//            holder.select.setImageResource(R.drawable.pitch_on);
            holder.select.setImageResource(bean.selectTypeImageRes);
            holder.title.setTextColor(ResourcesTransformUtil.getColor(bean.textSelect));
            holder.image.setImageResource(bean.imageResSelect);
            holder.rootView.setBackgroundResource(bean.bgSelect);
        } else {
            holder.select.setImageResource(bean.normalTypeImageRes);
            holder.title.setTextColor(ResourcesTransformUtil.getColor(bean.textNormal));
            holder.image.setImageResource(bean.imageResNormal);
            holder.rootView.setBackgroundResource(bean.bgNormal);
        }
        return convertView;
    }

    public void setSelectPosition(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }

    class PaymentExceptionalArticleHolder {
        ImageView image;
        TextView title;
        TextView text;
        ImageView select;
        View rootView;

        public PaymentExceptionalArticleHolder(View view) {
            rootView = view;
            image = view.findViewById(R.id.item_payment_exceptional_article_image);
            title = view.findViewById(R.id.item_payment_exceptional_article_title);
            select = view.findViewById(R.id.item_payment_exceptional_article_select);
            text = view.findViewById(R.id.item_show_text);
        }
    }
}
