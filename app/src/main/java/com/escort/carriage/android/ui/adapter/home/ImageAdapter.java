package com.escort.carriage.android.ui.adapter.home;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.escort.carriage.android.ProjectApplication;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.BannerBean;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class ImageAdapter extends BannerAdapter<BannerBean.Banner.ListBean, ImageAdapter.BannerViewHolder> {

    public ImageAdapter(List<BannerBean.Banner.ListBean> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, BannerBean.Banner.ListBean data, int position, int size) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_place)
                .error(R.drawable.ic_place)
                .priority(Priority.HIGH);
        Glide.with(ProjectApplication.getContext()).load(data.getBannerUrl())
                .apply(options)
                .into(holder.imageView);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}
