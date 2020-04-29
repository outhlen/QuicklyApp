package com.escort.carriage.android.ui.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.escort.carriage.android.R;
import com.escort.carriage.android.ui.activity.bean.PhotoBean;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends CommonAdapter<PhotoBean> {

    Context context;

    public PhotoListAdapter(Context context, int layoutId, List<PhotoBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, PhotoBean orderListBean, int position) {

        ImageView imageView = holder.getView(R.id.image);
        Glide.with(context).load(orderListBean.getUrl()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ImageInfo> imageInfos  = new ArrayList<>();
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setBigImageUrl(orderListBean.getUrl());
                imageInfo.setThumbnailUrl(orderListBean.getUrl());
                imageInfos.add(imageInfo);
                Intent intent = new Intent(context, ImagePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfos);
                bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
}
