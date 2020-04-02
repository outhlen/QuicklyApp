package com.escort.carriage.android.ui.adapter.home;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.home.CircuitListEntity;
import com.escort.carriage.android.ui.view.flowlayout.FlowAdapter;
import com.escort.carriage.android.ui.view.flowlayout.FlowLayout;
import com.escort.carriage.android.ui.view.text.DrawableTextView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
public class MyFlowAdapter extends FlowAdapter<TextView> {

    private Context context;
    private List<CircuitListEntity.CircuitItemEntity.ListBean> data;
    private OnItemClickListener<String> itemClickListener;

    public MyFlowAdapter(Context context, List<CircuitListEntity.CircuitItemEntity.ListBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public TextView onCreateViewHolder(FlowLayout flowLayout, int viewType) {
        DrawableTextView inflate = (DrawableTextView) View.inflate(flowLayout.getContext(), R.layout.item_drawable_text_view, null);

        return inflate;
    }

    @Override
    public void onBindViewHolder(TextView view, int position) {
        view.setText(data.get(position).getCityName());
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(data != null){
           size = data.size();
        }
        return size;
    }

    public void setItemClickListener(OnItemClickListener<String> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data, View view);
    }
}