package com.escort.carriage.android.ui.view.imgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class RoundedImagView extends AppCompatImageView {

    private int cornerSize = ResourcesTransformUtil.dip2px(10);

    private int defaultColor = Color.WHITE;
    // 如果只有其中一个有值，则只画一个圆形边框
    private int backageColor = 0;

    private Paint paint;

    public RoundedImagView(Context context) {
        this(context, null);
    }

    public RoundedImagView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        setCustomAttributes(context, attrs);
        setPaint();

    }

    private void setCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.roundedimageview);
        cornerSize = a.getDimensionPixelSize(
                R.styleable.roundedimageview_cornerRadius, cornerSize);
        backageColor = a
                .getColor(R.styleable.roundedimageview_backage_color,
                        defaultColor);
    }

    public void setDefaultColor(int colorRes){
        backageColor = ResourcesTransformUtil.getColor(colorRes);
        setPaint();
    }

    public void setPaint(){
        paint = new Paint();

        paint.setColor(backageColor);

        paint.setAntiAlias(true);//消除锯齿
    }

    @Override

    public void draw(Canvas canvas) {

        super.draw(canvas);

        drawLeftTop(canvas);

        drawRightTop(canvas);

        drawLeftBottom(canvas);

        drawRightBottom(canvas);

    }

    private void drawLeftTop(Canvas canvas) {

        Path path = new Path();

        path.moveTo(0, cornerSize);

        path.lineTo(0, 0);

        path.lineTo(cornerSize, 0);

        path.arcTo(new RectF(0, 0, cornerSize * 2, cornerSize * 2), -90, -90);

        path.close();

        canvas.drawPath(path, paint);

    }

    private void drawLeftBottom(Canvas canvas) {

        Path path = new Path();

        path.moveTo(0, getHeight() - cornerSize);

        path.lineTo(0, getHeight());

        path.lineTo(cornerSize, getHeight());

        path.arcTo(new RectF(0, // x

                getHeight() - cornerSize * 2,// y

                cornerSize * 2,// x

                getHeight()// getWidth()// y

        ), 90, 90);

        path.close();

        canvas.drawPath(path, paint);

    }

    private void drawRightBottom(Canvas canvas) {

        Path path = new Path();

        path.moveTo(getWidth() - cornerSize, getHeight());

        path.lineTo(getWidth(), getHeight());

        path.lineTo(getWidth(), getHeight() - cornerSize);

        RectF oval = new RectF(getWidth() - cornerSize * 2, getHeight()

                - cornerSize * 2, getWidth(), getHeight());

        path.arcTo(oval, 0, 90);

        path.close();

        canvas.drawPath(path, paint);

    }

    private void drawRightTop(Canvas canvas) {

        Path path = new Path();

        path.moveTo(getWidth(), cornerSize);

        path.lineTo(getWidth(), 0);

        path.lineTo(getWidth() - cornerSize, 0);

        path.arcTo(new RectF(getWidth() - cornerSize * 2, 0, getWidth(),

                0 + cornerSize * 2), -90, 90);

        path.close();

        canvas.drawPath(path, paint);

    }

    public int getCornerSize() {

        return cornerSize;

    }

    public void setCornerSize(int cornerSize) {

        this.cornerSize = cornerSize;

    }

}

