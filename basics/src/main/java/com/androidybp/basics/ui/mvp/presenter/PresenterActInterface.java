package com.androidybp.basics.ui.mvp.presenter;

import android.content.Intent;
import androidx.annotation.Nullable;
import com.androidybp.basics.ui.mvp.view.MvpViewInterface;

public interface PresenterActInterface<T extends MvpViewInterface> {

    /**
     * 绑定View的接口
     */
    void bindingView(T view);

    /**
     * 点击事件
     * @param type 类型
     */
    void onClick(int type);

    /**
     * 退出销毁数据的方法
     */
    void clear();


    void onCreate();

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void onNewIntent(Intent intent);

}
