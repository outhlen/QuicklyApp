package com.escort.carriage.android.ui.view.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自适应的GridView
 * 解决高度是wrap_content时,会有滚动的问题
 * 不过预览界面成空白了
 */
public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
