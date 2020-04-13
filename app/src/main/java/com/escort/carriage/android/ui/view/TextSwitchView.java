package com.escort.carriage.android.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.escort.carriage.android.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 滚动文本框
 */
public class TextSwitchView extends TextSwitcher implements ViewFactory {
    private int index = -1;
    private Context context;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    index = next(); //取得下标值
                    updateText();  //更新TextSwitcherd显示内容;
                    break;
            }
        }
    };
    private String[] resources ;
    private Timer timer;

    public TextSwitchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        if (timer == null)
            timer = new Timer();
        this.setFactory(this);
        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.in_animation));
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.out_animation));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChilClickListener != null) {
                    mChilClickListener.onClick(index);
                }
            }
        });
    }

    public void setResources(String[] res) {
        this.resources = res;
    }

    public void setTextStillTime(long time) {
        if (timer == null) {
            timer = new Timer();
        } else {
            timer.scheduleAtFixedRate(new MyTask(), 1, time);//每3秒更新
        }
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }

    private int next() {
        int flag = index + 1;
        if (flag > resources.length - 1) {
            flag = flag - resources.length;
        }
        return flag;
    }

    private void updateText() {
        this.setText(resources[index]);
    }

    @Override
    public View makeView() {
        TextView tv = new TextView(context);
        tv.setTextSize(dip2px(getContext(), 5f));
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
//        tv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        return tv;
    }

    private ChilClickListener mChilClickListener;
    public interface ChilClickListener{
        void onClick(int index);
    }
    public void setChilClickListener(ChilClickListener chilClickListener) {
        mChilClickListener = chilClickListener;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
