package com.androidybp.basics.ui.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvp.presenter.PresenterActInterface;
import com.androidybp.basics.ui.mvp.presenter.PresenterFrginterface;

public abstract class BaseMvpActivity<T extends PresenterActInterface, D> extends ProjectBaseActivity implements MvpViewInterface<D> {

    protected T pagePresenter;

    /**
     * 子类实现 获取页面布局
     * @return
     */
    protected abstract int getContentView();

    /**
     * 子类实现 获取页面布局
     * @return
     */
    protected abstract void initUI(Bundle savedInstanceState);

    /**
     * 创建当前页面强绑定的
     * @return
     */
    public abstract T createPresenter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initUI(savedInstanceState);
        pagePresenter = createPresenter();
        if(pagePresenter != null){
            pagePresenter.bindingView(this);
            pagePresenter.onCreate();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(pagePresenter != null){
            pagePresenter.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(pagePresenter != null){
            pagePresenter.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pagePresenter != null){
            pagePresenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pagePresenter != null){
            pagePresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(pagePresenter != null){
            pagePresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
        if(pagePresenter != null){
            pagePresenter.onDestroy();
        }
        clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(pagePresenter != null){
            pagePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(pagePresenter != null){
            pagePresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(pagePresenter != null){
            pagePresenter.onNewIntent(intent);
        }
    }

    @Override
    public void showUplodingAnim(int type, String str) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, str);
    }

    @Override
    public void hintUplodingAnim(int type, boolean isFlag) {
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
    }
}
