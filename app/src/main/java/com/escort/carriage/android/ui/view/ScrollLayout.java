package com.escort.carriage.android.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.androidybp.basics.utils.hint.LogUtils;


/**
 * 抽屉布局
 */
public class ScrollLayout extends FrameLayout {
    ViewDragHelper viewDragHelper;
    View mainView;
    View bottomView;
    int dragRange;
    private int flag;

    private int mTop;
    private int mHeight;

    Handler handler = new Handler();
    private boolean isFrist = true;
    public ScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        /**
         * 判断是否需要捕获View的触摸时间
         * @param child 当前触摸的View
         * @param pointerId 触摸点索引
         * @return true 捕获, false 不捕获
         */
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return/* child == mainView || */ child == bottomView;
//            return true;
        }

        /**
         * 当一个View被捕获触摸事件时执行
         * @param capturedChild 被捕获触摸事件的View
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /**
         * 获取垂直方向拖拽的范围,然而目前并没有用
         * 该方法的返回值用来作为判断滑动方向的条件之一
         * @param child
         * @return
         */
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
//            return 200;
            return super.getViewVerticalDragRange(child);
        }

        /**
         * 用来修正或指定子View在垂直方向上的移动
         * @param child 子View
         * @param top viewDragHelper计算好的View的最新的left的值,left=view.getLeft()+dy
         * @param dy 本次垂直移动的距离
         * @return 返回的值表示我们真正想让View移动的距离
         */
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
//            return super.clampViewPositionVertical(child, top, dy);
            LogUtils.showE("LHF", "clampViewPositionVertical = " + top);
            if (top > dragRange){
                top = dragRange;
            }else if (top < 0){
                top = 0;
            }
            return top;
        }

        /**
         * 当View移动的时候调用
         * @param changedView
         * @param left 当VIew移动之后最新的left
         * @param top 当VIew移动之后最新的top
         * @param dx 水平移动的距离
         * @param dy 垂直移动的距离
         */
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //不断的获取top坐标用来判断是否已经滑动到顶部
            mTop = top;
            mainView.setVisibility(VISIBLE);
            if (mOnScrollCallback != null){
                mOnScrollCallback.callback(true);
            }
            if (changedView == mainView){
                LogUtils.showD("LHF", "mainView移动了");
            }
            if (changedView == bottomView){
                LogUtils.showD("LHF", "bottomView移动了" + top);
                if (top == 0){
                    mainView.setVisibility(GONE);
                    if (mOnScrollCallback != null){
                        mOnScrollCallback.callback(false);
                    }
                    //滑动到顶部
                    viewDragHelper.smoothSlideViewTo(bottomView, 0, 0);
                    postInvalidateOnAnimation();
                }
//                if (top <= 700 && top >= 500){
                    if (mOnScrollCallback != null){
                        mOnScrollCallback.callback(top);
                    }
//                }
            }
        }

        /**
         * 当手指抬起
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.e("LHF", "onViewReleased: 手指抬起");
            if (bottomView.getTop() > dragRange / 2){
                //滑到底部
                viewDragHelper.smoothSlideViewTo(bottomView, 0, dragRange);
            }else {
                //滑动到顶部
                viewDragHelper.smoothSlideViewTo(bottomView, 0, 0);
            }
            postInvalidateOnAnimation();
        }
    };

    /**
     * 当ViewGroup将子View全部添加进来后执行的,在onMeasure之前执行的
     * 可用于初始化子View的引用,但还不能获取子View的宽高
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mainView = getChildAt(0);
        bottomView = getChildAt(1);
//        mainView.setVisibility(GONE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //让viewDragHelper帮助我们判断是否应该拦截
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        LogUtils.showE("LHF", "onInterceptTouchEvent = " + result);
        if (count ==1){
//            count++;
        }
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                result = true;
//        }
//        return super.onInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //让viewDragHelper帮助我们处理触摸事件
        viewDragHelper.processTouchEvent(event);
        LogUtils.showE("LHF", "onTouchEvent = ");
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dragRange = (int) (getMeasuredHeight()*0.7);
        mHeight = getHeight();
        int measuredHeight = getMeasuredHeight();
        LogUtils.showE("LHF", "height = " + mHeight);
        LogUtils.showE("LHF", "measuredHeight = " + measuredHeight);
        setHeight();
//        bottomView.setBottom();
//        滑到底部
        viewDragHelper.smoothSlideViewTo(bottomView, 0, dragRange);
        postInvalidateOnAnimation();
    }

    public void setHeight(){
        ViewGroup.LayoutParams layoutParams = mainView.getLayoutParams();
        layoutParams.height = (int) (getMeasuredHeight()*0.7);
        mainView.setLayoutParams(layoutParams);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(ScrollLayout.this);
        }
    }

    public void setMainViewGone(){
        mainView.setVisibility(GONE);
    }

    public void scrollToEnd(){
        //滑到底部
        viewDragHelper.smoothSlideViewTo(bottomView, 0, dragRange);
        postInvalidateOnAnimation();

    }
    int count = 0;
    boolean aaa = true;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        LogUtils.showE("LHF", "onLayout = " + top);
        //通过顶部坐标判断 button 是否滑动到顶部，滑动到顶部就不走默认的onLayout方法
//        if(!(mTop <= -bottomView.getMeasuredHeight())){
//            super.onLayout(changed, left, top, right, bottom);
//        }
        LogUtils.showE("LHF", "前count = " + count);
        //子View只有两个,所以onLayout只需要执行两次,
        if (count <= 1){
            super.onLayout(changed, left, top, right, bottom);
            bottomView.layout(left, mTop, right, bottom + dragRange);
//            bottomView.layout(0, dragRange, bottomView.getRight(), mHeight);//废弃

        }
        if (count >= 1/*  || count == 3|| count == 4*/){
            bottomView.layout(left, mTop, right, bottom + dragRange);
        }
        LogUtils.showE("LHF", "后count = " + count);
        count ++;
        if (count > 3){
            count = 2;
        }
//        isFrist = false;
//        bottomView.setTop(dragRange);
//        bottomView.layout(0, dragRange, getMeasuredWidth(), bottomView.getBottom());
//        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtils.showE("LHF", "onDraw");
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        LogUtils.showE("LHF", "onScrollChanged");
    }

    private OnScrollCallback mOnScrollCallback;

    public void setOnScrollCallback(OnScrollCallback onScrollCallback) {
        mOnScrollCallback = onScrollCallback;
    }

    public interface OnScrollCallback{
        void callback(boolean isOpen);
        void callback(int state);
    }
}
