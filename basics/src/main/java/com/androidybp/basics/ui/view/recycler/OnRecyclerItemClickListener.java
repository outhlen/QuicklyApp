package com.androidybp.basics.ui.view.recycler;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerItem item  点击回调
 */

public abstract class OnRecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private GestureDetectorCompat gestureDetector;
    protected long frontClickTime;//记录第一次用点击的时间 long值

      public abstract   void onItemClick(View view, int position);

      public abstract   void onItemLongClick(View view, int position);


    public OnRecyclerItemClickListener(final RecyclerView recyclerView) {
        gestureDetector = new GestureDetectorCompat(recyclerView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        long currentClickTime = System.currentTimeMillis();
                        if((currentClickTime - frontClickTime) < 800){
                            frontClickTime = currentClickTime;
                            return false;
                        }
                        frontClickTime = currentClickTime;
                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null) {
                            onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null) {
                            onItemLongClick(childView,
                                    recyclerView.getChildAdapterPosition(childView));
                        }
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

}

