package com.escort.carriage.android.ui.adapter.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
import com.bumptech.glide.Glide;
import com.escort.carriage.android.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class GridViewImgsAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<LocalMedia> mPaths;
    private List<LocalMedia> localList = new ArrayList<>();
    public GridViewImgsAdapter(Context context, List<LocalMedia> paths) {
        mContext = context;
        mPaths = paths;
        localList  = paths;
    }

    @Override
    public int getCount() {
        if (mPaths != null) {
            return mPaths.size() + 1;
        }
        return 1;
    }

    @Override
    public Object getItem(int position) {
        if (mPaths != null) {
            return mPaths.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View
    getView(final int position, View convertView, ViewGroup parent) {
        View viewAdd;
        if (getCount() - 1 == position) {
            viewAdd = View.inflate(mContext, R.layout.item_add_img_file, null);
            ImageView ivAdd = viewAdd.findViewById(R.id.item_add_fault_file_iv_img);
            ImageView ivDel = viewAdd.findViewById(R.id.item_add_fault_file_iv_delete);
            ivDel.setVisibility(View.GONE);
            Glide.with(mContext).load(R.mipmap.img_release_source_add_img).into(ivAdd);
            ivAdd.setOnClickListener(this);
        } else {
            viewAdd = View.inflate(mContext, R.layout.item_add_img_file, null);
            ImageView ivAdd = viewAdd.findViewById(R.id.item_add_fault_file_iv_img);
            ImageView ivDel = viewAdd.findViewById(R.id.item_add_fault_file_iv_delete);
            Glide.with(mContext).load(mPaths.get(position).getPath()).thumbnail(0.1f).into(ivAdd);
            ivAdd.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ivDel.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mImgChangeCallback != null) {
                        mImgChangeCallback.onImgDelete(localList.get(position));
                    }
                }
            });
            viewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 预览图片 可自定长按保存路径
                    PictureSelector.create(((ProjectBaseEditActivity) mContext)).externalPicturePreview(position,
                            "/custom_file", localList);
                    PictureSelector.create(((ProjectBaseEditActivity) mContext)).externalPicturePreview(position, localList);
//                    if (mImgChangeCallback != null) {
//                        mImgChangeCallback.onImgChange(localList);
//                    }
                }
            });
        }
        if (position == 6) {
            viewAdd.setVisibility(View.GONE);
        }
        return viewAdd;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_add_fault_file_iv_img) {
            selectImgs();
        }
    }

    private void selectImgs() {
        PictureSelector.create(((ProjectBaseEditActivity) mContext))
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(6)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(4, 3)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(true)// 是否显示gif图片 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .selectionMedia(mPaths)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private ImgChangeCallback mImgChangeCallback;

    public void setImgChangeCallback(ImgChangeCallback imgChangeCallback) {
        mImgChangeCallback = imgChangeCallback;
    }

    public interface ImgChangeCallback {
        void onImgChange(List<LocalMedia> paths);

        void onImgDelete(LocalMedia media);

    }


}
