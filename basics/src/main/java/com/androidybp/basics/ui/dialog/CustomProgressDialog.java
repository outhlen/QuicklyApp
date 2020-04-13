package com.androidybp.basics.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidybp.basics.R;
import com.androidybp.basics.ui.view.MessImageView;

import java.util.Timer;
import java.util.TimerTask;


public class CustomProgressDialog extends Dialog {
    private static AnimationDrawable animationDrawable;

    public CustomProgressDialog(Context context) {
        this(context, R.style.CustomProgressDialog);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(false);
        createProgressDialog(context);
    }


    private void createProgressDialog(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        animationDrawable = (AnimationDrawable) (inflate.findViewById(R.id.progress_bar)).getBackground();
        animationDrawable.start();
        setContentView(inflate);
        getWindow().getAttributes().gravity = Gravity.CENTER;
    }


    /**
     * 设置提示内容
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);
        if (strMessage != null) {
            tvMsg.setText(strMessage);
        }
        return this;
    }

    /**
     * 关闭dialog
     */
    public void dismissDialog(){
        stopAnim();
        dismiss();
    }

    /**
     * 显示加载成功的 图片后 关闭 提示框
     */
    public void showSuccess(){
        showResultIv(R.drawable.load_success);
        Timer timer = new Timer();
        timer.schedule(new MyTimrTask(), 800);
    }

    /**
     * 显示加载失败的 图片后 关闭 提示框
     */
    public void showFail(){
        showResultIv(R.drawable.load_fail);
        Timer timer = new Timer();
        timer.schedule(new MyTimrTask(), 800);
    }


    /**
     * 隐藏加载动画 显示加载状态图片
     * @param imageRes
     */
    private void showResultIv(int imageRes){
        //关闭动画
        stopAnim();
        //显示成功标示
        ImageView ivResult = findViewById(R.id.iv_loading_result);
        ivResult.setImageResource(imageRes);
        ivResult.setVisibility(View.VISIBLE);
        //隐藏加载动画
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.id_tv_loadingmsg).setVisibility(View.GONE);
    }

    /**
     * 关闭加载动画
     */
    private void stopAnim() {
        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }
    }

    class MyTimrTask extends TimerTask{

        @Override
        public void run() {
            dismiss();
        }
    }

}