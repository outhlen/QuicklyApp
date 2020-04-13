package com.escort.carriage.android.ui.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.bumptech.glide.request.target.SimpleTarget;
import com.escort.carriage.android.R;

public class AdvertisingImageDialog extends PopupWindow implements View.OnClickListener {
    private View mMenuView;
    private ImageView ivClose;
    private ImageView imageView;
    private Callback callback;
    private SimpleTarget<Bitmap> simpleTarget;

    public AdvertisingImageDialog(Activity context, String url, Callback callback) {
        this.callback = callback;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_advertising_image, null);
        showPop(url);
    }

    public void showPop(String url) {
        initPop();
        GlideManager.getGlideManager().loadImage(url, imageView);
        initlistener();
        setPopStyle();
    }

    private void setPopStyle() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);// 0x33000000  0xb0000000
//        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mMenuView.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = mMenuView.findViewById(R.id.rl_layout).getTop();
//                int y=(int) event.getY();
//                if(event.getAction()==MotionEvent.ACTION_UP){
//                    if(y<height){
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
    }

    private void initPop() {
        imageView = (ImageView) mMenuView.findViewById(R.id.imageView);
        ivClose = (ImageView) mMenuView.findViewById(R.id.ivClose);
//        View groupView = (View) mMenuView.findViewById(R.id.group);
//        ColorDrawable dw = new ColorDrawable(0xb0000000);// 0x33000000  0xb0000000
//        //设置SelectPicPopupWindow弹出窗体的背景
//        groupView.setBackgroundDrawable(dw);
    }



    private void initlistener() {
        imageView.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                dismiss();
                break;
            case R.id.ivClose://更新
                dismiss();
                break;

        }
    }

    private void clear(){
        mMenuView = null;
        imageView = null;
        ivClose = null;
        simpleTarget = null;
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        clear();
    }

    public interface Callback{
        void callback(boolean type);
    }
}