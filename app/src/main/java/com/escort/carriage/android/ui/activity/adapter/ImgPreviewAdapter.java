package com.escort.carriage.android.ui.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.escort.carriage.android.R;
import com.facebook.drawee.view.SimpleDraweeView;

import com.lzy.ninegrid.NineGridView;

import java.util.ArrayList;
import java.util.List;
import com.escort.carriage.android.ui.activity.ImagePreviewActivity;
import com.squareup.picasso.Picasso;
import com.lzy.ninegrid.ImageInfo;

import uk.co.senab.photoview.DefaultOnDoubleTapListener;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImgPreviewAdapter extends com.lzy.ninegrid.preview.ImagePreviewAdapter {

    private Context mContext;
    private List<ImageInfo> imageInfo;
    private int firstShowPosition = -1;


    public ImgPreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super(context, imageInfo);
        mContext = context;
        this.imageInfo = imageInfo;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_photoview_t, container, false);
        final PhotoView imageView = view.findViewById(R.id.pv);
        imageView.setOnDoubleTapListener(new DefaultOnDoubleTapListener((PhotoViewAttacher) imageView.getIPhotoViewImplementation()) {
            @Override
            public boolean onDoubleTap(MotionEvent ev) {
                PhotoViewAttacher photoViewAttacher = (PhotoViewAttacher) imageView.getIPhotoViewImplementation();
                if (photoViewAttacher == null)
                    return false;

                try {
                    float scale = photoViewAttacher.getScale();
                    float x = ev.getX();
                    float y = ev.getY();

                    if (scale < photoViewAttacher.getMediumScale()) {
                        photoViewAttacher.setScale(photoViewAttacher.getMediumScale(), x, y, true);
//                    } else if (scale >= photoViewAttacher.getMediumScale() && scale < photoViewAttacher.getMaximumScale()) {
//                        photoViewAttacher.setScale(photoViewAttacher.getMaximumScale(), x, y, true);
                    } else {
                        photoViewAttacher.setScale(photoViewAttacher.getMinimumScale(), x, y, true);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Can sometimes happen when getX() and getY() is called
                }

                return true;
            }
        });
        SimpleDraweeView vDraweeView = view.findViewById(R.id.smView);
        ImageInfo info = this.imageInfo.get(position);
        imageView.setOnPhotoTapListener(this);
        showExcessPic(info, imageView, vDraweeView);
        //如果需要加载的loading,需要自己改写,不能使用这个方法
        NineGridView.getImageLoader().onDisplayImage(view.getContext(), imageView, info.bigImageUrl);

        vDraweeView.setVisibility(View.INVISIBLE);
        imageView.setOnPhotoTapListener(this);
        container.addView(view);
        return view;
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(ImageInfo imageInfo, PhotoView imageView, SimpleDraweeView draweeView) {
        //先获取大图的缓存图片
        Bitmap cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.bigImageUrl);
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            Bitmap cacheSmallImage = NineGridView.getImageLoader().getCacheImage(imageInfo.thumbnailUrl);
            if (cacheSmallImage == null) {
                imageView.setImageResource(com.lzy.ninegrid.R.drawable.ic_default_image);
            } else
                imageView.setImageBitmap(cacheSmallImage);

            String url = imageInfo.getBigImageUrl();
            if (url.startsWith("http")) {
                Picasso.with(mContext).load(url)
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.drawable.ic_default_image)
                        .into(imageView);
            } else if (url.startsWith("file://")) {
                Picasso.with(mContext).load(Uri.parse(url))
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.drawable.ic_default_image)
                        .into(imageView);
            }
        } else {
            if (imageView.getVisibility() == View.VISIBLE && draweeView.getVisibility() == View.VISIBLE) {
                imageView.setImageBitmap(cacheImage);
                draweeView.setImageBitmap(cacheImage);
            } else if (imageView.getVisibility() == View.VISIBLE) {
                imageView.setImageBitmap(cacheImage);
            } else if (draweeView.getVisibility() == View.VISIBLE) {
                draweeView.setImageURI(imageInfo.getBigImageUrl());
            }
        }
    }

    /**
     * 单击屏幕关闭
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((ImagePreviewActivity) mContext).exitActivity();
    }

    public int getFirstShowPosition() {
        return firstShowPosition;
    }

    public void setFirstShowPosition(int firstShowPosition) {
        this.firstShowPosition = firstShowPosition;
    }
}
