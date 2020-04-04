package com.androidybp.basics.ui.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import com.androidybp.basics.ui.manager.activity.ProjectActivityManager;

/**
 * Activity基本的父类
 */
public class ProjectBaseActivity extends AppCompatActivity {
    protected long frontClickTime;//记录第一次用点击的时间 long值
    public boolean isPageexist = true;//当前页面是否存在
    public boolean isPagevisible = true;//当前页面是否可见
    protected boolean isClickIntercept = true;//防止多手指并发点击相应事件  拦截  默认为true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProjectActivityManager.getManager().addActivity(this);
        super.onCreate(savedInstanceState);
        isPageexist = true;

    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isClickIntercept) {
            switch (ev.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    return true;

            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断用户是否是频繁点击，判断条件是在一秒内不能频繁点击
     *
     * @return true -- 是频繁点击    ；  false -- 不是频繁点击
     */
    protected synchronized boolean isFrequentlyClick() {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - frontClickTime) > 800) {
            frontClickTime = currentClickTime;
            return false;
        }
        frontClickTime = currentClickTime;
        return true;
    }

    @Override
    protected void onStart() {
        isPagevisible = true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        isPagevisible = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ProjectActivityManager.getManager().removeActivity(this);
        isPageexist = false;
        super.onDestroy();
    }

}
