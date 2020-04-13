package com.androidybp.basics.ui.mvp.presenter;

import android.content.Intent;
import androidx.annotation.Nullable;
import com.androidybp.basics.ui.mvp.model.BaseMvpModel;
import com.androidybp.basics.ui.mvp.model.ModelInterface;
import com.androidybp.basics.ui.mvp.view.MvpViewInterface;

public abstract class BaseActPresenter<T extends MvpViewInterface> implements PresenterActInterface<T> {

    protected T view;
    public boolean isPageexist = true;//当前页面是否存在
    public boolean isPagevisible = true;//当前页面是否可见
    protected ModelInterface model;

    public abstract ModelInterface getModel();
    public abstract void initView();

    @Override
    public void bindingView(T view) {
        this.view = view;
    }



    @Override
    public void onClick(int type) {

    }


    @Override
    public void clear() {
        view = null;
        if(model != null){
            model.clear();
            model = null;
        }
    }

    @Override
    public void onCreate() {
        isPageexist = true;
        model = getModel();
        initView();
    }

    @Override
    public void onStart() {
        isPagevisible = true;
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        isPagevisible = false;
    }

    @Override
    public void onDestroy() {
        isPageexist = false;
        clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
