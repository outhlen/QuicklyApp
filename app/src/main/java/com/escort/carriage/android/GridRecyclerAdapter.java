package com.escort.carriage.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.escort.carriage.android.entity.bean.MenuBean;

import java.util.List;

public class GridRecyclerAdapter extends  RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<MenuBean> mList;
    ClickCallBack callBack;

    public GridRecyclerAdapter(Context context, List<MenuBean> list,ClickCallBack call) {
        mContext = context;
        mList = list;
        callBack = call;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
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
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_tv);
            imageView = itemView.findViewById(R.id.icon_iv);
        }

        public void setData(int position){
            MenuBean bean = mList.get(position);
            tvTitle.setText(bean.getName());
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_place)
                    .error(R.drawable.ic_place)
                    .priority(Priority.HIGH);
            Glide.with(ProjectApplication.getContext()).load(bean.getImage())
                    .apply(options)
                    .into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onClick(position);
                }
            });
        }
    }
    public  interface  ClickCallBack{
        void  onClick(int position);
    }
}
