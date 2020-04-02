package com.androidybp.basics.ui.base;


import android.view.MotionEvent;

/**
 * 只能单点触摸的Activity
 */
public class ProjectBaseSingleClickActivity extends ProjectBaseActivity {


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_POINTER_DOWN:
                return true;
        }

        return super.dispatchTouchEvent(ev);
    }


}
