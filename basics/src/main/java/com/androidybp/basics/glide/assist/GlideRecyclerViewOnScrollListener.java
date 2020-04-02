package com.androidybp.basics.glide.assist;

import androidx.recyclerview.widget.RecyclerView;
import com.androidybp.basics.glide.GlideManager;


/**
 * Created by yangbp
 */
public class GlideRecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                //滑动停止
                GlideManager.getGlideManager().resumeRequests();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
            case RecyclerView.SCROLL_STATE_SETTLING:
                //正在滚动
                GlideManager.getGlideManager().pauseRequests();
                break;

        }
    }
}
