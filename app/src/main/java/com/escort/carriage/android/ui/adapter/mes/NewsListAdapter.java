package com.escort.carriage.android.ui.adapter.mes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.response.home.NewsListBean;
import com.escort.carriage.android.ui.activity.mes.NewsDetailsActivity;

import java.util.List;

/**
 * 头条消息Adapter
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private Context mContext;
    private List<NewsListBean.DataBean.ListBean> mList;

    public NewsListAdapter(Context context, List<NewsListBean.DataBean.ListBean> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvSubTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
        }

        public void setData(int position){
            NewsListBean.DataBean.ListBean bean = mList.get(position);

            tvTitle.setText(bean.getTitle());
            tvSubTitle.setText(bean.getSubTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                    intent.putExtra("bean", bean);
                    mContext.startActivity(intent);
                }
            });
        }
    }


}
