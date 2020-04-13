package com.androidybp.basics.ui.view.loading;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class LoadingView extends AppCompatImageView {
    private LoadingDrawable mLoadingDrawable;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        try {
            LoadingRenderer loadingRenderer = new ElectricFanLoadingRenderer.Builder(context).build();
            setLoadingRenderer(loadingRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoadingRenderer(LoadingRenderer loadingRenderer) {
        mLoadingDrawable = new LoadingDrawable(loadingRenderer);
        setImageDrawable(mLoadingDrawable);
    }


    public void setNumber(float number){
        mLoadingDrawable.setNumber(number);
    }

}
