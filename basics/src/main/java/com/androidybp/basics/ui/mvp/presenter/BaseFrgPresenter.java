package com.androidybp.basics.ui.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.androidybp.basics.ui.mvp.model.ModelInterface;
import com.androidybp.basics.ui.mvp.view.MvpViewInterface;

public abstract class BaseFrgPresenter<T extends MvpViewInterface> implements PresenterFrginterface<T> {

    protected T view;
    protected ModelInterface model;

    public boolean pageExist = true;//当前页面是否存在
    public boolean pageVisible = true;//当前页面是否可见
    public boolean pageFocus = true;//判断当前页面是否可以获取焦点

    public abstract ModelInterface getModel();
    public abstract void initView();

    @Override
    public void bindingView(T view) {
        this.view = view;
        model = getModel();
    }

    @Override
    public void onClick(int type, Object obj) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView() {
        pageExist = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    @Override
    public void onStart() {
        pageVisible = true;
    }

    @Override
    public void onResume() {
        pageFocus = true;
    }

    @Override
    public void onPause() {
        pageFocus = false;
    }

    @Override
    public void onStop() {
        pageVisible = false;
    }

    @Override
    public void onDestroyView() {
        pageExist = false;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }


    @Override
    public void clear() {
        view = null;
        if(model != null){
            model.clear();
            model = null;
        }
    }
}
