package com.escort.carriage.android.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.escort.carriage.android.R;
import com.escort.carriage.android.ui.activity.adapter.ImgPreviewAdapter;
import com.escort.carriage.android.ui.view.MNGestureView;
import com.githang.statusbar.StatusBarCompat;

import com.lzy.ninegrid.ImageInfo;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ImagePreviewActivity extends Activity implements ViewTreeObserver.OnPreDrawListener {

    public static final String IMAGE_INFO = "IMAGE_INFO";
    public static final String CURRENT_ITEM = "CURRENT_ITEM";

    //退出动画时长
    public static final int ANIMATE_DURATION = 280;

    private RelativeLayout rootView;

    private ImgPreviewAdapter imagePreviewAdapter;
    private List<ImageInfo> imageInfo;
    private int currentItem;
    private int imageHeight;
    private int imageWidth;
    private int screenWidth;
    private int screenHeight;
    private MNGestureView mnGestureView;
    private float mAlpha = 1f;
    private ColorDrawable colorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view_t);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final TextView tv_pager = (TextView) findViewById(R.id.tv_pager);
        rootView = (RelativeLayout) findViewById(R.id.rl_rootView);
        mnGestureView = (MNGestureView) findViewById(R.id.mnGestureView);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;

        //设置背景色，后面需要为其设置渐变动画
        colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.black));
        rootView.setBackground(colorDrawable);

        Intent intent = getIntent();
        imageInfo = (List<ImageInfo>) intent.getSerializableExtra(IMAGE_INFO);
        currentItem = intent.getIntExtra(CURRENT_ITEM, 0);

        imagePreviewAdapter = new ImgPreviewAdapter(this, imageInfo);
        ((ImgPreviewAdapter) imagePreviewAdapter).setFirstShowPosition(currentItem);

        viewPager.setAdapter(imagePreviewAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                tv_pager.setText(String.format("%1$d/%2$d", currentItem + 1, imageInfo.size()));
            }
        });
        StatusBarCompat.setTranslucent(this.getWindow(), true);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_pager.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        tv_pager.setFitsSystemWindows(true);
        tv_pager.setText(String.format("%1$d/%2$d", currentItem + 1, imageInfo.size()));


        mnGestureView.setOnGestureListener(new MNGestureView.OnCanSwipeListener() {
            @Override
            public boolean canSwipe() {
                View view = imagePreviewAdapter.getPrimaryItem();
                PhotoView imageView = view.findViewById(R.id.pv);

                if (imageView.getScale() != 1.0) {
                    return false;
                }
                return true;
            }
        });

        mnGestureView.setOnSwipeListener(new MNGestureView.OnSwipeListener() {
            @Override
            public void downSwipe(float scaleX, float scaleY, float translationX, float translationY) {
                /*finish();
                overridePendingTransition(0, 0);*/

                exitAnimation2(new Runnable() {
                    public void run() {
                        //结束动画要做的操作
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }, scaleX, scaleY, translationX, translationY);
            }

            @Override
            public void onSwiping(float deltaY) {
                tv_pager.setVisibility(View.GONE);

                float alpha = 1 - deltaY / 700;
                if (alpha < 0.3) {
                    alpha = 0.3f;
                }
                if (alpha > 1) {
                    alpha = 1;
                }
                mAlpha = alpha;
                rootView.setBackgroundColor(evaluateArgb(alpha, Color.TRANSPARENT, Color.BLACK));
            }

            @Override
            public void overSwipe() {
                if (imageInfo.size() <= 1) {
                    tv_pager.setVisibility(View.GONE);
                } else {
                    tv_pager.setVisibility(View.VISIBLE);
                }
                mAlpha = 1f;
                rootView.setBackgroundColor(evaluateArgb(1f, Color.TRANSPARENT, Color.BLACK));
            }
        });
    }

    @Override
    public void onBackPressed() {
        //finishActivityAnim();
        exitActivity();
    }

    /**
     * 绘制前开始动画
     */
    @Override
    public boolean onPreDraw() {
        rootView.getViewTreeObserver().removeOnPreDrawListener(this);
        final View view = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final ImageInfo imageData = imageInfo.get(currentItem);
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long playTime = animation.getCurrentPlayTime();
                float fraction = duration > 0 ? (float) playTime / duration : 1f;
                if (fraction > 1) fraction = 1;
                view.setTranslationX(evaluateInt(fraction, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.getWidth() / 2, 0));
                view.setTranslationY(evaluateInt(fraction, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.getHeight() / 2, 0));
                view.setScaleX(evaluateFloat(fraction, vx, 1));
                view.setScaleY(evaluateFloat(fraction, vy, 1));
                view.setAlpha(fraction);
                rootView.setBackgroundColor(evaluateArgb(fraction, Color.TRANSPARENT, Color.BLACK));
            }
        });
        addIntoListener(valueAnimator);
        valueAnimator.setDuration(200);
        valueAnimator.start();
        return true;
    }

    /**
     * activity的退场动画
     */
    public void finishActivityAnim() {
        final View view = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final ImageInfo imageData = imageInfo.get(currentItem);
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = mAlpha;
                if (mAlpha == 1) {
                    long duration = animation.getDuration();
                    long playTime = animation.getCurrentPlayTime();
                    fraction = duration > 0 ? (float) playTime / duration : 1f;
                    if (fraction > 1) fraction = 1;
                }
                view.setTranslationX(evaluateInt(fraction, 0, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.getWidth() / 2));
                view.setTranslationY(evaluateInt(fraction, 0, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.getHeight() / 2));
                view.setScaleX(evaluateFloat(fraction, 1, vx));
                view.setScaleY(evaluateFloat(fraction, 1, vy));
                view.setAlpha(1 - fraction);
                rootView.setBackgroundColor(evaluateArgb(fraction, Color.BLACK, Color.TRANSPARENT));
            }
        });
        addOutListener(valueAnimator);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    public void exitActivity() {
        exitAnimation(new Runnable() {
            public void run() {
                //结束动画要做的操作
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    private void exitAnimation2(final Runnable endAction, float scaleX, float scaleY, float translationX, float translationY) {
        final View curPhotoView = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final ImageInfo imageData = imageInfo.get(currentItem);
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;
        //缩小动画
        /*curPhotoView.setPivotX(0);
        curPhotoView.setPivotY(0);
        curPhotoView.setScaleX(1);
        curPhotoView.setScaleY(1);
        curPhotoView.setTranslationX(0);
        curPhotoView.setTranslationY(0);*/

        //ScaleX 控制X方向的缩放倍数
        //ScaleY	控制Y方向的缩放倍数
        //TranslationX	控制X方向的位移
        //TranslationY	控制Y方向的位移

        Matrix matrix = new Matrix(mnGestureView.getChildAt(0).getMatrix());

        float[] values = new float[9];
        matrix.getValues(values);

        float toScale;
        if (imageData.imageViewWidth > imageData.imageViewHeight) {
            toScale = imageData.imageViewWidth / (values[Matrix.MSCALE_X] * imageWidth);
        } else {
            toScale = imageData.imageViewHeight / (values[Matrix.MSCALE_Y] * imageHeight);
        }

        //values[MTRANS_X],values[MTRANS_Y]）表示图片左上角的位移情况

        matrix.getValues(values);
        float dx = imageData.imageViewX - values[Matrix.MTRANS_X];
        float dy = imageData.imageViewY - values[Matrix.MTRANS_Y];

        int[] screenLocation = new int[2];
        curPhotoView.getLocationOnScreen(screenLocation);
        int xDelta = imageData.imageViewX - screenLocation[0];
        int yDelta = imageData.imageViewY - screenLocation[1];

        //ScaleX 控制X方向的缩放倍数
        //ScaleY	控制Y方向的缩放倍数
        //TranslationX	控制X方向的位移
        //TranslationY	控制Y方向的位移

        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        curPhotoView.animate().setDuration(ANIMATE_DURATION).scaleX(scaleX).scaleY(scaleY).
                translationX(translationX).translationY(translationY).setInterpolator(sInterpolator).withEndAction(endAction);
        //设置背景渐透明
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
        bgAnim.setDuration(ANIMATE_DURATION);
        bgAnim.start();
    }

    private void exitAnimation(final Runnable endAction) {
        final View curPhotoView = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final ImageInfo imageData = imageInfo.get(currentItem);
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;
        //缩小动画
        curPhotoView.setPivotX(0);
        curPhotoView.setPivotY(0);
        curPhotoView.setScaleX(1);
        curPhotoView.setScaleY(1);
        curPhotoView.setTranslationX(0);
        curPhotoView.setTranslationY(0);

        int[] screenLocation = new int[2];
        curPhotoView.getLocationOnScreen(screenLocation);
        int xDelta = imageData.imageViewX - screenLocation[0];
        int yDelta = imageData.imageViewY - screenLocation[1];

        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        curPhotoView.animate().setDuration(ANIMATE_DURATION).scaleX(vx).scaleY(vy).
                translationX(xDelta).translationY(yDelta).setInterpolator(sInterpolator).withEndAction(endAction);
        //设置背景渐透明
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
        bgAnim.setDuration(ANIMATE_DURATION);
        bgAnim.start();
    }

    public void setMyAnimation() {
        final View curPhotoView = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final ImageInfo imageData = imageInfo.get(currentItem);
        final float toScaleX = imageData.imageViewWidth * 1.0f / imageWidth;
        final float toScaleY = imageData.imageViewHeight * 1.0f / imageHeight;
        //缩小动画
        curPhotoView.setPivotX(0);
        curPhotoView.setPivotY(0);
        curPhotoView.setScaleX(1);
        curPhotoView.setScaleY(1);
        curPhotoView.setTranslationX(0);
        curPhotoView.setTranslationY(0);

        int[] screenLocation = new int[2];
        curPhotoView.getLocationOnScreen(screenLocation);
        int toTranslateX = imageData.imageViewX - screenLocation[0];
        int toTranslateY = imageData.imageViewY - screenLocation[1];


        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat("translateX", 0.0f, toTranslateX);
        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translateY", 0.0f, toTranslateY);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, toScaleX);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, toScaleY);
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(translateX, translateY, scaleX, scaleY);
        animator.setDuration(150);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
    }

    /**
     * 计算图片的宽高
     */
    private void computeImageWidthAndHeight(ImageView imageView) {
        // 获取真实大小
        Drawable drawable = imageView.getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        // 计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
        float h = screenHeight * 1.0f / intrinsicHeight;
        float w = screenWidth * 1.0f / intrinsicWidth;
        if (h > w) h = w;
        else w = h;

        // 得出当宽高至少有一个充满的时候图片对应的宽高
        imageHeight = (int) (intrinsicHeight * h);
        imageWidth = (int) (intrinsicWidth * w);
    }

    /**
     * 进场动画过程监听
     */
    private void addIntoListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setBackgroundColor(0x0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 退场动画过程监听
     */
    private void addOutListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setBackgroundColor(0x0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * Integer 估值器
     */
    public Integer evaluateInt(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

    /**
     * Float 估值器
     */
    public Float evaluateFloat(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * Argb 估值器
     */
    public int evaluateArgb(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24//
                | (startR + (int) (fraction * (endR - startR))) << 16//
                | (startG + (int) (fraction * (endG - startG))) << 8//
                | (startB + (int) (fraction * (endB - startB)));
    }
}
