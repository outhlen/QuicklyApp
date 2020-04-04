package com.androidybp.basics.glide.assist;

import android.widget.AbsListView;

import com.androidybp.basics.glide.GlideManager;

/**
 * Created by yangbp
 */
public class GlideListViewOnScrollListener implements AbsListView.OnScrollListener {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                //滑动停止
                GlideManager.getGlideManager().resumeRequests();
                break;
            case SCROLL_STATE_FLING:
                //正在滚动
                GlideManager.getGlideManager().pauseRequests();
                break;

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
