package com.androidybp.basics.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 为了让View不可见的时候  释放掉 drabel  加快内存回收
 */

public class MessImageView extends ImageView {
    public MessImageView(Context context) {
        super(context);
    }

    public MessImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
